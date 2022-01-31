struct a
{
};

int functionTest()
{
    int c;
    c = 0;
    c = c + 1;
    return c;
}

int functionTest2()
{
    
    int c;
    c = 0;
    c = c + 1;
    {
        return 0;
    }
    return c;
}

int functionTest3()
{
    int c;
    c = 0;
    c = c + 1;
    {
        return 0;
    }
}

int functionIfErreur()
{
    int az;
    if (az)
    {
        return 0;
    }
}

int functionIfElse()
{
    int ae;
    if (ae)
    {
        return 0;
    }
    else
    {
        return 1;
    }
}

struct a *functionTestS()
{
    int c;
    struct a *b;
    b = malloc(sizeof(struct a));
    c = 0;
    c = c + 1;
    return b;
}

struct a *functionIfErreurS()
{
    int az;
    struct a *b;
    b = malloc(sizeof(struct a));
    if (az)
    {
        return b;
    }
}

struct a *functionIfElseS()
{
    int ae;
    struct a *b;
    b = malloc(sizeof(struct a));
    if (ae)
    {
        return b;
    }
    else
    {
        
    }
}

int main()
{
    int a;
    if (0)
    {
        return 0;
    }
    else
    {
        return 1;
    }
    
    if (1)
    {
        int a;
        a = 0;
        a = a + 1;
        /* code */
    }
    if (0){
        int a;
    }
    a = 0;
    
}
