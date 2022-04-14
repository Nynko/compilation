

int main(){
    int a; 
    int b;
    int c;
    int d;
    a = 3;
    c = 2;
    {
        int a;
        a = 4;
        print(a); // 4
        {
            b = 6;
        }
        d = 0;
        while(c>0){
            c = c - 1;
            while (d < 5)
            {
                d = d + 1;
            }
        }
        
    }
    print(a); // 3
    print(b); // 6
    print(d); // 5
    print(c); // 0
    return 0;
}