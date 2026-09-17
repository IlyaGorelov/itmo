    .data
format_string_buffer: .byte  '________________________________'
numbers:         .word  '00000000000000000000000000000000'
number_for_output: .byte  10, '___________'

input_addr:      .word  0x80
output_addr:     .word  0x84
stack_top:       .word  0x1000

    .text
    .org     0x200
_start:
    movea.l  stack_top, A7
    movea.l  (A7), A7
    movea.l  input_addr, A0
    movea.l  (A0), A0
    movea.l  output_addr, A1
    movea.l  (A1), A1

    move.l   0x20, -(A7)
    jsr      read_format_string
    move.l   (A7)+, D1

    jsr      read_digits

    jsr      output

    halt

error:
    move.l   0xffffffff, (A1)
    halt

    ;==============Procedures============

    ; Params: (long) capacity
read_format_string:
    link     A6, -3

    move.b   0, -3(A6)                       ; is_digit
    move.b   0, -2(A6)                       ; length
    move.b   0, -1(A6)                       ; is_specifier_being_read

    move.b   0, D0                           ; current char
    move.b   0, D4                           ; is_digit
    move.l   0, D5                           ; count_of_specifiers

    movea.l  format_string_buffer, A2

    reading_loop:
        add.b    1, -2(A6)

        move.b   (A0), D0

        cmp.b    10, D0
        beq      return_from_read_format

        cmp.b    0x25, D0
        bne      check_padding
        move.b   1, -1(A6)
        jmp      store_char

        check_padding:
            cmp.b    0, -1(A6)
            beq      store_char
            cmp.b    '-' , D0
            bne      check_for_digits
            jmp      store_char

        check_for_digits:
            jsr      is_digit
            cmp.b    0, D4
            beq      check_for_d
            jmp      store_char

        check_for_d:
            cmp.b    'd' , D0
            bne      reset_flag
            add.b    1, D5
            jmp      reset_flag

        reset_flag:
            move.b   0, -1(A6)

        store_char:
            move.b   D0, (A2)+

            cmp.b    -2(A6), 8(A6)
            blt      error

            jmp      reading_loop

    return_from_read_format:
        move.b   D0, (A2)+
        unlk     A6
        rts

    ; Expect: count_of_specifiers in D5
read_digits:
    link     A6, -12

    move.b   0, -12(A6)                      ; is_negative;
    move.b   1, -10(A6)                      ; is_null
    move.l   0, -8(A6)                       ; current_number
    move.l   0, -4(A6)                       ; received_count

    movea.l  numbers, A3

    move.b   0, D0                           ; current_digit

    read_digits_loop:
        cmp.l    -4(A6), D5
        beq      return_from_read_digits

        move.b   (A0), D0

        cmp.b    10, D0
        beq      new_digit

        cmp.b    '-' , D0
        beq      set_negative

        jsr      is_digit
        cmp.b    0, D4
        beq      error

        move.b   0, -10(A6)

        sub.b    0x30, D0
        mul.l    10, -8(A6)
        bvs      error
        cmp.b    1,-12(A6)
        beq      subtract_digit
        add.l    D0, -8(A6)
        jmp      check_error

        subtract_digit:
            sub.l    D0, -8(A6)

        check_error:
            bvs      error
            jmp      read_digits_loop

        new_digit:
            cmp.b    1,-10(A6)
            beq      error
            jmp      save_in_buffer

        save_in_buffer:
            move.b   0, -12(A6)
            move.b   1, -10(A6)
            add.l    1, -4(A6)

            move.l   -8(A6), (A3)+
            move.l   0, -8(A6)

            jmp      read_digits_loop

        set_negative:
            cmp.b    1,-12(A6)
            beq      error
            move.b   1, -12(A6)
            jmp      read_digits_loop

    return_from_read_digits:
        unlk     A6
        rts

output:
    link     A6, -13

    move.b   0, D0                           ; current_char
    move.b   0, -13(A6)                      ; is_specifier_parsed
    move.b   0, -12(A6)                      ; is_left_aligned
    move.l   0, -10(A6)                      ; padding_number
    move.l   0, -6(A6)                       ; is_current_number_negative
    move.l   0, -4(A6)                       ; number_duplicate

    movea.l  format_string_buffer, A2
    movea.l  numbers, A3
    movea.l  number_for_output, A4
    move.b   (A4)+, D0

    output_loop:
        move.b   (A2)+, D0
        cmp.b    10, D0
        beq      return_from_output

        check_for_specifier:
            cmp.b    '%' , D0
            bne      print_char

            jsr      parse_specifiers
            cmp.b    1,-13(A6)
            beq      continue

        print_char:
            move.l   D0, (A1)
        continue:
            movea.l  number_for_output, A4
            move.b   (A4)+, D0
            jmp      output_loop

    return_from_output:
        unlk     A6
        rts

