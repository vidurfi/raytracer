package light;

import raytracer.Ray;
import scene.Scene;
import scene.Vec3;
import surface.Surface;

public class ParallelLight extends Light {
	public ParallelLight(Vec3 color, Vec3 direction) {
		this.color = color;
		this.direction = direction;
		this.ambientColor = null;
	}
	public void setDirection(Vec3 direction) {
		this.direction = direction;
	}
	public Vec3 getDirection() {
		return direction;
	}
	@Override
	public Vec3 illuminate(Ray ray, Surface surface, Vec3 normalOfIntersect, Vec3 pointOfIntersect) {
		Vec3 lightDirection = Vec3.normalize(this.direction);
		double lambertian = Math.max(0.0, Vec3.dot(normalOfIntersect, lightDirection));
		double diffuseComponent =  surface.getPhong().getY() * lambertian; //kD * (l . n)
		if (lambertian > 0) {
			Vec3 reflected = Vec3.normalize(Vec3.subtract(lightDirection, Vec3.multiply(normalOfIntersect, (Vec3.dot(normalOfIntersect, lightDirection)*2))));
			Vec3 vector = Vec3.normalize(Vec3.subtract(ray.getRayOrigin(), pointOfIntersect));
			double specAngle = Math.max(Vec3.dot(reflected, vector), 0.0);
			double specular = Math.pow(specAngle, surface.getPhongExponent());
			this.specularComponent = surface.getPhong().getZ() * specular; //kS * (v . r)^alpha
		}
		return Vec3.multiply(this.color, (diffuseComponent));
	}

	public boolean inShadow(Scene scene, Ray ray, Vec3 pointOfIntersect, Vec3 normalOfIntersect) {
		Vec3 lightDirection = this.direction;
		// BACKWALL: Vec3 toLightDirection = Vec3.normalize(Vec3.subtract(Vec3.subtract(ray.getRayOrigin(), lightDirection), Vec3.multiply(ray.getRayOrigin(), pointOfIntersect)));
		// SAUSAGE: Vec3 toLightDirection = Vec3.normalize(Vec3.subtract(Vec3.subtract(ray.getRayOrigin(), lightDirection), Vec3.multiply(ray.getRayDirection(), pointOfIntersect)));
		// WORKS: Vec3 toLightDirection = Vec3.add(Vec3.normalize(Vec3.subtract(Vec3.subtract(ray.getRayOrigin(), lightDirection), Vec3.multiply(ray.getRayOrigin(), pointOfIntersect))), Vec3.normalize(lightDirection));
		Vec3 toLightDirection = Vec3.add(Vec3.normalize(Vec3.subtract(Vec3.subtract(ray.getRayOrigin(), lightDirection), Vec3.multiply(ray.getRayOrigin(), pointOfIntersect))), Vec3.normalize(lightDirection));
		Ray shadowRay = new Ray(pointOfIntersect, toLightDirection);
		for (int j = 0; (j < scene.getSurfaces().size()); j++) {
			if (scene.getSurfaces().get(j).intersect(shadowRay)) return true;
		}
		return false;
	}
}
