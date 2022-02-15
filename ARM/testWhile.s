
.global _start             // Provide program starting address to linker
.align 2



_start:
        mov X1, #4
        B _while1


_while1:
        CMP X1, #0
        BEQ _finWhile1
        SUB X1, X1, #1
        B _while1

_finWhile1:
	B _end



// Setup the parameters to exit the program
// and then call Linux to do it.

_end:
        mov     X0, #0      // Use 0 return code
        mov     X16, #1     // Service command code 1 terminates this program
        svc     0           // Call MacOS to terminate the program