parse_specifiers:
    movea.l  A2, A5

    parse_specifiers_loop:
        move.b   (A5)+, D0

        check_padding_in_output:
            cmp.b    '-' , D0
            bne      check_for_digits_in_output
            move.b   1, -12(A6)
            jmp      parse_specifiers_loop

        check_for_digits_in_output:
            jsr      is_digit
            cmp.b    0, D4
            beq      check_for_d_in_output
            sub.b    0x30, D0
            mul.l    10, -10(A6)
            add.l    D0, -10(A6)
            jmp      parse_specifiers_loop

        check_for_d_in_output:
            cmp.b    'd' , D0
            bne      restore_char                    ; if after '%' goes not -/digit/d when it's not a specifier
            jmp      proceed_number

    restore_char:
        move.b   -(A2), D0
        move.b   (A2)+, D7
        move.b   0, -13(A6)
        jmp      return_from_parse_specifiers

    proceed_number:
        jsr      format_number
        move.b   1, -13(A6)

    return_from_parse_specifiers:
        rts

format_number:
    move.l   (A3)+, D0
    bpl      positive
    move.b   1, -6(A6)
    jsr      decrease_padding

    cmp.l    0x80000000, D0
    bne      negate

    jsr      store_min_int

    jmp      print_number_with_padding

    negate:
        neg.l    D0

    positive:
        cmp.l    0, D0
        beq      save_zero

    convert_to_ascii_reverse:
        move.l   D0, -4(A6)
        div.l    10, D0
        mul.l    10, D0
        sub.l    -4(A6), D0
        neg.l    D0
        add.l    0x30, D0
        move.b   D0, (A4)+

        jsr      decrease_padding

        move.l   -4(A6), D0
        div.l    10, D0
        cmp.l    0, D0
        beq      print_number_with_padding
        jmp      convert_to_ascii_reverse

    save_zero:
        move.b   0x30, (A4)+
        jsr      decrease_padding

    print_number_with_padding:
        cmp.l    0, -10(A6)
        bmi      error

        cmp.b    1, -12(A6)
        beq      print_with_left_align

        jsr      print_padding
        jsr      print_number
        jmp      return_from_format_number

    print_with_left_align:
        jsr      print_number
        jsr      print_padding
        jmp      return_from_format_number

    return_from_format_number:
        move.b   0, -12(A6)
        movea.l  A5, A2
        rts

decrease_padding:
    cmp.l    0, -10(A6)
    beq      return_from_decrease_padding
    sub.l    1, -10(A6)

    return_from_decrease_padding:
        rts

store_min_int:
    move.b   '8' , (A4)+
    jsr      decrease_padding
    move.b   '4' , (A4)+
    jsr      decrease_padding
    move.b   '6' , (A4)+
    jsr      decrease_padding
    move.b   '3' , (A4)+
    jsr      decrease_padding
    move.b   '8' , (A4)+
    jsr      decrease_padding
    move.b   '4' , (A4)+
    jsr      decrease_padding
    move.b   '7' , (A4)+
    jsr      decrease_padding
    move.b   '4' , (A4)+
    jsr      decrease_padding
    move.b   '1' , (A4)+
    jsr      decrease_padding
    move.b   '2' , (A4)+
    jsr      decrease_padding
    rts

print_number:
    cmp.b    1, -6(A6)
    bne      print_number_loop
    move.b   '-' , (A1)

    print_number_loop:
        move.b   -(A4), D0
        cmp.b    10 , D0
        beq      return_from_print_number
        move.b   D0, (A1)
        jmp      print_number_loop

    return_from_print_number:
        rts

print_padding:
    print_padding_loop:
        cmp.l    0, -10(A6)
        beq      return_from_print_padding
        sub.l    1, -10(A6)
        move.l   ' ' , (A1)
        jmp      print_padding_loop

    return_from_print_padding:
        rts

is_digit:
    link     A6, -2
    move.b   '0' , -2(A6)
    move.b   '9' , -1(A6)
    cmp.b    -2(A6), D0
    blt      false
    cmp.b    -1(A6), D0
    bgt      false
    move.l   1, D4
    jmp      return_is_digit

    false:
        move.l   0, D4

    return_is_digit:
        unlk     A6
        rts
