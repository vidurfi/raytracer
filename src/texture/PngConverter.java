package texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scene.Vec3;

public class PngConverter {
	private Vec3[][] uvMap;
	private List<PngChunk> chunks;
	private byte[] byteArray;
	private Png png;
	
	public PngConverter(File textureFile){
		this.byteArray = calcBytes(textureFile);
		this.chunks = calcChunks();
		this.png = new Png(this.chunks, textureFile);
		this.uvMap = png.getUVMap();
	}
	
	private byte[] calcBytes(File textureFile) {
		byte[] buffer = new byte[(int) textureFile.length()];
		try {
			FileInputStream fis = new FileInputStream(textureFile);
			fis.read(buffer);
			fis.close();	         
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException!");
			e.printStackTrace();
		}
		return buffer;
	}

	private List<PngChunk> calcChunks() {
		boolean run = true;
		List<PngChunk> chunks = new ArrayList<PngChunk>();
		PngChunk chunk = new PngChunk(8, byteArray);
		chunks.add(chunk);
		for(int i = chunk.nextIndex(); run; i = chunk.nextIndex()) {
			chunk = new PngChunk(i, byteArray);
			chunks.add(chunk);
			if (chunk.getType().equals("IEND")) run = false;
		}			
		return chunks;
	}

	public Vec3[][] getUvMap() {
		return uvMap;
	}
	
}
