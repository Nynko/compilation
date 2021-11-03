all: grammar java

# targets
grammar: grammar/*
	antlr4 -o src/grammar -Dlanguage=Java grammar/*.g4

java:
	./gradlew run


