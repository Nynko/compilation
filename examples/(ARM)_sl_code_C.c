#include <stdio.h>
#include <stdlib.h>

struct bloc
{
    int nb_element;
    int e1;
    int e2;
    int e3;
    int e4;
    int e5;
    int e6;
    int e7;
    int e8;
    int e9;
    int e10;
};

struct ligne
{
    int nb_bloc;
    struct bloc * b1;
    struct bloc * b2;
    struct bloc * b3;
    struct bloc * b4;
    struct bloc * b5;
    struct bloc * b6;
    struct bloc * b7;
    struct bloc * b8;
    struct bloc * b9;
    struct bloc * b10;
};

struct matrice
{
    int nb_ligne;
    struct ligne * l1;
    struct ligne * l2;
    struct ligne * l3;
    struct ligne * l4;
    struct ligne * l5;
    struct ligne * l6;
    struct ligne * l7;
    struct ligne * l8;
    struct ligne * l9;
    struct ligne * l10;
    struct ligne * l11;
    struct ligne * l12;
    struct ligne * l13;
    struct ligne * l14;
    struct ligne * l15;
    struct ligne * l16;
    struct ligne * l17;
    struct ligne * l18;
    struct ligne * l19;
    struct ligne * l20;
};


int get_element(struct bloc * lig, int indice){
    if (indice == 0){
        return lig->e1;
    } else if (indice == 1){
        return lig->e2;
    } else if (indice == 2){
        return lig->e3;
    } else if (indice == 3){
        return lig->e4;
    } else if (indice == 4){
        return lig->e5;
    } else if (indice == 5){
        return lig->e6;
    } else if (indice == 6){
        return lig->e7;
    } else if (indice == 7){
        return lig->e8;
    } else if (indice == 8){
        return lig->e9;
    } else if (indice == 9){
        return lig->e10;
    } else {
        return -1;
    }
}

struct bloc * get_bloc(struct ligne * lig, int indice){
    if (indice == 0){
        return lig->b1;
    } else if (indice == 1){
        return lig->b2;
    } else if (indice == 2){
        return lig->b3;
    } else if (indice == 3){
        return lig->b4;
    } else if (indice == 4){
        return lig->b5;
    } else if (indice == 5){
        return lig->b6;
    } else if (indice == 6){
        return lig->b7;
    } else if (indice == 7){
        return lig->b8;
    } else if (indice == 8){
        return lig->b9;
    } else if (indice == 9){
        return lig->b10;
    }
    return 0;
}

struct ligne * get_ligne(struct matrice * mat, int indice){
    if (indice == 0){
        return mat->l1;
    } else if (indice == 1){
        return mat->l2;
    } else if (indice == 2){
        return mat->l3;
    } else if (indice == 3){
        return mat->l4;
    } else if (indice == 4){
        return mat->l5;
    } else if (indice == 5){
        return mat->l6;
    } else if (indice == 6){
        return mat->l7;
    } else if (indice == 7){
        return mat->l8;
    } else if (indice == 8){
        return mat->l9;
    } else if (indice == 9){
        return mat->l10;
    } else if (indice == 10){
        return mat->l11;
    } else if (indice == 11){
        return mat->l12;
    } else if (indice == 12){
        return mat->l13;
    } else if (indice == 13){
        return mat->l14;
    } else if (indice == 14){
        return mat->l15;
    } else if (indice == 15){
        return mat->l16;
    } else if (indice == 16){
        return mat->l17;
    } else if (indice == 17){
        return mat->l18;
    } else if (indice == 18){
        return mat->l19;
    } else if (indice == 19){
        return mat->l20;
    }
    return 0;
}

int get_indice_bloc(int indice, int taille_bloc){
    return indice / taille_bloc; 
}

int print_bloc(struct bloc * blo) {
    int nb_element;
    int i;
    nb_element = blo->nb_element;
    i = 0;
    while (i != nb_element)
    {
        printf("%c",get_element(blo, i));
        i = i + 1;
    }
    return 0;
}

int print_bloc_decalage(struct bloc * blo, int debut, int fin) {
    int nb_element;
    int i;
    nb_element = blo->nb_element;
    i = debut;
    while (i != fin + 1)
    {
        printf("%c",get_element(blo, i));
        i = i + 1;
    }
    return 0;
}

int print_ligne(struct ligne * lig){
    int nb_bloc;
    int i;
    nb_bloc = lig->nb_bloc;
    i = 0;
    while (i != nb_bloc) {
        print_bloc(get_bloc(lig, i));
        i = i + 1;
    }
    printf("%c",'n');
    return 0;
}

