package raytracer;

import java.io.File;

import scene.Scene;

public class Main {
	public static void main(String[] args) {
		if(args.length != 0) {
			File inputFile = new File(args[0]);
			Scene scene = new Scene(inputFile);
			Raytracer.render(scene);
		} else {
			System.out.println("Please specify an inputfile!");
		}
	}
}
