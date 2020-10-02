package light;

import scene.Vec3;
import surface.Surface;
import raytracer.Ray;
import scene.Scene;
import scene.Vec2;

public class SpotLight extends Light {
	public SpotLight(Vec3 color, Vec3 position, Vec3 direction, Vec2 falloff) {
		this.color = color;
		this.position = position;
		this.direction = direction;
		this.falloff = falloff;
		this.specularComponent = 0;
		this.ambientColor = null;
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
	public Vec3 getPosition() {
		return position;
	}
	public Vec3 getColor() {
		return direction;
	}
	public Vec2 getFalloff() {
		return falloff;
	}
	@Override
	public Vec3 illuminate(Ray ray, Surface surface, Vec3 normalOfIntersect, Vec3 pointOfIntersect) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean inShadow(Scene scene, Ray ray, Vec3 pointOfIntersect, Vec3 normalOfIntersect) {
		// TODO Auto-generated method stub
		return false;
	}
}
