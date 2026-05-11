from math import isqrt
import random
import sympy as sp

def is_zero(x):
    return sp.simplify(x) == 0

def is_zero_vector(v):
    return all(is_zero(v[i, 0]) for i in range(v.rows))

def is_zero_matrix(A):
    return all(is_zero(A[i, j]) for i in range(A.rows) for j in range(A.cols))

def simplify_matrix(A):
    return A.applyfunc(sp.simplify)

def matrix_equal(A, B):
    return is_zero_matrix(simplify_matrix(A - B))

def rref(A):
    A = sp.Matrix(A)
    n, m = A.shape

    pivots = []
    row = 0

    for col in range(m):
        pivot = None
        for r in range(row, n):
            if not is_zero(A[r, col]):
                pivot = r
                break

        if pivot is None:
            continue

        if pivot != row:
            A.row_swap(row, pivot)

        pivot_value = A[row, col]
        A.row_op(row, lambda _, j: sp.simplify(A[row, j] / pivot_value))

        for r in range(n):
            if r != row and not is_zero(A[r, col]):
                factor = A[r, col]
                A.row_op(r, lambda _, j: sp.simplify(A[r, j] - factor * A[row, j]))

        A = simplify_matrix(A)
        pivots.append(col)
        row += 1

        if row == n:
            break

    return A, pivots

def rank(A):
    _, pivots = rref(A)
    return len(pivots)

def nullspace_basis(A):
    A = sp.Matrix(A)
    n, m = A.shape
    R, pivots = rref(A)

    pivot_set = set(pivots)
    free_cols = [j for j in range(m) if j not in pivot_set]

    basis = []
    for free_col in free_cols:
        x = sp.Matrix.zeros(m, 1)
        x[free_col, 0] = 1

        for i, pivot_col in enumerate(pivots):
            x[pivot_col, 0] = sp.simplify(-R[i, free_col])

        basis.append(simplify_matrix(x))

    return basis

def cols_to_matrix(vectors):
    return sp.Matrix.hstack(*vectors)

def independent_basis(vectors):
    if len(vectors) == 0:
        return []

    n = vectors[0].rows
    basis = []
    current_rank = 0

    for v in vectors:
        M = cols_to_matrix(basis + [v])
        new_rank = rank(M)
        if new_rank > current_rank:
            basis.append(v)
            current_rank = new_rank

    return basis

def basis_extension(base_basis, ambient_basis):
    if len(ambient_basis) == 0:
        return []

    basis = independent_basis(base_basis)
    current_rank = rank(cols_to_matrix(basis)) if len(basis) > 0 else 0

    added = []
    for v in ambient_basis:
        M = cols_to_matrix(basis + [v])
        new_rank = rank(M)
        if new_rank > current_rank:
            basis.append(v)
            added.append(v)
            current_rank = new_rank

    return added

def image_of_space(A, basis):
    images = []
    for v in basis:
        Av = simplify_matrix(A * v)
        if not is_zero_vector(Av):
            images.append(Av)
    return independent_basis(images)


def characteristic_polynomial(A):
    x = sp.Symbol('x')
    n = A.shape[0]
    poly_expr = sp.expand((x * sp.eye(n) - A).det())
    return sp.Poly(poly_expr, x)

def getEigen(A, eps=1e-20):
    poly = characteristic_polynomial(A)
    degree = poly.degree()

    exact_roots = sp.roots(poly)

    result = []

    for root, mult in exact_roots.items():
        result.append((sp.simplify(root), int(mult)))

    exact_count = sum(exact_roots.values())

    #  получили точные вещественные корни
    if exact_count == degree:
        result.sort(key=lambda item: sp.default_sort_key(item[0]))
        return result
    # если их оказалось мало - ищем на комплексной плоскости
    print("Корней оказалось недостаточно, ищем на комплексной плоскости")
    numeric_roots = sp.nroots(poly)

    used = [False] * len(numeric_roots)

    for exact_root, exact_mult in exact_roots.items():
        matched = 0

        for i, num_root in enumerate(numeric_roots):
            if used[i]:
                continue

            diff = sp.N(num_root - exact_root)

            if abs(complex(diff)) < eps:
                used[i] = True
                matched += 1

                if matched == exact_mult:
                    break

    for i, root in enumerate(numeric_roots):
        if used[i]:
            continue

        mult = 1
        used[i] = True

        for j in range(i + 1, len(numeric_roots)):
            if used[j]:
                continue

            if abs(complex(root - numeric_roots[j])) < eps:
                used[j] = True
                mult += 1

        result.append((sp.N(root, 20), mult))

    result.sort(
        key=lambda item: (
            float(sp.re(item[0])),
            float(sp.im(item[0]))
        )
    )

    return result

def kernel_bases_of_powers(N, max_power):
    bases = {0: []}
    current = sp.eye(N.shape[0])

    for k in range(1, max_power + 1):
        current = simplify_matrix(current * N)
        bases[k] = nullspace_basis(current)

    return bases

def jordan_chains(A, eigenvalue, mult):
    n = A.shape[0]
    N = simplify_matrix(A - eigenvalue * sp.eye(n))

    K = kernel_bases_of_powers(N, mult)
    K[mult + 1] = K[mult]

    vectors_by_length = {k: [] for k in range(1, mult + 1)}

    for k in range(mult, 0, -1):
        used = independent_basis(K[k - 1] + image_of_space(N, K[k + 1]))
        bottoms = basis_extension(used, K[k])
        vectors_by_length[k] = bottoms

    chains = []
    for k in range(mult, 0, -1):
        for u in vectors_by_length[k]:
            vecs = [u]
            cur = u
            for _ in range(1, k):
                cur = simplify_matrix(N * cur)
                vecs.append(cur)

            chain = list(reversed(vecs))
            chains.append((eigenvalue, chain))

    return chains

def jordan_decomposition(A):
    A =  sp.Matrix(A)

    n = A.rows
    jordan_basis = []
    blocks = []

    for eigenvalue, mult in getEigen(A):
        chains = jordan_chains(A, eigenvalue, mult)
        for _, chain in chains:
            jordan_basis.extend(chain)
            blocks.append((eigenvalue, len(chain)))

    P = simplify_matrix(cols_to_matrix(jordan_basis))

    J = sp.Matrix.zeros(n, n)
    pos = 0
    for eigenvalue, size in blocks:
        for i in range(size):
            J[pos + i, pos + i] = eigenvalue
        for i in range(size - 1):
            J[pos + i, pos + i + 1] = 1
        pos += size

    return simplify_matrix(J), P


def expr_to_str(x):
    return sp.sstr(sp.simplify(x))

def matrix_to_str(name, A):
    lines = [f"{name} ="]
    for i in range(A.rows):
        row_items = [f"{expr_to_str(A[i, j]):>4}" for j in range(A.cols)]
        lines.append("  [" + ", ".join(row_items) + "]")
    lines.append("")
    return "\n".join(lines)


def random_unimodular_matrix(n, steps=12, value_range=(-3, 3)):
    rng = random.Random()
    P = sp.eye(n)

    for _ in range(steps):
        op = rng.choice(["swap", "neg", "add"])

        if op == "swap":
            i, j = rng.sample(range(n), 2)
            P.row_swap(i, j)

        elif op == "neg":
            i = rng.randrange(n)
            P.row_op(i, lambda v, _: -v)

        else:
            i, j = rng.sample(range(n), 2)
            k = 0
            while k == 0:
                k = rng.randint(value_range[0], value_range[1])
            source_row = [P[i, col] for col in range(n)]
            P.row_op(j, lambda v, col: v + k * source_row[col])

    det = sp.expand(P.det())
    if det not in (1, -1):
        raise ValueError(f"Сгенерирована неунимодулярная матрица, det={det}")
    return simplify_matrix(P)

def is_square_number(n):
    r = isqrt(n)
    return r * r == n

def random_nonsquare(min_d=2, max_d=30):
    while True:
        d = random.randint(min_d, max_d)
        if not is_square_number(d):
            return sp.Integer(d)
        
def random_sympy_number():
    kind = random.choice(["rational", "irrational"])

    if kind == "integer":
        return sp.Integer(random.randint(-5, 5))

    if kind == "rational":
        numerator = random.randint(-10, 10)
        denominator = random.randint(1, 10)
        return sp.Rational(numerator, denominator)

    if kind == "irrational":
        b = 0
        while b == 0:
            b = random.randint(-5, 5)

        b = sp.Integer(b)
        d = random_nonsquare(2, 30)

        return sp.simplify(b * sp.sqrt(d))

def random_blocks(n):
    if n <= 0:
        raise ValueError("n должно быть положительным")

    blocks = []
    remaining = n

    while remaining > 0:
        size = random.randint(1, remaining)
        eigenvalue = random_sympy_number()
        blocks.append((eigenvalue, size))
        remaining -= size

    random.shuffle(blocks)
    return blocks

def make_jordan_matrix(n):
    blocks = random_blocks(n)
    blocks.sort(key=lambda x: (sp.default_sort_key(x[0]), -x[1]))
    J = sp.Matrix.zeros(n, n)
    pos = 0
    for eigenvalue, size in blocks:
        for i in range(size):
            J[pos + i, pos + i] = eigenvalue
        for i in range(size - 1):
            J[pos + i, pos + i + 1] = 1
        pos += size
    return simplify_matrix(J)

def generate_test_matrix():
    size = random.randint(3, 6)
    J = make_jordan_matrix(size)
    P = random_unimodular_matrix(J.rows)
    A = simplify_matrix(P * J * P.inv())
    return A, J, P

def write_case_result(f, A, title, J_src, P_src):
    A = sp.Matrix(A)

    f.write("=" * 100 + "\n")
    f.write(title + "\n")
    f.write("-" * 100 + "\n")
    f.write(matrix_to_str("A", A))
    f.write("Исходная J\n" + matrix_to_str("J_src", J_src))

    J, P= jordan_decomposition(A)
    f.write("Полученная J\n" +matrix_to_str("J", J))
    f.write("Результат сравнения J с исходной J: " + ("совпадают" if matrix_equal(J, J_src) else "не совпадают") + "\n")
    f.write("Исходная P\n" + matrix_to_str("P_src", P_src))

    f.write("Полученная P\n" +matrix_to_str("P", P))



def demo(output_file):
    # переписать чтобы были алгебр значения
    A_gen, J_src, P_src = generate_test_matrix()
    case=(
        A_gen,
        "Тест"
    )

    with open(output_file, "w", encoding="utf-8") as f:
            write_case_result(f, case[0], case[1], J_src, P_src)


if __name__ == "__main__":
    demo("prod_out.txt")
