# Template JavaFX

Ce template permet de développer et de lancer une application JavaFX. La bibliothèque JavaFX est automatiquement téléchargée.

## Compiler/lancer le projet

Compiler le projet :

```bash
./gradlew build
```

Lancer le projet :

```bash
./gradlew run
```

## Lire des fichiers/images

Pour utiliser utiliser un fichier (image, texte...) depuis Java, placez-le dans le dossier `resources` et accèdez-y via `/nomdufichier`.

## Compiler fichier .jar

```bash
./gradlew buildJar
```

Pour lancer le fichier :

```bash
java  --module-path PATH/TO/JAVAFX --add-modules javafx.controls,javafx.base,javafx.graphics,javafx.web,javafx.swing,javafx.media -jar build/libs/<projectname>-all.jar
```
