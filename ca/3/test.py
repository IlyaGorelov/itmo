count = 0
n = 346
if n == 0:
    print(32)
for i in range(31, -1, -1):
        if (n >> i) & 1 == 0:
            count += 1
        else:
            break
print(count)