package surface;

import java.io.File;
import java.util.List;

import raytracer.Ray;
import scene.Vec3;
import texture.PngConverter;

public class Mesh extends Surface {
	private File meshFile;
	private Vec3[][] colorArray;
	private List<Face> faces;
	public Mesh() {
		this.itIsNull = true;
	}
	public Mesh(File meshFile, File textureFile, Vec3 phong, double phongExponent, double reflectance, double transmittance, double refraction) {
		this.meshFile = meshFile;
		convertOBJ();
		this.color = null;
		this.textureFile = textureFile;
		PngConverter png = new PngConverter(textureFile);
		this.colorArray = png.getUvMap();
		this.color = new Vec3(0.5,0.5,0.5);
		this.phong = phong;
		this.phongExponent = phongExponent;
		this.reflectance = reflectance;
		this.transmittance = transmittance;
		this.refraction = refraction;
		this.itIsNull = false;
	}
	public Mesh(File meshFile, Vec3 color, Vec3 phong, double phongExponent, double reflectance, double transmittance, double refraction) {
		this.meshFile = meshFile;
		convertOBJ();
		this.color = color;
		this.textureFile = null;	
		this.phong = phong;
		this.phongExponent = phongExponent;
		this.reflectance = reflectance;
		this.transmittance = transmittance;
		this.refraction = refraction;
		this.itIsNull = false;
	}
	private void convertOBJ() {
		ObjConverter converter = new ObjConverter(meshFile);
		this.faces = converter.getFaces();
	}
	public File getMeshFile() {
		return meshFile;
	}
	@Override
	public String toString() {
		return "Mesh [meshFile=" + meshFile + ", phong=" + phong + ", textureFile=" + textureFile + ", color=" + color
				+ ", phongExponent=" + phongExponent + ", reflectance=" + reflectance + ", transmittance="
				+ transmittance + ", refraction=" + refraction + "]\n";
	}
	public List<Face> getFaces() {
		return faces;
	}
	@Override
	public void rotateX(double angle) {
		double radian = Math.toRadians(angle);
		this.faces.forEach((face)->{
			face.setA(new Vec3(face.getA().getX(),
					   		   face.getA().getY()*Math.cos(radian) - face.getA().getZ()*Math.sin(radian),
					   		   face.getA().getY()*Math.sin(radian) + face.getA().getZ()*Math.cos(radian)));
			face.setB(new Vec3(face.getB().getX(),
					   		   face.getB().getY()*Math.cos(radian) - face.getB().getZ()*Math.sin(radian),
					   		   face.getB().getY()*Math.sin(radian) + face.getB().getZ()*Math.cos(radian)));
			face.setC(new Vec3(face.getC().getX(),
					   		   face.getC().getY()*Math.cos(radian) - face.getC().getZ()*Math.sin(radian),
					   		   face.getC().getY()*Math.sin(radian) + face.getC().getZ()*Math.cos(radian)));
		});
	}
	@Override
	public void rotateY(double angle) {
		double radian = Math.toRadians(angle);
		this.faces.forEach((face)->{
			face.setA(new Vec3(face.getA().getX()*Math.cos(radian) + face.getA().getZ()*Math.sin(radian),
					   		   face.getA().getY(),
					   		  -face.getA().getX()*Math.sin(radian) + face.getA().getZ()*Math.cos(radian)));
			face.setB(new Vec3(face.getB().getX()*Math.cos(radian) + face.getB().getZ()*Math.sin(radian),
					   		   face.getB().getY(),
					   		  -face.getB().getX()*Math.sin(radian) + face.getB().getZ()*Math.cos(radian)));
			face.setC(new Vec3(face.getC().getX()*Math.cos(radian) + face.getC().getZ()*Math.sin(radian),
					   		   face.getC().getY(),
					   		  -face.getC().getX()*Math.sin(radian) + face.getC().getZ()*Math.cos(radian)));
		});	
	}
	@Override
	public void rotateZ(double angle) {
		double radian = Math.toRadians(angle);
		this.faces.forEach((face)->{
			face.setA(new Vec3(face.getA().getX()*Math.cos(radian) - face.getA().getY()*Math.sin(radian),
					   		   face.getA().getX()*Math.sin(radian) + face.getA().getY()*Math.cos(radian),
					   		   face.getA().getZ()));
			face.setB(new Vec3(face.getB().getX()*Math.cos(radian) - face.getB().getY()*Math.sin(radian),
					   		   face.getB().getX()*Math.sin(radian) + face.getB().getY()*Math.cos(radian),
					   		   face.getB().getZ()));
			face.setC(new Vec3(face.getC().getX()*Math.cos(radian) - face.getC().getY()*Math.sin(radian),
					   		   face.getC().getX()*Math.sin(radian) + face.getC().getY()*Math.cos(radian),
					   		   face.getC().getZ()));
		});			
	}
	@Override
	public void scale(Vec3 scalars) {
		this.faces.forEach((face)->{
			face.setA(new Vec3(face.getA().getX() * scalars.getX(),
							   face.getA().getY() * scalars.getY(),
							   face.getA().getZ() * scalars.getZ()));
			face.setB(new Vec3(face.getB().getX() * scalars.getX(),
					 		   face.getB().getY() * scalars.getY(),
					 		   face.getB().getZ() * scalars.getZ()));
			face.setC(new Vec3(face.getC().getX() * scalars.getX(),
			   	 		  	   face.getC().getY() * scalars.getY(),
				 		  	   face.getC().getZ() * scalars.getZ()));
		});		
	}
	@Override
	public void translate(Vec3 translate) {
		this.faces.forEach((face)->{
			face.setA(new Vec3(face.getA().getX() + translate.getX(),
							   face.getA().getY() + translate.getY(),
							   face.getA().getZ() + translate.getZ()));
			face.setB(new Vec3(face.getB().getX() + translate.getX(),
					 		   face.getB().getY() + translate.getY(),
					 		   face.getB().getZ() + translate.getZ()));
			face.setC(new Vec3(face.getC().getX() + translate.getX(),
			   	 		  	   face.getC().getY() + translate.getY(),
				 		  	   face.getC().getZ() + translate.getZ()));
		});
	}
	@Override
	public boolean intersect(Ray ray) {
		for (int i = 0; (i < this.faces.size()) ; i++) {
			Face face = this.faces.get(i);
			ray.setExitPoint(i);
			if (rayIntersectsTriangleFace(ray, face)) return true;
		}
		return false; 
	}
	
	private boolean rayIntersectsTriangleFace(Ray ray, Face face) {
		double epsilon = 0.0000001;
		Vec3 edge1 = Vec3.subtract(face.getB(), face.getA());
		Vec3 edge2 = Vec3.subtract(face.getC(), face.getA());
		Vec3 h = Vec3.cross(ray.getRayDirection(), edge2);
		
		double a = Vec3.dot(edge1, h);
		if (a > -epsilon && a < epsilon) return false;
		
		double f = 1.0/a;
		Vec3 s = Vec3.subtract(ray.getRayOrigin(), face.getA());
		double u = Vec3.dot(s, h)*f;
		if (u < 0.0 || u > 1.0) return false;
		
		Vec3 q = Vec3.cross(s, edge1);
		double v = Vec3.dot(ray.getRayDirection(), q)*f;
		if (v < 0.0 || u + v > 1.0) return false;
		
		double t = -Vec3.dot(edge2, q)*f;
        if (t > epsilon)
        {
            ray.setEntryPoint(t);
            return true;
        } else return false;
	}
	
	@Override
	public Vec3 getPosition() {
		return null;
	}
	
}
