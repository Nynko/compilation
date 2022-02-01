
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

    return 0;
}

/*
examples/PointerOperatorCoverage2.c:19:7: warning: incompatible pointer to integer conversion assigning to 'int' from 'void *'
examples/TDS5.Good2.c:24:5: error: invalid argument type 'void *' to unary expression
examples/TDS5.Good2.c:23:30: error: invalid operands to binary expression ('void *' and 'struct b *')
examples/TDS5.Good2.c:24:30: error: 'void *' and 'struct b *' are not pointers to compatible types
examples/TDS5.Good2.c:25:30: error: invalid operands to binary expression ('void *' and 'struct b *')
examples/TDS5.Good2.c:26:30: error: invalid operands to binary expression ('void *' and 'struct b *')
examples/TDS5.Good2.c:27:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:28:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:29:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:30:30: warning: comparison of distinct pointer types ('void *' and 'struct b *')
examples/TDS5.Good2.c:35:7: error: invalid operands to binary expression ('int' and 'void *')
examples/TDS5.Good2.c:36:7: error: invalid operands to binary expression ('int' and 'void *')
examples/TDS5.Good2.c:37:7: error: invalid operands to binary expression ('int' and 'void *')
examples/TDS5.Good2.c:38:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:39:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:40:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:41:7: warning: ordered comparison between pointer and integer ('int' and 'void *')
examples/TDS5.Good2.c:47:30: error: invalid operands to binary expression ('void *' and 'int')
examples/TDS5.Good2.c:48:30: error: invalid operands to binary expression ('void *' and 'int')
examples/TDS5.Good2.c:49:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:50:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:51:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:52:30: warning: ordered comparison between pointer and integer ('void *' and 'int')
examples/TDS5.Good2.c:56:30: error: invalid operands to binary expression ('void *' and 'void *')
examples/TDS5.Good2.c:58:30: error: invalid operands to binary expression ('void *' and 'void *')
examples/TDS5.Good2.c:59:30: error: invalid operands to binary expression ('void *' and 'void *')

13 erreurs et 13 warnings
*/