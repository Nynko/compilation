
struct test
{
  int a;
  struct test* b;
  int c;
};


int recupValue(struct test * b, int a){
    int d;
    return b->c;
}

int func(int a, int b){
    print(a);
    print(b);
    return 0;
}

int main(){
    struct test * a;
    struct test * b;
    a = malloc(sizeof(struct test));
    b = malloc(sizeof(struct test));
    a->a = 1;
    a->b = b;
    a -> b -> a = 2;
    a -> b -> c = 3;
    a -> c = 4;
    func(recupValue(a, 0), recupValue(a->b, 1));
    return 0;
}










