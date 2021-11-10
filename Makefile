all: grammar java

# targets
grammar: FORCE
	antlr4 -o src/ -Dlanguage=Java grammar/*.g4

java:
	./gradlew run


FORCE:
