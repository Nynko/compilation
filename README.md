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
make run -- ARGS="examples/good.c"
```

Compiler l'intégralité du projet :

```bash
make all
```

Lancer le projet :

```bash
./gradlew run
```

Compiler un fichier sous mac ou linux avec l'executable:
(Remplacer macos par linux pour les machines linux arm)

```bash
make macos ARGS="pathToTheCFile"
```

Run sl:
(replace macos by linux for arm linux machine)

```bash
make macos ARGS=./examples/\(ARM\)_sl.c
./out/ARM
```
