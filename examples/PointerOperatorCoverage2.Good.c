
struct a
{
    int i;
};

struct b
{
    struct a * a;
};


int main(){
    
    struct a * pa;
    struct b * pb;
    int i;
    
    i = malloc(sizeof(struct a));
    i = 100;
    pa = malloc(sizeof(struct a));
    pb = malloc(sizeof(struct b));

    !malloc(sizeof(struct b));
    -malloc(sizeof(struct b));

    malloc(sizeof(struct a)) + pb;
    malloc(sizeof(struct a)) - pb;
    malloc(sizeof(struct a)) * pb;
    malloc(sizeof(struct a)) / pb;
    malloc(sizeof(struct a)) < pb;
    malloc(sizeof(struct a)) <= pb;
    malloc(sizeof(struct a)) > pb;
    malloc(sizeof(struct a)) >= pb;
    malloc(sizeof(struct a)) && pb;
    malloc(sizeof(struct a)) || pb;

    i + malloc(sizeof(struct a));
    i - malloc(sizeof(struct a));
    i * malloc(sizeof(struct a));
    i / malloc(sizeof(struct a));
    i < malloc(sizeof(struct a));
    i <= malloc(sizeof(struct a));
    i > malloc(sizeof(struct a));
    i >= malloc(sizeof(struct a));
    i && malloc(sizeof(struct a));
    i || malloc(sizeof(struct a));

    malloc(sizeof(struct a)) + i;
    malloc(sizeof(struct a)) - i;
    malloc(sizeof(struct a)) * i;
    malloc(sizeof(struct a)) / i;
    malloc(sizeof(struct a)) < i;
    malloc(sizeof(struct a)) <= i;
    malloc(sizeof(struct a)) > i;
    malloc(sizeof(struct a)) >= i;
    malloc(sizeof(struct a)) && i;
    malloc(sizeof(struct a)) || i;

    malloc(sizeof(struct b)) + malloc(sizeof(struct a));
    malloc(sizeof(struct b)) - malloc(sizeof(struct a));
    malloc(sizeof(struct b)) * malloc(sizeof(struct a));
    malloc(sizeof(struct b)) / malloc(sizeof(struct a));
    malloc(sizeof(struct b)) < malloc(sizeof(struct a));
    malloc(sizeof(struct b)) <= malloc(sizeof(struct a));
    malloc(sizeof(struct b)) > malloc(sizeof(struct a));
    malloc(sizeof(struct b)) >= malloc(sizeof(struct a));
    malloc(sizeof(struct b)) && malloc(sizeof(struct a));
    malloc(sizeof(struct b)) || malloc(sizeof(struct a));

    pb + malloc(sizeof(struct a));
    pb - malloc(sizeof(struct a));
    pb * malloc(sizeof(struct a));
    pb / malloc(sizeof(struct a));
    pb < malloc(sizeof(struct a));
    pb <= malloc(sizeof(struct a));
    pb > malloc(sizeof(struct a));
    pb >= malloc(sizeof(struct a));
    pb && malloc(sizeof(struct a));
    pb || malloc(sizeof(struct a));

    return 0;
}

/*
examples/PointerOperatorCoverage2.c:19:7: warning: incompatible pointer to integer conversion assigning to 'int' from 'void *'
examples/TDS5.Good2.c:25:5: error: invalid argument type 'void *' to unary expression
examples/TDS5.Good2.c:27:30: error: invalid operands to binary expression ('void *' and 'struct b *')
examples/TDS5.Good2.c:28:30: error: 'void *' and 'struct b *' are not pointers to compatible types
examples/TDS5.Good2.c:29:30: error: invalid operands to binary expression ('void *' and 'struct b *')
examples/TDS5.Good2.c:30:30: error: invalid operands to binary expression ('void *' and 'struct b *')
examples/TDS5.Good2.c:31:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:32:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:33:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:34:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:39:7: error: invalid operands to binary expression ('int' and 'void *')
examples/TDS5.Good2.c:40:7: error: invalid operands to binary expression ('int' and 'void *')
examples/TDS5.Good2.c:41:7: error: invalid operands to binary expression ('int' and 'void *')
examples/TDS5.Good2.c:42:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:43:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:44:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:45:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:51:30: error: invalid operands to binary expression ('void *' and 'int')
examples/TDS5.Good2.c:52:30: error: invalid operands to binary expression ('void *' and 'int')
examples/TDS5.Good2.c:53:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:54:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:55:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:56:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:60:30: error: invalid operands to binary expression ('void *' and 'void *')
examples/TDS5.Good2.c:62:30: error: invalid operands to binary expression ('void *' and 'void *')
examples/TDS5.Good2.c:63:30: error: invalid operands to binary expression ('void *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:71:8: error: invalid operands to binary expression ('struct b *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:72:8: error: 'struct b *' and 'void *' are not pointers to compatible types
examples/PointerOperatorCoverage2.Good.c:73:8: error: invalid operands to binary expression ('struct b *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:74:8: error: invalid operands to binary expression ('struct b *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:75:8: warning: comparison of distinct pointer types ('struct b *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:76:8: warning: comparison of distinct pointer types ('struct b *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:77:8: warning: comparison of distinct pointer types ('struct b *' and 'void *')
examples/PointerOperatorCoverage2.Good.c:78:8: warning: comparison of distinct pointer types ('struct b *' and 'void *')

17 erreurs et 17 warnings
*/