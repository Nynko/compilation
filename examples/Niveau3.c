int facto(int result) { //Test de factorielle
    if (result = 0) {
        return 1;
    }
    else
    {
        return result * facto(result - 1)
    }
    
}


int main()
{
    int a;
    int b;
    int c;
    c = 1;
    a = 3;
    b = facto(a); //Appel de fonction récursive
    print(b);

    a = 0;
    while(a < 5) { // While et if imbriqués avec déclaration dans la boucle
        int c;
        c = a + b;
        if (c == 6) {
            print(6)
        }
        else {
            print(10)
        }
        print(c);
        a = a + 1;
    }
    return 0;
}
