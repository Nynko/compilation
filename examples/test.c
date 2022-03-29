int f(int a){
    return 1;
}

int main() {
    int a, b;
    a = 1;
    {
        a = 10;
    }
    b = f(1);
    b = a + b;
    return b;
}