
struct b{
  int c;
  int d;
};

struct a {
  int a;
  struct b * b;
};

int main(){
    int a ;
    a = sizeof(struct a); // Should be 16 bytes 
    print(a);
    return 0;
}