struct Int{
    int a;
    struct Int * b;
};

int main(){
    int a;
    struct Int * b;
    a = 8185;
    while(a){
        b = malloc(8);
        a = a - 1;
    }
    return 0;
}