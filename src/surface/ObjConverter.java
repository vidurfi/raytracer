package surface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import scene.Vec2;
import scene.Vec3;

public final class ObjConverter {
	
	private File meshFile;
	private List<String> content;
	private List<Vec3> vertices;
	private List<Vec3> normals;
	private List<Vec2> textures;
	private List<Face> faces;
	
	public ObjConverter(File meshFile){
		this.meshFile = meshFile;
		this.content = fileToString();
		this.vertices = getSpecifiedVertices("v");
		this.normals = getSpecifiedVertices("vn");
		this.textures = getVec2List(getSpecifiedVertices("vt"));
		this.faces = initFaces();
	}
	
	private List<Face> initFaces(){
		List<Face> faces = new ArrayList<Face>();
		this.content.forEach((line) -> {
			String[] substrings = line.split(" ");
			if(substrings[0].equals("f")) {
				String[] firstIndices = substrings[1].split("/");
				String[] secondIndices = substrings[2].split("/");
				String[] thirdIndices = substrings[3].split("/");
				Vec3 a = this.vertices.get(Integer.parseInt(firstIndices[0])-1);
				Vec2 texA = this.textures.get(Integer.parseInt(firstIndices[1])-1);
				Vec3 normal = this.normals.get(Integer.parseInt(firstIndices[2])-1);
				
				Vec3 b = this.vertices.get(Integer.parseInt(secondIndices[0])-1);
				Vec2 texB = this.textures.get(Integer.parseInt(secondIndices[1])-1);
				
				Vec3 c = this.vertices.get(Integer.parseInt(thirdIndices[0])-1);
				Vec2 texC = this.textures.get(Integer.parseInt(thirdIndices[1])-1);
				
				faces.add(new Face(a, b, c, texA, texB, texC, normal ));
			}
		});
		return faces;
	}
	
	private List<Vec2> getVec2List(List<Vec3> vec3List){
		List<Vec2> newList = new ArrayList<Vec2>();
		vec3List.forEach((item) -> {
			newList.add(item.toVec2());
		});
		return newList;
	}
	
	private List<Vec3> getSpecifiedVertices(String modifier) {
		List<Vec3> vec3 = new ArrayList<Vec3>();
		List<String> modifiers = getModifiers();
		if (modifiers.contains(modifier)) {
			this.content.forEach((line)->{
				String[] substrings = line.split(" ");
				if((substrings[0].equals(modifier)) && (modifier.equals("vt"))) {
					vec3.add(new Vec3(Double.parseDouble(substrings[1]), Double.parseDouble(substrings[2]), 0));
				} else if(substrings[0].equals(modifier)) {
					vec3.add(new Vec3(Double.parseDouble(substrings[1]), Double.parseDouble(substrings[2]), Double.parseDouble(substrings[3])));
				}
			});
		} else {
			System.out.println("Modifier: " + modifier +", doesn't exist!");
		}
		return vec3;
	}
	
	private List<String> getModifiers(){
		List<String> modifiers = new ArrayList<String>();
		this.content.forEach((line) -> {
			String modifier = line.substring(0, line.indexOf(" "));
			if (!modifiers.contains(modifier) && modifier!="#") {
				modifiers.add(modifier);
			}
		});
		return modifiers;
	}
	
	private List<String> fileToString() {
		List<String> fileString = new ArrayList<String>();
		try {
			Scanner reader = new Scanner(this.meshFile);
			while (reader.hasNextLine()) {
				fileString.add(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Meshfile (OBJ file) not found!");
			e.printStackTrace();
		}
		return fileString;		
	}

	public List<Vec3> getVertices() {
		return vertices;
	}

	public List<Vec3> getNormals() {
		return normals;
	}

	public List<Vec2> getTextures() {
		return textures;
	}

	public List<Face> getFaces() {
		return faces;
	}	
}
