# Projet compilation

## Compiler/lancer le projet

Générer la grammaire avec Antlr4 :

```bash
make grammar
```

Compiler le Java :

```bash
make build
```

Lancer les tests unitaires :

```bash
make test
```

Afficher un arbre syntaxique

```bash
make run -- ARGS="examples/good.exp"
```

Compiler l'intégralité du projet :

```bash
make all
```

Lancer le projet :

```bash
./gradlew run
```
