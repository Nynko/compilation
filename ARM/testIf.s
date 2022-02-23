
.global _start             // Provide program starting address to linker
.align 2



_start:
        mov X1, #1

;if1
        CMP X1, #0
        BEQ _finif1
        ADD X1, X1, #1
_finif1:
	B _end




_end:
        mov     X0, #0      // Use 0 return code
        mov     X16, #1     // Service command code 1 terminates this program
        svc     0           // Call MacOS to terminate the program
