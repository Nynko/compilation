struct b
{
    int c;
};

struct a
{
    int b;
    struct b * bb;
};

struct a * f(){
    struct a * b;
    b = malloc(sizeof(struct a));
    return b;
}

int main(){
    int ma;
    struct a *aa;
    aa = malloc(sizeof(struct a));
    aa->b = 2;
    aa->bb->c = 1;
    f()->b = 1;
    {ma = 1;}
    return ma;
}