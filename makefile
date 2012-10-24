JFLAGS = -g
JC = javac
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = simulation.java

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
