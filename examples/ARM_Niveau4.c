struct test {
  int a;
  struct test * b;
};

struct test * func(int d, struct test * c){

    struct test * a ;
    struct test * b ;  
    a = malloc(sizeof(struct test));
    b = malloc(sizeof(struct test));
    a -> a = d;
    a -> b = b;
    b -> a = d + 1;
    b -> b = c;
    return a;
}

int main(){
    struct test * a ;
    struct test * b ;
    struct test * c;
    int d;
    d = 1;
    a = malloc(sizeof(struct test));
    if(a) {
        a -> b = malloc(sizeof(struct test));
    }
    while(d>=0){
        a -> a = 11;
        d = d -1;
    }
    print(a->a); // 11 
    {
        b = malloc(sizeof(struct test));
        {
            a -> b -> a = 55;
            {
                b -> a = 66;
                {
                    b -> b = malloc(sizeof(struct test));
                    {
                        b -> b -> a = 140;
                    }
                }
            }
        }
    }
    print(a->b->a); // a -> b -> a = 55
    print(b->a); // b -> a = 66
    print(b->b->a); // b -> b -> a = 140

    c = func(1, a);
    print(c->a); // c -> a = 1
    print(c->b->a); // c -> b -> a = 2
    return 0;
}