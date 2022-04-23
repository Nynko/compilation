int main(){
    if( 1 || 1){
        print(1);
    }
    if( 1 && 1){
        print(2);
    }
    if( 1 && 0){
        print(456);
    }
    else {
        print(3);
    }
    if( 0 || 0){
        print(256);
    }
    else {
        print(4);
    }
    if( 1 || 0){
        print(5);
    }
    return 0;
}