struct test {
  int a;
  struct test * b;
};

struct test2{
    int a;
    int b;
    int c;
    struct test *d ;
};

struct test2 * func(int d, struct test * c){

    struct test2 * a ;
    struct test * b ;  
    a = malloc(sizeof(struct test2));
    b = malloc(sizeof(struct test));
    a -> a = d;
    a -> d = b;
    b -> a = d + 1;
    b -> b = c;
    return a;
}

int main(){
    struct test * a ;
    struct test * b ;
    struct test2 * c ;
    a = malloc(sizeof(struct test));

    if(a) {
        a -> b = malloc(sizeof(struct test));
    }
    {
        {
            a -> a = 11;
        }
    }
    print(a->a);
    c = func(4+2, a);
    print(c->a);
    print(c->d->b->a);
    return 0;
}