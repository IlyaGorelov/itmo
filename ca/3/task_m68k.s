    .data
format_string_buffer: .byte  '________________________________' ; byte[] format_string_buffer = '______'
numbers:         .word  '00000000000000000000000000000000' ; byte[] numbers_buffer = '0000000000000'
number_for_output: .byte  'e___________'     ; 11 gaps for number for output

input_addr:      .word  0x80
output_addr:     .word  0x84
stack_top:       .word  0x1000

temp:            .word  0

    .text
    .org     0x200
_start:
    movea.l  stack_top, A7                   ; A7 = stack_top
    movea.l  (A7), A7                        ; A7 = mem[stack_top]
    movea.l  input_addr, A0                  ; A0 = input_addr
    movea.l  (A0), A0                        ; A0 = mem[input_addr]
    movea.l  output_addr, A1                 ; A1 = output_addr
    movea.l  (A1), A1                        ; A1 = mem[output_addr]

    move.b   0, D0                           ; D0 = char = 0
    move.b   0x0, D1                         ; D1 = length = 0
    move.b   0x20, D2                        ; D2 = capacity = 32
    move.b   0, D3                           ; D3 = is_specifier_beeing_read = 0
    move.b   0, D4                           ; D4 = is_it_digit = 0
    move.l   0, D5                           ; D5 = count_of_specifiers = 0

    movea.l  format_string_buffer, A2        ; A2 = pointer = 0 - pointer for buffer_string

read_format_string:
    add.b    1, D1                           ; length += 1

    move.b   (A0), D0                        ; char = new

    cmp.b    10, D0                          ; if char == '\n'
    beq      read_digits                     ; -> read_digits

    cmp.b    0x25, D0                        ; if char != '%'
    bne      check_align                     ; -> check_align
    move.l   1, D3                           ; else is_it_specifier_beeing_read = 1
    jmp      store_char

check_align:
    cmp.b    0, D3                           ; if is_it_specifier_beeing_read == 0
    beq      store_char                      ; -> store_char
    cmp.b    '-' , D0                        ; else if char != '-'
    bne      check_for_digits                ; -> check_for_digits
    jmp      store_char                      ; else -> store_char

check_for_digits:
    jsr      is_digit
    cmp.b    0, D4                           ; if is_digit == 0
    beq      check_for_d                     ; -> check_for_d
    jmp      store_char                      ; else -> store_char

check_for_d:
    cmp.b    'd' , D0                        ; if char != 'd'
    bne      reset_flag                      ; -> reset_flag
    add.b    1, D5                           ; count_of_specifiers++
    jmp      reset_flag

reset_flag:
    move.l   0, D3                           ; is_specifier_beeing_read = 0
store_char:
    move.b   D0, (A2)+                       ; buffer[pointer++] = char

    cmp.b    D1, D2                          ; if capacity<length
    blt      error                           ; -> error

    jmp      read_format_string              ; looping

read_digits:
    move.b   D0, (A2)+                       ; buffer[pointer++] = char ; saving '\n'

    movea.l  numbers, A3                     ; A3 = pointer = 20
    movea.l  temp, A4                        ; A4 = address of the label for the temp value

    move.b   0, D0                           ; D0 = digit = 0
    move.b   1, D1                           ; D1 = is_null = 1
    move.b   0, D2                           ; D2 = number
    move.l   0, D6                           ; D6 = received_count = 0

reading_digits:
    cmp.l    D6, D5                          ; if received_count==count_of_specifiers
    beq      output                          ; -> output

    move.b   (A0), D0                        ; digit = new_digit

    cmp.b    10, D0                          ; if digit == '\n'
    beq      new_digit                       ; -> new_digit

    cmp.b    '-' , D0                        ; if digit == '-'
    beq      set_negative

    jsr      is_digit                        ; call is_digit
    cmp.b    0, D4                           ; if is_digit == 0
    beq      error                           ; -> error

    move.b   0, D1

    sub.b    0x30, D0                        ; D0 = int32.Parse(D0)
    mul.l    10, D2                          ; number *=10
    bvs      error                           ; if V==1 -> error
    cmp.b    1,(A4)                          ; if is_negative
    beq      minus
    add.l    D0, D2                          ; number += digit
    jmp      check_error
minus:
    sub.l    D0, D2                          ; number += digit
    jmp      check_error
check_error:
    bvs      error                           ; if V==1 -> error

    jmp      reading_digits

new_digit:
    cmp.b    1,D1
    beq      error
    jmp      save_in_buffer
negate:
    neg.l    D2
save_in_buffer:
    move.b   1, D1
    move.l   D2, (A3)+                       ; numbers_buffer[pointer++] = number
    add.l    1, D6                           ; received_count++
    move.l   0, D0                           ; number = 0
    move.l   0, D2                           ; digit = 0
    move.l   0, (A4)                         ; restore is_negative
    jmp      reading_digits

set_negative:
    cmp.l    1,(A4)                          ;  if is_negative (if number is already negative -> error)
    beq      error                           ; -> error
    move.l   1, (A4)                         ; is_negative = true
    jmp      reading_digits

    ; =================READING DONE==========================

output:
    move.b   0, D0                           ; D0 = char = 0
    move.b   0, D2                           ; D2 = is_left_aligned = 0
    move.b   0, D3                           ; D3 = shift_number = 0
    move.l   0, D5                           ; D5 = is current number is negative

    movea.l  0, A2                           ; A2 = string_pointer = 0
    movea.l  numbers, A3                     ; A3 = number_pointer = 20
    movea.l  number_for_output, A4           ; A4 = number for output pointer
    move.b   (A4)+, D0                       ; number_for_output pointer ++
    movea.l  temp, A6                        ; A6 = temp value
