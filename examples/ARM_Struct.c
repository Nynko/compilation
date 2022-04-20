struct Int {
  int a;
};

int main(){
    struct Int * a ;
    struct Int * b;
    b = 0;
    a = malloc(8);
    if (!b) {
        a -> a = 5;
    }
    else {
        a -> a = 6;
    }
    print(a->a);
    if (b) {
        a -> a = 4;
    }
    else {
        a -> a = 3;
    }
    print(a->a);
    return 0;
}