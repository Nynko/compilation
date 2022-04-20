

struct test {
  int a;
  struct test * b;
};

int main(){
    struct test * a ;
    a = malloc(sizeof(struct test));
    a -> b = malloc(sizeof(struct test));
    a -> a = 11;
    print(a->a);
    a -> b -> a = 55;
    print(a-> b -> a);
    return 0;
}