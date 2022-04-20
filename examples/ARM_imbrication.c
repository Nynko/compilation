

int main(){
    int a; 
    int b;
    int c;
    int d;
    int compt;
    a = 3;
    c = 2;
    {
        int a;
        compt = 0;
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

                if(d == 1){
                    compt = compt + 1;
                }
                else{
                    compt = compt - 1;
                }
            }
        }
        
    }
    print(a); // 3
    print(b); // 6
    print(d); // 5
    print(c); // 0
    print(compt); // -3
    return 0;
}