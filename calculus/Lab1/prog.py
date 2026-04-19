import numpy as np

EPS = 1e-9

def is_zero_vector(v):
    return np.all(np.abs(v) < EPS)


def clean_matrix(A):
    A = A.copy()
    A[np.abs(A) < EPS] = 0.0
    return A


def to_int_if_close(x):
    rx = round(x)
    if abs(x - rx) > 1e-6:
        raise ValueError(f"Ожидался почти целый коэффициент, получено {x}")
    return int(rx)

# returns матрицу R, индексы ведущих столбцов, таких что R - ступенчатая форма A
# Reduced Row Echelon Form (RREF)
def rref(A):
    A = A.copy().astype(float)
    n, m = A.shape

    pivots = []
    row = 0

    for col in range(m):
        pivot = None
        best_abs = EPS

        # ищем строку с макс элементом в текущем столбце
        for r in range(row, n):
            if abs(A[r, col]) > best_abs:
                best_abs = abs(A[r, col])
                pivot = r

        if pivot is None:
            continue

        # ведущий элемент не в текущей строке - меняем строки местами
        if pivot != row:
            A[[row, pivot]] = A[[pivot, row]]

        # нормируем строку так, чтобы ведущий элемент стал 1
        pivot_value = A[row, col]
        A[row, :] = A[row, :] / pivot_value

        # обнуляем текущий столбец во всех остальных строках
        for r in range(n):
            if r != row and abs(A[r, col]) > EPS:
                factor = A[r, col]
                A[r, :] = A[r, :] - factor * A[row, :]

        A = clean_matrix(A)
        pivots.append(col)
        row += 1

        if row == n:
            break

    return A, pivots


def rank(A):
    _, pivots = rref(A)
    return len(pivots)

# returns kernel
def nullspace_basis(A):
    n, m = A.shape
    # pivots столбцы с ведущими единицами
    R, pivots = rref(A)

    pivot_set = set(pivots)
    # столбцы - свободные переменные
    free_cols = [j for j in range(m) if j not in pivot_set]

    basis = []
    for free_col in free_cols:
        x = np.zeros(m, dtype=float)
        x[free_col] = 1.0

        for i, pivot_col in enumerate(pivots):
            # вычисляем значение ведущей переменной через свободные
            x[pivot_col] = -R[i, free_col]

        x[np.abs(x) < EPS] = 0.0
        basis.append(x)

    return basis


def cols_to_matrix(vectors, n=None):
    if len(vectors) == 0:
        if n is None:
            raise ValueError("Нужно указать n для пустого списка векторов")
        return np.zeros(n, 0)
    return np.column_stack(vectors)


def independent_basis(vectors):
    if len(vectors) == 0:
        return []

    n = len(vectors[0])
    basis = []
    current_rank = 0

    for v in vectors:
        M = cols_to_matrix(basis + [v], n)
        new_rank = rank(M)
        if new_rank > current_rank:
            basis.append(v)
            current_rank = new_rank

    return basis


def basis_extension(base_basis, ambient_basis):
    if len(ambient_basis) == 0:
        return []

    n = len(ambient_basis[0])
    basis = independent_basis(base_basis)
    current_rank = rank(cols_to_matrix(basis, n)) if len(basis) > 0 else 0

    added = []
    for v in ambient_basis:
        M = cols_to_matrix(basis + [v], n)
        new_rank = rank(M)
        if new_rank > current_rank:
            basis.append(v)
            added.append(v)
            current_rank = new_rank

    return added


def image_of_space(A, basis):
    images = []
    for v in basis:
        Av = A @ v
        if not is_zero_vector(Av):
            images.append(Av)
    return independent_basis(images)



# использую Faddeev–LeVerrier algorithm
# @returns коэффициенты для характеристического многочлена вида:
# x^n + coeffs[1] x^(n-1) + ... + coeffs[n], где x - собственное значение
# https://en.wikipedia.org/wiki/Faddeev%E2%80%93LeVerrier_algorithm#:~:text=.-,Example,-%5Bedit%5D
def characteristic_poly_coeffs(A):
    A = np.array(A, dtype=float)
    n = A.shape[0]
    I = np.eye(n, dtype=float)
    B = np.zeros((n, n), dtype=float)
    coeffs = [1.0]

    for k in range(1, n + 1):
        B = A @ (B + coeffs[-1] * I)
        new_coef = -np.trace(B) / k
        coeffs.append(new_coef)

    return [to_int_if_close(c) for c in coeffs]


def poly_eval(coeffs, x):
    value = 0
    for c in coeffs:
        value = value * x + c
    return value


def horner_division(coeffs, root):
    out = [coeffs[0]]
    for c in coeffs[1:]:
        out.append(c + out[-1] * root)
    out.pop()
    return out


