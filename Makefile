SOURCES = $(shell find src -type f -name "*.java")
CLASSES = $(patsubst src/%.java,out/%.class,$(SOURCES))
MAINCLASS = Hw0

all: $(CLASSES)

run:
	java -classpath out Hw0

pack:
	zip hw0.zip -r Makefile src

clean:
	rm -rf out

$(CLASSES): $(SOURCES) out
	javac $(SOURCES) -d out

out:
	mkdir -p out