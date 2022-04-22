int facto(int result) { //Test de factoriel
    if (result = 0) {
        return 1;
    }
    else
    {
        return result * facto(result - 1);
    }
    
}


int main()
{
    int a;
    int b;
    a = 3;
    b = facto(a); //appel de fonction récursif
    print(b);

    a = 0;
    while(a < 5) { // While et if imbriqué avec déclaration dans la boucle
        int c;
        c = a + b;
        if (c == 6) {
            print(6);
        }
        else {
            print(10);
        }
        print(c);
        a = a + 1;
    }
    return 0;
}
