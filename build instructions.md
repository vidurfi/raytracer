##### To compile:

javac -d "output-path"\bin\ -cp "src-folder-path"\src "src-folder-path"\src\raytracer\Main.java

e.g.: javac -d C:\javaprojects\raytracer\bin\ -cp C:\javaprojects\raytracer\src C:\javaprojects\raytracer\src\raytracer\Main.java

##### To run this:

java -cp "output-path"\bin raytracer.Main "path-to-xml"

e.g.: java -cp C:\javaprojects\raytracer\bin raytracer.Main "C:\javaprojects\raytracer\scene\example1.xml"

The OBJ files should be in the same folder as the XML files. (in this example in the "scene" folder)