int print_ligne_decalage(struct ligne * lig, int debut, int fin){
    int nb_bloc, indice_bloc_debut, indice_bloc_fin;
    int i;
    nb_bloc = lig->nb_bloc;
    indice_bloc_debut = get_indice_bloc(debut, 10);
    indice_bloc_fin = get_indice_bloc(fin, 10);
    i = indice_bloc_debut;
    while ( i != indice_bloc_fin + 1 ) {
        if (i == indice_bloc_debut)
        {
            int temp;
            temp = nb_bloc * 10 - 1;
            while (temp != fin)
            {
                printf("%c",'s');
                temp = temp - 1;
            }

            if (fin >= 10)
            {
                print_bloc_decalage(get_bloc(lig, i), debut - indice_bloc_debut * 10, 9);
            }
            else {
                print_bloc_decalage(get_bloc(lig, i), debut - indice_bloc_debut * 10, fin);
            }
            
        }
        else if (i == indice_bloc_fin){
            print_bloc_decalage(get_bloc(lig, i), 0, fin - indice_bloc_fin * 10);
        }
        else
        {
            print_bloc(get_bloc(lig, i));
        }
        i = i + 1;
    }
    printf("%c",'n');
    return 0;
}

int print_matrice(struct matrice * mat){
    int nb_ligne;
    int i;
    nb_ligne = mat->nb_ligne;
    i = 0;
    while (i != nb_ligne) {
        print_ligne(get_ligne(mat, i));
        i = i + 1;
    }
    printf("%c",'n');
    return 0;
}

int print_matrice_decalage(struct matrice * mat, int debut, int fin){
    int nb_ligne;
    int i;
    nb_ligne = mat->nb_ligne;
    i = 0;
    while (i != nb_ligne) {
        print_ligne_decalage(get_ligne(mat, i), debut, fin);
        i = i + 1;
    }
    printf("%c",'n');
    return 0;
}

struct bloc * declaration_bloc(int nb_element){
    struct bloc * blo;
    blo = malloc(sizeof(struct bloc));
    blo->nb_element = nb_element;
    blo->e1 = 'a';
    blo->e2 = 'b';
    blo->e3 = 'c';
    blo->e4 = 'd';
    blo->e5 = 'e';
    blo->e6 = 'f';
    blo->e7 = 'g';
    blo->e8 = 'h';
    blo->e9 = 'i';
    blo->e10 = 'j';

    return blo;
}

struct ligne * declaration_ligne(int nb_bloc){
    struct ligne * lig;
    lig = malloc(sizeof(struct ligne));
    lig->nb_bloc = nb_bloc;
    lig->b1 = declaration_bloc(10);
    lig->b2 = declaration_bloc(10);
    lig->b3 = declaration_bloc(10);
    lig->b4 = declaration_bloc(10);
    lig->b5 = declaration_bloc(10);
    lig->b6 = declaration_bloc(10);
    lig->b7 = declaration_bloc(10);
    lig->b8 = declaration_bloc(10);
    lig->b9 = declaration_bloc(10);
    lig->b10 = declaration_bloc(10);

    return lig;
}

struct matrice * declaration_matrice(int nb_ligne, int nb_bloc){
    struct matrice * mat;
    mat = malloc(sizeof(struct matrice));
    mat->nb_ligne = nb_ligne;
    mat->l1 = declaration_ligne(nb_bloc);
    mat->l2 = declaration_ligne(nb_bloc);
    mat->l3 = declaration_ligne(nb_bloc);
    mat->l4 = declaration_ligne(nb_bloc);
    mat->l5 = declaration_ligne(nb_bloc);
    mat->l6 = declaration_ligne(nb_bloc);
    mat->l7 = declaration_ligne(nb_bloc);
    mat->l8 = declaration_ligne(nb_bloc);
    mat->l9 = declaration_ligne(nb_bloc);
    mat->l10 = declaration_ligne(nb_bloc);
    mat->l11 = declaration_ligne(nb_bloc);
    mat->l12 = declaration_ligne(nb_bloc);
    mat->l13 = declaration_ligne(nb_bloc);
    mat->l14 = declaration_ligne(nb_bloc);
    mat->l15 = declaration_ligne(nb_bloc);
    mat->l16 = declaration_ligne(nb_bloc);
    mat->l17 = declaration_ligne(nb_bloc);
    mat->l18 = declaration_ligne(nb_bloc);
    mat->l19 = declaration_ligne(nb_bloc);
    mat->l20 = declaration_ligne(nb_bloc);

    return mat;
}

int main(){
    int nb_ligne, nb_bloc;
    struct matrice * mat;
    struct matrice * mat_vide;
    int i;
    nb_ligne = 20;
    nb_bloc = 10;
    mat = declaration_matrice(nb_ligne, nb_bloc);
    mat_vide = declaration_matrice(nb_ligne, nb_bloc);
    i = 0;
    while (i != nb_bloc*10)
    {
        int j;
        print_matrice_decalage(mat, nb_bloc*10 - i, nb_bloc*10-1);
        j = 0;
        while (j != 10000000)
        {
            j = j + 1;
        }
        i = i + 1;
    }
    i = 0;
    while (i != nb_bloc*10 +1)
    {
        int j;
        print_matrice_decalage(mat, 0, nb_bloc*10-i-1);
        j = 0;
        while (j != 10000000)
        {
            j = j + 1;
        }
        i = i + 1;
    }
    return 0;
}