


struct a{
    int a;
    int b;
};

struct b{
    struct a * c;
    int c;
    int d;
    int e;
    int f;
};


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
        while(condition){
            int d;
            {
                return 0;
            }
        }
    }

    return 1;
}