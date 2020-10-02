package surface;

import java.io.File;

import raytracer.Ray;
import scene.Vec3;

public abstract class Surface {
	protected Vec3 phong;
	protected File textureFile;
	protected Vec3 color;
	protected Vec3 emissionColor;
	protected double phongExponent;
	protected double reflectance;
	protected double transmittance;
	protected double refraction;
	protected boolean itIsNull;
	public Vec3 getPhong() {
		return phong;
	}
	public File getTextureFile() {
		return textureFile;
	}
	public Vec3 getColor() {
		return color;
	}
	public double getPhongExponent() {
		return phongExponent;
	}
	public double getReflectance() {
		return reflectance;
	}
	public double getTransmittance() {
		return transmittance;
	}
	public double getRefraction() {
		return refraction;
	}
	public Vec3 getEmissionColor() {
		return emissionColor;
	}
	public boolean isNull() {
		return itIsNull;
	}
	
	public void setColor(Vec3 color) {
		this.color = color;
	}
	public void setEmissionColor(Vec3 color) {
		this.emissionColor = color;
	}
	public abstract void rotateX(double angle);
	public abstract void rotateY(double angle);
	public abstract void rotateZ(double angle);
	public abstract void scale(Vec3 scalars);
	public abstract void translate(Vec3 translate);
	public abstract boolean intersect(Ray ray);
	public abstract Vec3 getPosition();
}