output_loop:
    move.b   (A2)+, D0                       ; char = format_string_buffer[string_pointer++]
    cmp.b    10, D0                          ; if char == '\n'
    beq      do_halt                         ; -> do_halt

check_for_percent:
    cmp.b    '%' , D0                        ; if char != '%'
    bne      continue                        ; -> continue

handle_specifiers_while:
    movea.l  A2, A5                          ; A5 = temp_char_pointer
while_2:
    move.b   (A5)+, D0                       ; char = format_string_buffer[string_pointer++]
check_align_2:
    cmp.b    '-' , D0                        ; if char != '-'
    bne      check_for_digits_2              ; -> check_for_digits_2
    move.l   1, D2                           ; else is_left_aligned = 1
    jmp      specifier_loop

check_for_digits_2:
    jsr      is_digit
    cmp.b    0, D4                           ; if !is_it_digit
    beq      check_for_d_2                   ; -> check_for_d_2
    sub.b    0x30, D0                        ; else D0 = int32.Parse(D0)
    mul.l    10, D3                          ;  shift_number *= 10

    add.l    D0, D3                          ; shift_number += char
    jmp      specifier_loop

check_for_d_2:
    cmp.b    'd' , D0                        ; if char != 'd'
    bne      output_unhandled                ; -> output_unhandled
    jmp      output_number_with_align

specifier_loop:
    jmp      while_2

output_unhandled:
    jmp      continue

output_number_with_align:
    move.l   (A3)+, D0                       ; number = numbers_buffer[number_pointer++]
    bpl      continue_2                      ; if number > 0
    move.l   1, D5                           ; current number is negative
    jsr      shift_number_dec

    ; Special case for -2^31 (0x80000000 = -2147483648)
    cmp.l    0x80000000, D0
    bne      negate_normal

    move.b   '8' , (A4)+
    move.b   '4' , (A4)+
    move.b   '6' , (A4)+
    move.b   '3' , (A4)+
    move.b   '8' , (A4)+
    move.b   '4' , (A4)+
    move.b   '7' , (A4)+
    move.b   '4' , (A4)+
    move.b   '1' , (A4)+
    move.b   '2' , (A4)+
    jsr      shift_number_dec
    jmp      output_number_with_align_2

negate_normal:
    neg.l    D0

continue_2:
    cmp.l    0, D0                           ; if number == 0
    beq      save_zero                       ; -> save_zero

convert_to_dec_reverse:
    move.l   D0, (A6)                        ; temp = number
    div.l    10, D0                          ; number /=10
    mul.l    10, D0                          ; number *=10
    sub.l    (A6), D0                        ; number -= A6
    neg.l    D0                              ; number *= -1
    add.l    0x30, D0
    move.b   D0, (A4)+
    jsr      shift_number_dec
    move.l   (A6), D0                        ;
    div.l    10, D0
    cmp.l    0, D0                           ; if number == 0
    beq      output_number_with_align_2
    jmp      convert_to_dec_reverse

save_zero:
    move.b   0x30, (A4)+
    jsr      shift_number_dec

output_number_with_align_2:
    cmp.l    0, D3
    bmi      error                           ; if shift number < 0 -> error

    cmp.b    1, D2                           ; if is_left_aligned
    beq      left_align                      ; -> left_align
    jsr      align_loop                      ; else -> align_loop
    cmp.l    1, D5
    bne      continue_3
    move.b   0x2d, (A1)                      ; if negative -> sout('-')
continue_5:
    move.b   -(A4), D0                       ;
    cmp.b    'e' , D0                        ; if D0 == 'e'
    beq      continue_4
    move.b   D0, (A1)                        ; else -> sout('-')
    jmp      continue_5
    move.b   0, D2                           ; is_left_aligned = false
    movea.l  A5, A2
    jmp      next_out

left_align:
    cmp.l    1, D5
    bne      continue_3
    move.b   0x2d, (A1)                      ; if negative -> sout('-')

continue_3:
    move.b   -(A4), D0                       ;
    cmp.b    'e' , D0                        ; if D0 == 'e'
    beq      continue_4
    move.b   D0, (A1)                        ; else -> sout('-')
    jmp      continue_3

continue_4:
    jsr      align_loop
    movea.l  A5, A2
    jmp      next_out

align_loop:
    cmp.b    0, D3                           ; if shift_number == 0
    beq      return_2                        ; -> return_2
    sub.l    1, D3                           ; shift_number--
    move.l   0x20, (A1)                      ; sout( ' ' )
    jmp      align_loop
return_2:
    rts

continue:
    move.l   D0, (A1)                        ; sout( char )
next_out:
    movea.l  number_for_output, A4           ; A4 = number for output pointer
    move.b   (A4)+, D0                       ; number_for_output pointer ++
    jmp      output_loop


do_halt:
    halt

error:
    move.l   0xffffffff, D0
    move.l   D0, (A1)
    halt

    ; D3 = shift_number
shift_number_dec:
    cmp.l    0, D3
    beq      return_3
    sub.l    1, D3
return_3:
    rts

is_digit:
    link     A6, -2
    move.b   0x30, -2(A6)                    ; start of the digits
    move.b   0x39, -1(A6)                    ; end of the digits
    cmp.b    -2(A6), D0                      ; if char < start
    blt      false                           ; -> false
    cmp.b    -1(A6), D0                      ; else if char > end
    bgt      false                           ; -> false
    move.l   1, D4                           ; is_digit = true
    jmp      return
false:
    move.l   0, D4                           ; is_digit = false
return:
    unlk     A6
    rts
