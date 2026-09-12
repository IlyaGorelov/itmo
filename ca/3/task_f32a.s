    .data
input_addr:      .word  0x80
output_addr:     .word  0x84
number:          .word  0
const_32:        .word  32
count_zeros:     .word  0
const_one:       .word  1
R:               .word  30

    .text
    .org 0x200
_start:
_get_number:
    @p input_addr a! @       \ stack.push(input number)
    !p number @p number      \ number = stack.pop() | stack.push(number)
    if _zero_halt            
    @p number                \ stack.push(number)

    @p R                     \ stack.push(R)
    >r                       \ return_stack.push(stack.pop())
for:
    r> dup                   \ stack.push(return_stack.pop()).duplicate()
    >r >r                    \ return_stack.push(stack.pop()) x2
shift_R_times:
    2/
    next shift_R_times

    if _zero

    _const_one ;
_zero:
    @p count_zeros @p const_one + !p count_zeros \ count_zeros++
_const_one:
    @p number                \ restore number | stack.push(number)
    next for

    @p count_zeros @p output_addr a! ! \ sout(count_zeros)
    _halt

_zero_halt:
    @p const_32 @p output_addr a! ! \ sout(32)
_halt:
    halt
