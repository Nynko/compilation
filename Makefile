all: grammar java

# targets
grammar: grammar/*
	antlr4 -o src/ -Dlanguage=Java grammar/*.g4

java:
	./gradlew run


