struct c {
    int c;
};

struct a {
    struct c * a;
    int b;
    int c;
    struct e * p;
};

int functionTest(int a, int b){
    int c;
    c = a + b;
    return c;
}

struct a * functionStruct(int b, int a, struct c * ab){
    struct c * e;
}

int main(){
    struct d * ab;
    functionTest(1,2);
    functionStruct(1,2,ab);
    return 0;
}

