

struct test {
  int a;
  struct test * b;
};

int main(){
    struct test * a ;
    struct test * b ;
    int c;
    c = 2;
    a = malloc(sizeof(struct test));
    if(a) {
        a -> b = malloc(sizeof(struct test));
    }
    while(c>=0){
        a -> a = 11;
        c = c -1;
    }
    print(a->a);
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
    
    print(a-> b -> a);
    print(b -> a);
    print(b -> b -> a);

    return 0;
}