

struct Int {
  int a;
  struct Int * b;
};

int main(){
    struct Int * b ;
    int a;
    int c;
    int d;
    a = 10;
    b = malloc(8);
    c = 0;
    d = 0;
    while(a){
        c = b;
        d = -c;
        b = malloc(8);
        a = a - 1;
        print(a);
        c = b + d;
        print(c);
    }
    return 0;
}