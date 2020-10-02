package texture;

import java.nio.ByteBuffer;

public class PngChunk {
	private int startIndex;
	private byte[] byteArray;
	
	private int length;
	private String type;
	private byte[] data;
	private byte[] crc;
	
	public PngChunk(int startIndex, byte[] byteArray) {
		this.startIndex = startIndex;
		this.byteArray = byteArray;
		length = calcLength();
		type = calcType();
		data = calcData();
		crc = calcCrc();
	}
	
	private int calcLength() {
		byte[] lengthArray = new byte[4];
		for(int i = 0; i < 4; i++)
			lengthArray[i] = byteArray[startIndex + i];
		int length = ByteBuffer.wrap(lengthArray).getInt();
		return length;
	}
	
	private String calcType() {
		char[] typeArray = new char[4];
		for(int i = 0; i < 4; i++)
			typeArray[i] = (char)byteArray[startIndex + i + 4];
		String type = typeArray[0] + "" + typeArray[1] + typeArray[2] + typeArray[3];
		return type;
	}
	
	private byte[] calcData() {
		byte[] dataArray = new byte[length];
		for(int i = 0; i < length; i++)
			dataArray[i] = byteArray[startIndex + i + 8];		
		return dataArray;
	}
	
	private byte[] calcCrc() {
		byte[] crcArray = new byte[4];
		for(int i = 0; i < 4; i++)
			crcArray[i] = byteArray[startIndex + i + 8 + length];		
		return crcArray;
	}
	
	public int nextIndex() {
		return startIndex + length + 12;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public String getType() {
		return this.type;
	}

	public byte[] getData() {
		return data;
	}

	public byte[] getCrc() {
		return crc;
	}
	
	
}
