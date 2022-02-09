


struct a{
    int a;
    int b;
};

struct b{
    struct a * c;
    int d;
    int e;
    int f;
};

struct a * f(){
    struct a* pa;
    pa = malloc(sizeof(struct a));
    return pa;
}


int main(){
    int condition;
    if(condition){
        {
            int a;
            {
                struct b * a;
                return 0;
            }
        }
    }
    else{
        int a;
        int condition;
        a + condition;
        while(condition){
            int d;
            {
                return 0;
            }
        }
    }

    return 1;
}