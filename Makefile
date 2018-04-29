SOURCES = $(shell find src -type f -name "*.java")
CLASSES = $(patsubst src/%.java,out/%.class,$(SOURCES))
MAINCLASS = HW3

all: $(CLASSES)

run:
	java -classpath out $(MAINCLASS)

pack:
	zip $(MAINCLASS).zip -r Makefile src

clean:
	rm -rf out

$(CLASSES): $(SOURCES) out
	javac $(SOURCES) -d out

out:
	mkdir -p out