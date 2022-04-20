int funct(int c){
    if (c==0) {
        return 0;
    }
    return funct(c-1) + 1;
}

int main(){
    print(funct(4));
    return 0;
}








