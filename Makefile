all: grammar build test



# targets
grammar: FORCE
	antlr4 -o src/main/compilateur -Dlanguage=Java grammar/*.g4

build:
	./gradlew build -Dskip.tests

java: build

.PHONY: run
run: FORCE
	./gradlew run --args "$(ARGS)"

test: grammar
	./gradlew test

FORCE:
