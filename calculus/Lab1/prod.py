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

def cols_to_matrix(vectors, n=None):
    if len(vectors) == 0:
        if n is None:
            raise ValueError("Для пустого списка векторов нужно указать n")
        return sp.Matrix.zeros(n, 0)
    return sp.Matrix.hstack(*vectors)

def independent_basis(vectors):
    if len(vectors) == 0:
        return []

    n = vectors[0].rows
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

    n = ambient_basis[0].rows
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
        Av = simplify_matrix(A * v)
        if not is_zero_vector(Av):
            images.append(Av)
    return independent_basis(images)


def characteristic_polynomial(A):
    x = sp.Symbol('x')
    n = A.shape[0]
    poly_expr = sp.expand((x * sp.eye(n) - A).det())
    return sp.Poly(poly_expr, x)

def getEigen(A):
    poly = characteristic_polynomial(A)
    raw_roots = poly.all_roots(multiple=True)

    result = []
    for root in raw_roots:
        placed = False
        for item in result:
            if is_zero(item[0] - root):
                item[1] += 1
                placed = True
                break
        if not placed:
            result.append([root, 1])

    return [(value, mult) for value, mult in result]

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

    P = simplify_matrix(cols_to_matrix(jordan_basis, n))

    if rank(P) != n:
        raise ValueError("Матрица перехода P вырождена")

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
        row_items = [f"{expr_to_str(A[i, j]):>2}" for j in range(A.cols)]
        lines.append("  [" + ", ".join(row_items) + "]")
    lines.append("")
    return "\n".join(lines)

def blocks_to_str(blocks):
    return ", ".join(f"(λ={expr_to_str(lam)}, size={size})" for lam, size in blocks)

def jordan_blocks_from_J(J):
    n = J.rows
    blocks = []
    i = 0

    while i < n:
        lam = J[i, i]
        size = 1
        while i + size < n and is_zero(J[i + size, i + size] - lam) and is_zero(J[i + size - 1, i + size] - 1):
            size += 1
        blocks.append((lam, size))
        i += size

    return blocks

def random_unimodular_matrix(n, steps=12, value_range=(-3, 3), seed=None):
    rng = random.Random(seed)
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
        raise ValueError(f"Сгенерирована неунитмодулярная матрица, det={det}")
    return simplify_matrix(P)

def random_blocks(n, eigen_min=-5, eigen_max=5):
    if n <= 0:
        raise ValueError("n должно быть положительным")

    blocks = []
    remaining = n

    while remaining > 0:
        size = random.randint(1, remaining)
        eigenvalue = sp.Integer(random.randint(eigen_min, eigen_max))
        blocks.append((eigenvalue, size))
        remaining -= size

    random.shuffle(blocks)
    return blocks

def make_jordan_matrix(n, eigen_min=-5, eigen_max=5):
    blocks = random_blocks(n, eigen_min, eigen_max)
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

def generate_test_matrix(seed=None):
    size = random.randint(4, 10)
    J = make_jordan_matrix(size,-2,2)
    P = random_unimodular_matrix(J.rows, steps=max(10, 3 * J.rows), seed=seed)
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



def demo(output_file="pro.txt"):
    A_gen, J_src, P_src = generate_test_matrix(seed=42)
    case=(
        A_gen,
        "Сгенерированный тест A = P J P^(-1) для целой матрицы"
    )

    with open(output_file, "w", encoding="utf-8") as f:
            write_case_result(f, case[0], case[1], J_src, P_src)


if __name__ == "__main__":
    demo("prod.txt")
