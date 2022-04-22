
struct test {
  int a;
  struct test * b;
};

int func(int a, int b,int c, int d){

    print(a);
    print(b);
    print(c);
    print(d);
    if(a == 47){
        func(a+b+c+d,a+b*c+d,a+b*c*d,a+b*c*d*d);
    }
    return c;
}
int main(){
    struct test * a ;
    struct test * b ;
    int c;
    c = 2;
    a = malloc(sizeof(struct test));
    if(a) {
        a -> b = malloc(sizeof(struct test));
    }
    a -> b -> a = 55;
    func(45+c,3+5*6-c,sizeof(struct test),a->b->a);
    return 0;
}










