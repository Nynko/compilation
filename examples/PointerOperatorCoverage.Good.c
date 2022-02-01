
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
    
    pa = malloc(sizeof(struct a));
    pb = i;
    i = pa;
    pb = malloc(sizeof(struct b));

    i = 100;

    !pa;
    -pa;

    pa + pb;
    pa - pb;
    pa * pb;
    pa / pb;
    pa < pb;
    pa <= pb;
    pa > pb;
    pa >= pb;
    pa && pb;
    pa || pb;

    pa + pa;
    pa - pa;
    pa * pa;
    pa / pa;
    pa < pa;
    pa <= pa;
    pa > pa;
    pa >= pa;
    pa && pa;
    pa || pa;

    pa + i;
    pa - i;
    pa * i;
    pa / i;
    pa < i;
    pa <= i;
    pa > i;
    pa >= i;
    pa && i;
    pa || i;

    i + pb;
    i - pb;
    i * pb;
    i / pb;
    i < pb;
    i <= pb;
    i > pb;
    i >= pb;
    i && pb;
    i || pb;

    return 0;
}

/*
examples/PointerOperatorCoverage.Good.c:20:8: warning: incompatible integer to pointer conversion assigning to 'struct b *' from 'int'
examples/PointerOperatorCoverage.Good.c:21:7: warning: incompatible pointer to integer conversion assigning to 'int' from 'struct a *'
examples/TDS5.Good.c:24:5: error: invalid argument type 'struct a *' to unary expression
examples/TDS5.Good.c:26:8: error: invalid operands to binary expression ('struct a *' and 'struct b *')
examples/TDS5.Good.c:27:8: error: 'struct a *' and 'struct b *' are not pointers to compatible types
examples/TDS5.Good.c:28:8: error: invalid operands to binary expression ('struct a *' and 'struct b *')
examples/TDS5.Good.c:29:8: error: invalid operands to binary expression ('struct a *' and 'struct b *')
examples/TDS5.Good.c:30:8: warning: comparison of distinct pointer types ('struct a *' and 'struct b *')
examples/TDS5.Good.c:31:8: warning: comparison of distinct pointer types ('struct a *' and 'struct b *')
examples/TDS5.Good.c:32:8: warning: comparison of distinct pointer types ('struct a *' and 'struct b *')
examples/TDS5.Good.c:33:8: warning: comparison of distinct pointer types ('struct a *' and 'struct b *')
examples/TDS5.Good.c:37:8: error: invalid operands to binary expression ('struct a *' and 'struct a *')
examples/TDS5.Good.c:39:8: error: invalid operands to binary expression ('struct a *' and 'struct a *')
examples/TDS5.Good.c:40:8: error: invalid operands to binary expression ('struct a *' and 'struct a *')
examples/TDS5.Good.c:50:8: error: invalid operands to binary expression ('struct a *' and 'int')
examples/TDS5.Good.c:51:8: error: invalid operands to binary expression ('struct a *' and 'int')
examples/TDS5.Good.c:52:8: warning: ordered comparison between pointer and integer ('struct a *' and 'int')
examples/TDS5.Good.c:53:8: warning: ordered comparison between pointer and integer ('struct a *' and 'int')
examples/TDS5.Good.c:54:8: warning: ordered comparison between pointer and integer ('struct a *' and 'int')
examples/TDS5.Good.c:55:8: warning: ordered comparison between pointer and integer ('struct a *' and 'int')
examples/TDS5.Good.c:60:7: error: invalid operands to binary expression ('int' and 'struct b *')
examples/TDS5.Good.c:61:7: error: invalid operands to binary expression ('int' and 'struct b *')
examples/TDS5.Good.c:62:7: error: invalid operands to binary expression ('int' and 'struct b *')
examples/TDS5.Good.c:63:7: warning: ordered comparison between pointer and integer ('int' and 'struct b *')
examples/TDS5.Good.c:64:7: warning: ordered comparison between pointer and integer ('int' and 'struct b *')
examples/TDS5.Good.c:65:7: warning: ordered comparison between pointer and integer ('int' and 'struct b *')
examples/TDS5.Good.c:66:7: warning: ordered comparison between pointer and integer ('int' and 'struct b *')

13 erreurs 14 warnings

a voir si l'on rajoute ces warnings
examples/TDS5.Good.c:41:8: warning: self-comparison always evaluates to false [-Wtautological-compare]
examples/TDS5.Good.c:42:8: warning: self-comparison always evaluates to true [-Wtautological-compare]
examples/TDS5.Good.c:43:8: warning: self-comparison always evaluates to false [-Wtautological-compare]
examples/TDS5.Good.c:44:8: warning: self-comparison always evaluates to true [-Wtautological-compare]
*/