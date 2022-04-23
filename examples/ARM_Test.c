struct test {
  int a;
  struct test * b;
};

int func3(int a, struct test * b){
    {
        b = malloc(sizeof(struct test));
        {
            b -> a = a;
        }
    }
    return 0;
}

int func2(int a, int b, int c){
    struct test * aa;
    struct test * bb;
    print(a);
    print(b);
    print(c);
    aa = malloc(sizeof(struct test));
    {
        {
            aa -> b = malloc(sizeof(struct test));
            func3(a,bb);
            aa -> b -> a = b;
        }
        while(c != (aa -> b -> a)){
            print(c);
            print(aa->b->a);
            c = b;
            return 0;
        }
    }
    return c;
}


int func(int c, int d, int e, int f, int g){
    while(c != 0){
        func2(c,d,e);
        c = c - 1;
    }
    return 0;
}
int main(){
    func(45,43 ,56, 67, 78);
    return 0;
}










