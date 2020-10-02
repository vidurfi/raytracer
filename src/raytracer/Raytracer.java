package raytracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import scene.Scene;
import scene.Vec3;

public class Raytracer {
	public static void render(Scene scene) {
		System.out.println("Starting rendering...");
		int height = (int) scene.getCamera().getResolution().getH();
		int width = (int) scene.getCamera().getResolution().getV();
		double lookAtZ = scene.getCamera().getPosition().getZ();
		double fov = scene.getCamera().getHorizontalFOV();
		double aspectRatio = width/height;
		Vec3 rayOrigin = scene.getCamera().getPosition();
		Vec3[][] image = new Vec3[height][width];
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				double dirX = aspectRatio*((2*(x+0.5)/width)-1)*Math.tan(fov*Math.PI/180);
				double dirY = (1-2*(y+0.5)/height)*Math.tan(fov*Math.PI/180);
				Vec3 rayDirection = (new Vec3(dirX, dirY, lookAtZ));
				Ray ray = new Ray(rayOrigin, rayDirection);
				image[x][y] = Ray.trace(scene, ray, 0);
			}
		}
		System.out.println("Done rendering!");
		String str = scene.getOutputFile();
		str = str.substring(str.lastIndexOf("\\")+1);
		String outputString = str.substring(0, str.indexOf("."));
		File outputFile = new File(outputString+".ppm");
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			System.out.println("Starting writing...");
			String matrix = "P3\n" + width + " " + height + "\n255\n";
			fos.write(matrix.getBytes());
			int rgb[] = new int[3];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					rgb[0] = (int) (image[width-1-x][height-1-y].getX()*255);
					rgb[1] = (int) (image[width-1-x][height-1-y].getY()*255);
					rgb[2] = (int) (image[width-1-x][height-1-y].getZ()*255);
					matrix = "" + rgb[0] + " " + rgb[1] + " " + rgb[2] + " ";
					fos.write(matrix.getBytes());
				}
				matrix = "\n";
				fos.write(matrix.getBytes());
			}
			System.out.println("Done writing!");
			fos.close();
			System.out.println("Image saved at: " + outputFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			System.out.println("Writing failed, file not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
