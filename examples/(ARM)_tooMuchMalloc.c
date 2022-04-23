struct Int{
    int a;
    struct Int * b;
};

int main(){
    int a;
    struct Int * b;
    a = 247;
    while(a){
        b = malloc(8);
        a = a - 1;
        print(247-a);
    }
    return 0;
}