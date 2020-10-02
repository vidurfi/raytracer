package light;

import raytracer.Ray;
import scene.Scene;
import scene.Vec3;
import surface.Surface;

public class AmbientLight extends Light {
	public AmbientLight(Vec3 color) {
		this.color = color;
		this.specularComponent = 0;
	}
	@Override
	public Vec3 illuminate(Ray ray, Surface surface, Vec3 normalOfIntersect, Vec3 pointOfIntersect) {
		return this.ambientColor = Vec3.multiply(this.color, surface.getPhong().getX());
	}
	@Override
	public boolean inShadow(Scene scene, Ray ray, Vec3 pointOfIntersect, Vec3 normalOfIntersect) {
		return false;
	}	
}
