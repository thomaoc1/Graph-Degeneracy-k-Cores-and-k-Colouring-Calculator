JFLAGS = -g
JC = javac

all: main 

main:
	$(JC) src/*.java

clean:
	rm src/*.class
