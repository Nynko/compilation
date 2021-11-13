all: grammar java

# targets
grammar: FORCE
	antlr4 -o src/main/compilateur -Dlanguage=Java grammar/*.g4

java:
	./gradlew run

test: grammar
	./gradlew test

FORCE:
