package light;

import scene.Vec3;
import surface.Surface;
import raytracer.Ray;
import scene.Scene;
import scene.Vec2;

public abstract class Light {
	Vec3 color;
	Vec3 position;
	Vec3 direction;
	Vec2 falloff;
	double specularComponent;
	Vec3 ambientColor;
	
	
	public Vec3 getAmbientColor() {
		return ambientColor;
	}
	public void setAmbientColor(Vec3 ambientColor) {
		this.ambientColor = ambientColor;
	}
	public double getSpecularComponent() {
		return specularComponent;
	}
	public void setSpecularComponent(double specularComponent) {
		this.specularComponent = specularComponent;
	}
	public Vec3 getColor() {
		return color;
	}
	public Vec3 getPosition() {
		return position;
	}
	public Vec3 getDirection() {
		return direction;
	}
	public Vec2 getFalloff() {
		return falloff;
	}
	public void setColor(Vec3 color) {
		this.color = color;
	}
	public void setPosition(Vec3 position) {
		this.position = position;
	}
	public void setDirection(Vec3 direction) {
		this.direction = direction;
	}
	public void setFalloff(Vec2 falloff) {
		this.falloff = falloff;
	}
	public abstract Vec3 illuminate(Ray ray, Surface surface, Vec3 normalOfIntersect, Vec3 pointOfIntersect);
	
	@Override
	public String toString() {
		return "Light [color=" + color + ", position=" + position + ", direction=" + direction + ", falloff=" + falloff
				+ "]";
	}
	public abstract boolean inShadow(Scene scene, Ray ray, Vec3 pointOfIntersect, Vec3 normalOfIntersect);
}
