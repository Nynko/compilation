struct Int{
    int a;
    struct Int * b;
};

int main(){
    int a;
    struct Int * b;
    a = 84;
    while(a){
        b = malloc(8);
        a = a - 1;
        print(a);
        print(b);
    }
    return 0;
}