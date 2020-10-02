package texture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import scene.Vec3;

public class Png {
	private int width;
	private int height;
	private int colorType;
	private List<Vec3> pixels = new ArrayList<Vec3>();
	private File textureFile;
	
	public Png(List<PngChunk> chunks, File textureFile) {
		this.textureFile = textureFile;
		this.width = calcWidth(chunks.get(0));
		this.height = calcHeight(chunks.get(0));
		this.colorType = calcColorType(chunks.get(0));
		calcPixels();
	}
	private int calcWidth(PngChunk chunk) {
		byte[] widthArray = new byte[4];
		for(int i = 0; i < 4; i++)
			widthArray[i] = chunk.getData()[i];
		int width = ByteBuffer.wrap(widthArray).getInt();
		return width;
	}
	private int calcHeight(PngChunk chunk) {
		byte[] heightArray = new byte[4];
		for(int i = 0; i < 4; i++)
			heightArray[i] = chunk.getData()[i+4];
		int height = ByteBuffer.wrap(heightArray).getInt();
		return height;
	}
	private int calcColorType(PngChunk chunk) {
		return (int) chunk.getData()[9];
	}
	private void calcPixels() {
		try {
			BufferedImage bufferedImage = ImageIO.read(textureFile);
		    byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();	        
	        int[][] result = new int[height][width];
		    final int pixelLength = 3;
		    for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
		       int rgb = 0;
		       rgb += -16777216;
		       rgb += ((int) pixels[pixel] & 0xff);
		       rgb += (((int) pixels[pixel + 1] & 0xff) << 8);
		       rgb += (((int) pixels[pixel + 2] & 0xff) << 16);
		       result[row][col] = rgb;
		       int B = (rgb & 0x000000FF);
		       int G = (rgb & 0x0000FF00) >> 8;
		       int R = (rgb & 0x00FF0000) >> 16;
		       col++;
		       if (col == width) {
		          col = 0;
		          row++;
		       }
		       this.pixels.add(new Vec3(R, G, B));
		    }       		
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public Vec3[][] getUVMap(){
		Vec3[][] image = new Vec3[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				image[i][j] = this.pixels.get(i*height+j);
			}
		}
		return image;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getColorType() {
		return colorType;
	}
	public List<Vec3> getPixels() {
		return pixels;
	}
	
}
