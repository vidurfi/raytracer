##### Current state

The code was finished on 29. 6. 2020,  and as of yet, it is not completely finished. I ran out of time during the course due to trying to write a PNG converter. It was later revealed that we could use a library for this, but unfortunately it was too late.
I plan to finish this and add the rest of the example scenes in the future.

##### To compile:

javac -d "output-path"\bin\ -cp "src-folder-path"\src "src-folder-path"\src\raytracer\Main.java

e.g.: javac -d C:\javaprojects\raytracer\bin\ -cp C:\javaprojects\raytracer\src C:\javaprojects\raytracer\src\raytracer\Main.java

##### To run this:

java -cp "output-path"\bin raytracer.Main "path-to-xml"

e.g.: java -cp C:\javaprojects\raytracer\bin raytracer.Main "C:\javaprojects\raytracer\scene\example1.xml"

The OBJ files should be in the same folder as the XML files. (in this example in the "scene" folder)
