all: grammar build test



# targets
grammar: FORCE
	antlr4 -o src/main/compilateur -visitor -Dlanguage=Java grammar/*.g4

build:
	./gradlew build -Dskip.tests

java: build

.PHONY: run
run: FORCE
	./gradlew run --args "$(ARGS)"

test: grammar
	./gradlew test

ast: FORCE
		./gradlew run --args "$(ARGS)"
		dot -Tsvg ./out/tree.dot -o ./out/tree.svg

tds : FORCE
		./gradlew run --args "$(ARGS)"
		dot -Tsvg ./out/tree.dot -o ./out/tree.svg
		dot -Tsvg ./out/tds.dot -o ./out/tds.svg

macos: FORCE
		./gradlew run --args "$(ARGS)" 
		dot -Tsvg ./out/tree.dot -o ./out/tree.svg
		dot -Tsvg ./out/tds.dot -o ./out/tds.svg
		as -o ./out/ARM.o ./out/ARM.s
		ld -o ./out/ARM ./out/ARM.o -lSystem -syslibroot `xcrun -sdk macosx --show-sdk-path` -arch arm64

linux: FORCE
		./gradlew run --args "$(ARGS)"
		dot -Tsvg ./out/tree.dot -o ./out/tree.svg
		dot -Tsvg ./out/tds.dot -o ./out/tds.svg
		as -o ./out/ARM.o ./out/ARM.s
		ld -o ./out/ARM ./out/ARM.o -lc -L'/usr/lib/aarch64-linux-gnu/' 


macosNamed: FORCE
		./gradlew run --args "$(ARGS)"
		as -o ./bin/ARM/ARMo/$(NAME).o ./bin/ARM/ARMs/$(NAME).s
		ld -o ./bin/ARM/$(NAME) ./bin/ARM/ARMo/$(NAME).o -lSystem -syslibroot `xcrun -sdk macosx --show-sdk-path` -arch arm64

linuxNamed: FORCE
		./gradlew run --args "$(ARGS)"
		as -o ./bin/ARM/ARMo/$(NAME).o ./bin/ARM/ARMs/$(NAME).s
		ld -o ./bin/ARM/$(NAME) ./bin/ARM/ARMo/$(NAME).o -lc -L'/usr/lib/aarch64-linux-gnu/'

macosDebug: FORCE
		./gradlew run --args "$(ARGS)"

FORCE:

