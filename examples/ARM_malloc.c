

struct Int {
  int a;
  struct Int * b;
};

int main(){
    struct Int * a ;
    a = malloc(8);
    a -> b = malloc(8);
    a -> a = 11;
    print(a->a);
    a -> b -> a = 55;
    print(a-> b -> a);
    return 0;
}