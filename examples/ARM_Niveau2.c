int add(int right) { //Définition de fonction avec paramétres
    int c; // Test du chainage
    c = 10;
    print(c); // 10 vérifie qu'on a bien un nouveau c
    return right + 1;
}

int mult(int right, int left)  { //Définition de fonction a 2 paramètres
    return right * left;
}


int main()
{
    int a;
    int b;
    int c;

    a = 5;
    c = 2;
    b = add(a); //Appel de fonction a paramétre
    print(b);
    print(c); //2 vérifie qu'on n'a pas le c définie dans add
    b = mult (a, c);
    print(b);
    return 0;
}
