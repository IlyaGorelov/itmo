    .data
length:          .byte  0x0                ; byte len = 0
buffer:          .byte  '___________________________________' ; byte[] buffer = '_____'
pointer:         .word  0x1                ; int pointer = 1
length_int:      .word  0                  ; int length_int = 1
capacity:        .word  0x20               ; const int capacity 32
cur_char:        .word  '_'                ; int cur_char = '_'
input_addr:      .word  0x80
output_addr:     .word  0x84
one:             .word  1                  ; const int one = 1
zero:            .word  0                  ; const int zero = 0
four:            .word  4                  ; const int 4
six:             .word  6                  ; const int 6
difference:      .word  0x20               ; const int difference = 0x20 - keeps difference of chars codes btw small and capital letters
next_symbol_is_first_flag: .word  1        ; const int next_symbol_is_first_flag = 1
start_of_small_letters: .word  0x61        ; const int 0x61 - keeps the start of small letters diapason in ascii
end_of_small_letters: .word  0x7b          ; const int 0x7b - keeps the end of small letters diapason in ascii + 1
start_of_capital_letters: .word  0x41      ; const int 0x41 - keeps the start of captial letters diapason in ascii
end_of_captial_letters: .word  0x5b        ; const int 0x5b - keeps the end of capital letters diapason in ascii + 1
space_ascii:     .word  0x20               ; const int space_ascii = 0x20
lf_ascii:        .word  10                 ; const int lf_ascii = 10 - keeps the code of line feed (\n)
add_5f:          .word  0x5f5f5f00         ; const int add_5f - adds underscores to the major bytes of a char
error:           .word  0xCCCCCCCC         ; const int error
shift_number:    .word  24                 ; const int shift = 24 - shift to get only a needed char
shift_to_remove_junior_bytes: .word  8

    .text
    .org         200                         ; starting here because otherwise programm code could, probably, override some important IO things
_start:

_receive_char:
    load         pointer                     ; AC = pointer  (pointer starts from 1, not from 0)
    sub          capacity                    ; if AC > capacity
    bgtz         _error_break                ; then _error_break
    load         input_addr                  ; AC = input_addr
    load_acc                                 ; AC = new char
    store        cur_char                    ; cur_char = AC
    sub          lf_ascii                    ; if AC == '\n' (AC -= lf_ascii)
    beqz         _proceed                    ; then halt
    load         cur_char                    ; AC = cur_char
    store_ind    pointer                     ; buffer[pointer] = cur_char
    load         pointer                     ; AC = pointer
    add          one                         ; AC += 1
    store        pointer                     ; pointer = AC
    jmp          _receive_char

_proceed:

_store_length:
    load         length                      ; AC = length (like 0xCC_CC_CC_00 where CC is a char code)
    add          pointer                     ; AC += pointer
    sub          one                         ; AC -= 1
    store        length                      ; length = AC

    load         pointer                     ; AC = pointer
    sub          one                         ; AC -= 1
    store        length_int                  ; length_int = AC
_reset_pointer:
    load         one                         ; AC = 1
    store        pointer                     ; pointer = 1

_for:
    load         pointer                        ; AC = pointer
    sub          length_int                     ; if  pointer > length
    bgtz         _halt                          ; then _halt
    load         pointer                        ; AC = pointer
    load_acc                                    ; AC = buffer[pointer]
    shiftl       shift_number
    shiftr       shift_number                   ; AC = char
    store        cur_char                       ; cur_char = AC
    sub          space_ascii                    ; if AC == ' ' (AC -= space_ascii)
    beqz         _set_next_symbol_is_first_flag ; then next_symbol_is_first_flag = 1
    load         next_symbol_is_first_flag      ; AC = next_symbol_is_first_flag
    sub          one                            ; if AC == false
    bltz         _make_lowercase                ; then _make_lowercase
    jmp          _make_uppercase                ; else _make_uppercase

_continue_for:
    load         cur_char
    load         pointer                     ; AC = pointer
    load_acc                                 ; AC = buffer[pointer] - getting a 'dirty' value containing other chars
    shiftr       shift_to_remove_junior_bytes
    shiftl       shift_to_remove_junior_bytes
    add          cur_char                    ; AC = cur_char
    store_ind    pointer                     ; buffer[pointer] = AC
    load         pointer                     ; AC = pointer
    add          one                         ; AC += 1
    store        pointer                     ; pointer = AC
    jmp          _for                        ; looping

_make_uppercase:
    load         zero                        ; AC = 0
    store        next_symbol_is_first_flag   ; next_symbol_is_first_flag = AC
    load         cur_char                    ; AC = cur_char
    sub          start_of_small_letters      ; if AC < start_of_small_letters
    bltz         _print                      ; then print(cur_char)
    load         cur_char                    ; AC = cur_char
    sub          end_of_small_letters        ; if AC >= end_of_small_letters
    bgez         _print                      ; then print (cur_char)
    load         cur_char                    ; else AC = cur_char
    sub          difference                  ; AC += difference
    store        cur_char                    ; cur_char = AC
    jmp          _print                      ; print (cur_char)

_make_lowercase:
    load         zero                        ; AC = 0
    store        next_symbol_is_first_flag   ; next_symbol_is_first_flag = 0
    load         cur_char                    ; AC = cur_char
    sub          start_of_capital_letters    ; if AC < start_of_capital_letters
    bltz         _print                      ; then print(cur_char)
    load         cur_char                    ; else AC = cur_char
    sub          end_of_captial_letters      ; if AC >= end_of_captial_letters
    bgez         _print                      ; then print(cur_char)
    load         cur_char                    ; else AC = cur_char
    add          difference                  ; AC += difference (0x20)
    store        cur_char                    ; cur_char = AC
    jmp          _print

_print:
    load         cur_char                    ; AC = cur_char
    store_ind    output_addr                 ; print(AC)
_loop:
    jmp          _continue_for

_set_next_symbol_is_first_flag:
    load         one
    store        next_symbol_is_first_flag
    jmp          _print


_halt:
    load         pointer                     ; AC = pointer
    sub          one                         ; AC -= 1
    store        pointer                     ; pointer = AC
    load_acc                                 ; AC = buffer[pointer]
    add          add_5f                      ; adds underscores after symbol to suit for the task
    store_ind    pointer                     ; buffer[pointer] = AC
    halt

_error_break:
    load         error
    store_ind    output_addr
    halt