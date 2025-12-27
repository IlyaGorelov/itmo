# Author = Gorelov Ilya Aleksandrovich
# Group = P3131
# Date = 05.10.2025
# Variant = 2
# В примере задания ошибка - не умножают на 5
def replaceNumber(m):
    return str((int(m[0])**3)*5-13)

import re

tests = ['У меня 2 яблока и 3 груши.',
         # У меня 27 яблока и 122 груши.
         '7 * 3 = 21',
         # 1702 * 122 = 46292
         '0 + 1 - 2 = -1',
         # -13 + -8 - 27 = -18
         '12 / 4 = 3',
         # 8627 / 307 = 122
         '(4 + 5) = 45'
         # (307 + 612) = 455612
         ]

# s = input()
#               
# result = re.sub(r'([^\.])-?\b\d+\b([^\.\d])',replaceNumber,s)
# print(re.findall(r'([^\.])-?\b\d+\b([^\.\d])',s))

s = '.d..............q,,,,,,,,,,,,,,,,,'
print(re.findall(r'\..*?,',s))