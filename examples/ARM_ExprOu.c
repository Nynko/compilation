

int main(){

    int a;
    int b;
    int c;
    a = 0;
    b = 1;
    c = 0;
    if(a || b || c){
        print(10212);
    }

    if(a || c || b){
        print(12333);
    }

    if(a ||c){
        print(556);
    }
    return 0;
}