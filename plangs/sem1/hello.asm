; hello.asm 
  section .data
  message1: db  'msg for stdout', 10
  message2: db  'msg for stderr', 10

  section .text
  global _start

  _start:
      mov     rax, 1           ; 'write' syscall number
      mov     rdi, 1           ; stdout descriptor
      mov     rsi, message1     ; string address
      mov     rdx, 15          ; string length in bytes
      syscall


      mov     rax, 1           ; 'write' syscall number
      mov     rdi, 2           ; stdout descriptor
      mov     rsi, message2     ; string address
      mov     rdx, 15          ; string length in bytes
      syscall

      mov     rax, 60          ; 'exit' syscall number
      xor     rdi, rdi
      syscall