classes = IDMain IDAlg IDUtils IDNode IDMath

all: $(classes)

$(classes):
						javac $@.java

.PHONY: clean

clean:
			rm -f ./*.class