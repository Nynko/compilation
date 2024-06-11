int add(int right) { //Définition de fonction avec 1 paramètre
    int c; // Test du chaînage
    c = 10;
    print(c); // Vérifie la valeur de c
    return a + 1;
}

int mult(int right, int left)  { //Définition de fonction à 2 paramètres
    return right * left;
}


int main()
{
    int a;
    int b;
    int c;

    a = 5;
    c = 2;
    b = add(a); //Appel de fonction avec paramètre
    print(b);
    print(c); //Vérifie que c n'a pas été réécrit par add
    b = mult (a, c);
    print(b);
    return 0;
}
