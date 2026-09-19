    .data
input_addr:      .word  0x80
output_addr:     .word  0x84
stack_top:       .word  0x1000

max_number_for_square: .word  0x0000B504
error:           .word  0xCCCCCCCC

    .text
    .org     0x100
_start:
    lui      sp, %hi(stack_top)
    addi     sp, sp, %lo(stack_top)
    lw       sp, 0(sp)

    lui      t0, %hi(input_addr)
    addi     t0, t0, %lo(input_addr)
    lw       t0, 0(t0)
    lw       a0, 0(t0)

    jal      ra, sum_odd_n

    lui      t0, %hi(output_addr)
    addi     t0, t0, %lo(output_addr)
    lw       t0, 0(t0)
    sw       a0, 0(t0)

    halt

    ; sum of odds up to number = ( (n+1)/2 )^2
sum_odd_n:
    addi     sp, sp, -8
    sw       ra, 0(sp)
    sw       a0, 4(sp)

    slti     t0, a0, 1
    bnez     t0, sum_odd_n_invalid

    addi     a0, a0, 1
    addi     t0, zero, 2
    div      a0, a0, t0

    jal      ra, square

    j        sum_odd_n_return

sum_odd_n_invalid:
    addi     a0, zero, -1

sum_odd_n_return:
    lw       ra, 0(sp)
    addi     sp, sp, 8
    jr       ra

square:
    addi     sp, sp, -4
    sw       ra, 0(sp)

    lui      t1, %hi(max_number_for_square)
    addi     t1, t1, %lo(max_number_for_square)
    lw       t0, 0(t1)

    bgt      a0, t0, square_overflow

    mul      a0, a0, a0

    j        square_return

square_overflow:
    lui      t0, %hi(error)
    addi     t0, t0, %lo(error)
    lw       a0, 0(t0)

square_return:
    lw       ra, 0(sp)
    addi     sp, sp, 4
    jr       ra