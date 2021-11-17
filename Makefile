all: grammar java

# targets
grammar: FORCE
	antlr4 -o src/main/compilateur -visitor -Dlanguage=Java grammar/*.g4

java:
	./gradlew run

test: grammar
	./gradlew test

ast:
	./gradlew run
	dot -Tsvg ./out/tree.dot -o ./out/tree.svg

FORCE:
