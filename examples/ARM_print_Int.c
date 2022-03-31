
int func(int c){
    if(c==0){
        return 0;
    }
    else{
        return func(c-1)+1;
    }

}
int main(){
    int a;
    a = func(100);
    print(a);
    return 0;
}










