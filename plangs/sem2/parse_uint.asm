section .text

; getsymbol is a routine to
; read a symbol (e.g. from stdin)
; into al
getsymbol:
    xor rax, rax
    xor rdi, rdi
    dec rsp
    mov rsi, rsp
    mov rdx, 1
    syscall
    test rax, rax
    jz .EOF
    mov al, [rsp]
    jmp .END
    .EOF:
    xor rax, rax
    .END:
    inc rsp
    ret

; rdi - string pointer
; rsi - num chars
print_string:
    mov  rdx, rsi
    mov  rsi, rdi
    mov  rax, 1
    mov  rdi, 1
    syscall
    ret

; exit with 0 code
exit:
    mov  rax, 60
    xor  rdi, rdi
    syscall

global _start
_start:
    xor rdx, rdx
    xor rax, rax

    _A:
    call getsymbol

    cmp al, '0'
    jb _E
    cmp al, '9'
    ja _E

    sub   al, '0'
    movzx r8, al
    imul  rax, 10
    add   rax, r8
    inc   rdx
    
    _B:
    call getsymbol
    test al, al
    jz _D
    cmp al, 10
    jz _D
    cmp al, '0'
    jb _E
    cmp al, '9'
    ja _E

    sub   al, '0'
    movzx r8, al
    imul  rax, 10
    add   rax, r8
    inc   rdx

    jmp _B
    
    _D:
    call exit
    
    _E:
    xor edx, edx
    call exit