def divisors(n):
    n = abs(int(n))
    if n == 0:
        return [0]

    ds = set()
    for d in range(1, int(n**0.5) + 1):
        if n % d == 0:
            ds.add(d)
            ds.add(-d)
            ds.add(n // d)
            ds.add(-(n // d))
    return sorted(ds)

# @param coeffs - коэффициенты характеристического многочлена вида:
# x^n + coeffs[1] x^(n-1) + ... + coeffs[n], где x - собственное значение
# @returns словарь {eigenvalue: multiplicity}
def get_eigen(coeffs):
    coeffs = coeffs[:]
    roots = []

    while len(coeffs) > 1:
        if coeffs[-1] == 0:
            roots.append(0)
            coeffs = horner_division(coeffs, 0)
            continue

        found = False
        for r in divisors(coeffs[-1]):
            if poly_eval(coeffs, r) == 0:
                roots.append(r)
                coeffs = horner_division(coeffs, r)
                found = True
                break

        if not found:
            raise ValueError("Не удалось найти целый корень")

    mult = {}
    for r in roots:
        mult[r] = mult.get(r, 0) + 1

    return dict(sorted(mult.items()))

def kernel_bases_of_powers(N, max_power):
    bases = {0: []}
    current = np.eye(N.shape[0])

    for k in range(1, max_power + 1):
        current = current @ N
        current = clean_matrix(current)
        bases[k] = nullspace_basis(current)

    return bases

def jordan_chains_for_eigenvalue(A, value, mult):
    n = A.shape[0]
    N = A - value * np.eye(n)
    N = clean_matrix(N)

    # получаем такие вектора что N^k * v = 0
    K = kernel_bases_of_powers(N, mult)
    K[mult + 1] = K[mult]

    # нижние вектора для цепочек каждой длины
    vectors_by_length = {k: [] for k in range(1, mult + 1)}

    for k in range(mult, 0, -1):
        # получим недоспустимые вектора: вектора, из ядер пред. степеней, + образы ядер более высоких степеней под действием N
        S = independent_basis(K[k - 1] + image_of_space(N, K[k + 1]))
        # получим вектора для верхов цепочки длины k
        vectors = basis_extension(S, K[k])
        vectors_by_length[k] = vectors

    chains = []
    for k in range(mult, 0, -1):
        for u in vectors_by_length[k]:
            vecs = [u]
            cur = u
            for _ in range(1, k):
                cur = N @ cur
                cur[np.abs(cur) < EPS] = 0.0
                vecs.append(cur)

            chain = list(reversed(vecs))
            chains.append((value, chain))

    return chains

def jordan_decomposition(A):
    A = np.array(A, dtype=float)
    n = A.shape[0]

    if A.shape[0] != A.shape[1]:
        raise ValueError("Матрица не квадратная")

    coeffs = characteristic_poly_coeffs(A)
    eigen_mult = get_eigen(coeffs)

    jordan_basis = []
    blocks = []

    for value, mult in eigen_mult.items():
        chains = jordan_chains_for_eigenvalue(A, value, mult)
        for value2, chain in chains:
            jordan_basis.extend(chain)
            blocks.append((value2, len(chain)))

    P = np.column_stack(jordan_basis)
    P = clean_matrix(P)

    J = np.zeros((n, n), dtype=float)
    pos = 0
    for value, size in blocks:
        for i in range(size):
            J[pos + i, pos + i] = float(value)
        for i in range(size - 1):
            J[pos + i, pos + i + 1] = 1.0
        pos += size

    J = clean_matrix(J)
    return J, P


#=======================================================
def num_to_str(x):
    if abs(x - round(x)) < 1e-9:
        return str(int(round(x)))
    return f"{x:.6g}"


def matrix_to_str(name, A):
    result=""
    result+=f"{name} ="+"\n"
    for row in A:
        result+="  ["+ "  ".join(f"{num_to_str(x):>2}" for x in row) + "]"+"\n"
    result+=("\n")
    return result


def matrices_equal(A, B):
    return np.all(np.abs(A - B) < EPS)


def demo(A, title=""):
    result = "";
    result+="=" * 90+"\n"
    if title:
        result+=title+"\n"
        result+="-" * 90+"\n"

    A = np.array(A)
    result+=matrix_to_str("A", A)

    J, P = jordan_decomposition(A)

    result+="\n"

    result+=matrix_to_str("J", J)
    result+=matrix_to_str("P", P)

    reconstructed = P @ J @ np.linalg.inv(P)
    reconstructed = clean_matrix(reconstructed)

    result+="Проверка A = P J P^(-1):"+ "OK" if matrices_equal(A, reconstructed) else "FAIL"
    open("prog_out.txt", "a", encoding="utf-8").write(result + "\n\n")


if __name__ == "__main__":
    open("prog_out.txt", "w", encoding="utf-8").write("")
    tests = [
        (
            [[1, 1, 0, -1],
             [-1, 3, 0, -1],
             [0, 0, 2, 0],
             [0, 0, 0, 2]],
            "Пример 4x4"
        ),
        (
            [[4, -5, 2],
             [5, -7, 3],
             [6, -9, 4]],
            "Пример 3x3"
        ),
        (
            [[1, 1, -1],
             [0, 2, 1],
             [0, 0, 3]],
            "Диагонализируемая матрица"
        ),
        (
            [[0, 1, 0],
             [0, 0, 1],
             [0, 0, 0]],
            "Нильпотентная матрица: одна клетка размера 3"
        ),
        (
            [[2, 1, 0],
             [0, 2, 1],
             [0, 0, 2]],
            "Одна жорданова клетка размера 3 для λ = 2"
        ),
        
        
    ]

    for A, title in tests:
        demo(A, title)