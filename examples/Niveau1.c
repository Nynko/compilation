int main()
{
    int a; //Déclaration de variable
    int b;
    int c;

    b = 5; // Affectation 
    a = 5 + 6; // Affectation avec opération arithmétique
    print(a);
    print(b); 

    if (b > 0) { // Test de if et >
        print(1);
    }

    b = 0;
    while (1 && b < 2 ) // Test de while et expression arithmétique
    {
        a = 5 + 6 * 2; // Précédence des opérations
        print(a); 
        b = b + 1; // Opération avec variable
        print(b); 
        c = 1 && 2 <= 3; 
        print(c); 
    }

    return 0;
}
