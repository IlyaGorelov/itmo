def getFibonacciLine(numberInFib):
    result = [0,1]
    while len(result)<len(numberInFib)+2:
        result.append(result[-2]+result[-1])
    return result[2:]

def toCC10(numberInFib):
    # Получаем ряд Фибоначчи такой же длины, как и число, без первых 0 и 1
    fibonacciLine = getFibonacciLine(numberInFib)[::-1]
    result = 0
    for i in range(len(numberInFib)):
        result += fibonacciLine[i] if numberInFib[i]=='1' else 0

    return result

n = input()
print(toCC10(n))
