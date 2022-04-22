int main()
{
    int a; //Déclaration de variable
    int b;
    int c;

    b = 5; // Affectation 
    a = 5 + 6; // Affectation arithmétique
    print(a); // Vérification (a = 11)
    print(b); // 5

    if (b > 0) { // test de if et >
        print(1);
    }

    b = 0;
    while (1 && b < 2 ) // test de while et expression arithmétique
    {
        a = 5 + 6 * 2; // ordre des opérations
        if(0 || a > 0 && 1) // test de if et expression arithmétique
        {
            if(0 || 1 && 1 && 1 || 0){
                print(a); // 17
            }
        }
        b = b + 1; // opération avec variable
        print(b); // 0 puis 1
        c = 1 && 2 <= 3; 
        print(c); // 1 
    }

    return 0;
}
