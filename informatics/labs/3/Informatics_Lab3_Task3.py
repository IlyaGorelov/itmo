# Author = Gorelov Ilya Aleksandrovich
# Group = P3131
# Date = 05.10.2025
# Variant = 0
import re

#s = input().strip()

minuteNumber = r'([1-5][0-9]|[0-9])'
minutes = rf'({minuteNumber}(,{minuteNumber})*|\*|{minuteNumber}-{minuteNumber})'
minutes = rf'{minutes}(/{minuteNumber})?'

hourNumber = r'(1[0-9]|2[0-3]|[0-9])'
houres = rf'({hourNumber}(,{hourNumber})*|\*|{hourNumber}-{hourNumber})|'
houres = rf'{houres}(/{hourNumber})?'

dayNumber = r'([1-2][0-9]|3[0-1]|[1-9])'
days = rf'({dayNumber}(,{dayNumber})*|\*|{dayNumber}-{dayNumber})|'
days = rf'{days}(/{dayNumber})?'

monthNumber = r'(1[0-2]|[1-9])'
months = rf'({monthNumber}(,{monthNumber})*|\*|{monthNumber}-{monthNumber})|'
months = rf'{months}(/{monthNumber})?'

#() {5,} [] 
weekdayNumber = r'([0-6])'
weekdays = rf'({weekdayNumber}(,{weekdayNumber})*|\*|{weekdayNumber}-{weekdayNumber})|'
weekdays = rf'{weekdays}(/{weekdayNumber})?'

reg = rf'({minutes}) ({houres}) ({days}) ({months}) ({weekdays})'

tests = ['0/15 0 * * *',
         # +
         '0-5 13 * * 5',
         # +
         '* * * * 9',
         # -
         '0 0 10/5 * *',
         # +
         '4/7 * 1 * 3-6'
         # +
         ]

s = input()
if re.fullmatch(reg,s):
    print("+")
else:
    print('-')