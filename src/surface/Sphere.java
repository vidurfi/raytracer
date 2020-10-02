package surface;

import java.io.File;

import raytracer.Ray;
import scene.Vec3;
import texture.Png;
import texture.PngConverter;

public class Sphere extends Surface{
	private Vec3 position;
	private Vec3 radius;
	private Vec3[][] colorArray;
	private double rotationX;
	private double rotationY;
	private double rotationZ;
	public Sphere() {
		this.itIsNull = true;
	}
	public Sphere(Vec3 position, File textureFile, Vec3 radius, Vec3 phong, double phongExponent, double reflectance, double transmittance, double refraction) {
		this.position = position;
		this.textureFile = textureFile;
		PngConverter png = new PngConverter(this.textureFile);
		this.colorArray = png.getUvMap();
		this.color = new Vec3(0.5,0.5,0.5);
		this.radius = radius;
		this.phong = phong;
		this.phongExponent = phongExponent;
		this.reflectance = reflectance;
		this.transmittance = transmittance;
		this.refraction = refraction;
		this.itIsNull = false;
	}
	public Sphere(Vec3 position, Vec3 color, Vec3 radius, Vec3 phong, double phongExponent, double reflectance, double transmittance, double refraction) {
		this.position = position;
		this.textureFile = null;
		this.color = color;
		this.radius = radius;
		this.phong = phong;
		this.phongExponent = phongExponent;
		this.reflectance = reflectance;
		this.transmittance = transmittance;
		this.refraction = refraction;
		this.itIsNull = false;
	}
	@Override
	public boolean intersect(Ray ray) {
		Vec3 originToCenter = Vec3.subtract(ray.getRayOrigin(), this.position);
		double a = Vec3.dot(ray.getRayDirection(), ray.getRayDirection());
		double b = 2.0* Vec3.dot(originToCenter, ray.getRayDirection());
		double radius = Vec3.length(Vec3.normalize(this.radius));
		double c = Vec3.dot(originToCenter, originToCenter) - radius*radius;
		double d = b*b - 4*a*c;
		if (d < 0) return false;
		else if (d == 0) {
			ray.setEntryPoint(-0.5*b/a);
			ray.setExitPoint(ray.getEntryPoint());
		} else {
			double q = 0;
			if (b > 0) q = -0.5*(b + Math.sqrt(d)); 
			if (b <= 0) q = -0.5*(b - Math.sqrt(d));
			if(ray.getEntryPoint() <= ray.getEntryPoint()) {
				ray.setEntryPoint(q/a);
				ray.setExitPoint(c/q);
			} else {
				ray.setExitPoint(q/a);
				ray.setEntryPoint(c/q);
			}
		}
		return true;
	}
	public Vec3 getPosition() {
		return position;
	}
	public Vec3 getRadius() {
		return radius;
	}
	public double getRotationX() {
		return rotationX;
	}
	public double getRotationY() {
		return rotationY;
	}
	public double getRotationZ() {
		return rotationZ;
	}
	@Override
	public String toString() {
		return "Sphere [position=" + position + ", radius=" + radius + ", phong=" + phong + ", textureFile="
				+ textureFile + ", color=" + color + ", phongExponent=" + phongExponent + ", reflectance=" + reflectance
				+ ", transmittance=" + transmittance + ", refraction=" + refraction + "]\n";
	}
	@Override
	public void rotateX(double angle) {
		double radian = Math.toRadians(angle);
		/*this.radius = new Vec3(
				this.radius.getX(),
				this.radius.getY()*Math.cos(radian) - this.radius.getZ()*Math.sin(radian),
				this.radius.getY()*Math.sin(radian) + this.radius.getZ()*Math.cos(radian)
				);*/
		this.rotationX = radian;
	}
	@Override
	public void rotateY(double angle) {
		double radian = Math.toRadians(angle);
		/*this.radius = new Vec3(
				this.radius.getX()*Math.cos(radian) + this.radius.getZ()*Math.sin(radian),
				this.radius.getY(),
				-this.radius.getX()*Math.sin(radian) + this.radius.getZ()*Math.cos(radian)
				);*/
		this.rotationY = radian;
	}
	@Override
	public void rotateZ(double angle) {
		double radian = Math.toRadians(angle);
		/*this.radius = new Vec3(
				this.radius.getX()*Math.cos(radian) - this.radius.getY()*Math.sin(radian),
				this.radius.getX()*Math.sin(radian) + this.radius.getY()*Math.cos(radian),
				this.radius.getZ()
				);*/	
		this.rotationZ = radian;
	}
	@Override
	public void scale(Vec3 scalars) {
		this.radius = new Vec3(this.radius.getX()*scalars.getX(), this.radius.getY()*scalars.getY(), this.radius.getZ()*scalars.getZ());
	}
	@Override
	public void translate(Vec3 translate) {
		this.position = new Vec3(this.position.getX() + translate.getX(), this.position.getY() + translate.getY(), this.position.getZ() + translate.getZ());
	}
	
	
}
