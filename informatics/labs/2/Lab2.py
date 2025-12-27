def getAnalyzeResult(number: str):
    r = [number[0],number[1],number[3]]
    r = [int(x) for x in r]
    i = [number[2],number[4],number[5],number[6]]
    i = [int(x) for x in i]

    s1 = r[0]^i[0]^i[1]^i[3]
    s2 = r[1]^i[0]^i[2]^i[3]
    s3 = r[2]^i[1]^i[2]^i[3]

    s = str(s3)+str(s2)+str(s1)

    wrongBit = int(s,2)
    s2 = ''
    for i in range(7):
        if i!=wrongBit-1:
            s2+=number[i]
        else:
            s2+=str(int(not bool(int(number[i]))))
    
    correctMessage = s2[2]+s2[4]+s2[5]+s2[6]
    return [correctMessage,wrongBit]

s = input()

if len(s)==7:
    result = getAnalyzeResult(s)
    if result[1]!=0:
        print(f'Правильное сообщение: {result[0]}   Неправильный бит №{result[1]}')
    else:
        print(f'Правильное сообщение: {result[0]}   Ошибки нет')
else:
    print("Введите число из 7 цифр")