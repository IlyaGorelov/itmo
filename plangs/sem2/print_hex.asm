section .data
codes:
    db      '0123456789ABCDEF'

new_line:
    db      10

section .text
global _start

print_number:
    mov rdx, 1
    mov rax, 1
    mov  rcx, 64
    .loop:
        push rdi
        sub  rcx, 4
        ; cl is a register, smallest part of rcx

        sar  rdi, cl
        and  rdi, 0xf

        lea  rsi, [codes + rdi]

        mov  rdi, rax
        ; syscall leaves rcx and r11 changed
        push rcx
        syscall
        pop  rcx

        pop rdi
        ; test can be used for the fastest 'is it a zero?' check
        ; see docs for 'test' command
        test rcx, rcx
        jnz .loop
    ret

print_new_line:
    mov rdx, 1
    mov rax, 1
    mov  rdi, rax
    mov  rsi, new_line
    
    syscall
    ret

function:
    push rbp
    mov rbp, rsp
    sub  rsp, 32

    mov qword [rbp-8], 0xaa
    mov qword [rbp-16],0xbb
    mov qword [rbp-24], 0xcc
    mov qword [rbp-32], 0xff

    mov rdi, qword [rbp-8]
    call print_number
    call print_new_line

    mov rdi, qword [rbp-16]
    call print_number
    call print_new_line

    mov rdi, qword [rbp-24]
    call print_number
    call print_new_line

    mov rdi, qword [rbp-32]
    call print_number
    call print_new_line

    mov rsp, rbp
    pop rbp
    ret

_start:
    call function

    mov  rax, 60            ; invoke 'exit' system call
    xor  rdi, rdi
    syscall
