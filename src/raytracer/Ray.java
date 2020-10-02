package raytracer;

import scene.Scene;
import scene.Vec3;
import surface.Mesh;
import surface.Sphere;
import surface.Surface;

public class Ray {
	private Vec3 rayOrigin;
	private Vec3 rayDirection;
	private double entryPoint;
	private double exitPoint;
	public Ray(Vec3 rayOrigin, Vec3 rayDirection) {
		this.rayOrigin = rayOrigin;
		this.rayDirection = rayDirection;
	}
	public Ray(Vec3 rayOrigin, Vec3 rayDirection, double entryPoint, double exitPoint) {
		this.rayOrigin = rayOrigin;
		this.rayDirection = rayDirection;
		this.entryPoint = entryPoint;
		this.exitPoint = exitPoint;
	}
	public Vec3 getRayOrigin() {
		return rayOrigin;
	}
	public void setRayOrigin(Vec3 rayOrigin) {
		this.rayOrigin = rayOrigin;
	}
	public Vec3 getRayDirection() {
		return rayDirection;
	}
	public void setRayDirection(Vec3 rayDirection) {
		this.rayDirection = rayDirection;
	}
	public double getEntryPoint() {
		return entryPoint;
	}
	public void setEntryPoint(double entryPoint) {
		this.entryPoint = entryPoint;
	}
	public double getExitPoint() {
		return exitPoint;
	}
	public void setExitPoint(double exitPoint) {
		this.exitPoint = exitPoint;
	}
	private static double fresnel(double refractance, Vec3 normal, Vec3 point) {
		double f0 = Math.pow((refractance - 1), 2)/ Math.pow((refractance + 1), 2);
		return (f0 + (1 - f0)*Math.pow((1 - Vec3.dot(normal, point)), 5));
	}
	
	public static Vec3 trace(Scene scene, Ray ray, double depth) {
		Vec3 gray = new Vec3(127, 127, 127);
		double specularComponent = 0;
		boolean inShadow = false;
		double tNear = Double.MAX_VALUE;
		Surface closestSurface = new Sphere();
		for(int i = 0; i < scene.getSurfaces().size(); i++) {
			if(scene.getSurfaces().get(i).intersect(ray)){
				if (ray.getEntryPoint() < 0) ray.setEntryPoint(ray.getExitPoint());
				if (ray.getEntryPoint() < tNear) {
					tNear = ray.getEntryPoint();
					closestSurface = scene.getSurfaces().get(i);
				} 
			}
		}
		if (closestSurface.isNull()) return scene.getBackgroundColor();
		Vec3 pointOfIntersect = Vec3.add(ray.getRayOrigin(), Vec3.multiply(ray.getRayDirection(), tNear));
		Vec3 normalOfIntersect = null;
		Vec3 surfaceColor = new Vec3(0);
		Vec3 illumination = new Vec3(0);
		Vec3 ambientColor = new Vec3(0);
		if (closestSurface instanceof Sphere) {
			normalOfIntersect = Vec3.subtract(pointOfIntersect, closestSurface.getPosition());
		} else if (closestSurface instanceof Mesh) {
		 	normalOfIntersect = ((Mesh) closestSurface).getFaces().get((int)ray.getExitPoint()).getNormal();
		}
		normalOfIntersect = Vec3.normalize(normalOfIntersect);
		double bias = 0.00001;
		boolean inside = false;
		if (Vec3.dot(ray.getRayDirection(), ray.getRayDirection()) > 0) {
			normalOfIntersect = Vec3.subtract(new Vec3(0), normalOfIntersect);
			inside = true;
		}
		if (closestSurface instanceof Sphere) {
			if(((closestSurface.getTransmittance() > 0) && (closestSurface.getReflectance() > 0)) && (depth < scene.getCamera().getMaxBounces())) {
				Vec3 reflectionDirection = Vec3.multiply(Vec3.multiply(Vec3.subtract(ray.getRayDirection(),normalOfIntersect), 2), Vec3.dot(ray.getRayDirection(), normalOfIntersect));
				Vec3 reflection = Ray.trace(scene, new Ray(Vec3.add(pointOfIntersect, Vec3.multiply(normalOfIntersect, bias)), reflectionDirection), depth + 1);
				Vec3 refraction = null;
				if (closestSurface.getTransmittance() > 0) {
					double refractionCoefficient = 1.1;
					double eta = (inside) ? refractionCoefficient : 1/refractionCoefficient;
					double cosi = - Vec3.dot(normalOfIntersect, ray.getRayDirection());
					double k = 1 - eta * eta * (1 - cosi * cosi);
					Vec3 refractionDirection = Vec3.multiply(Vec3.multiply(Vec3.multiply(ray.getRayDirection(), eta), normalOfIntersect), (eta * cosi - Math.sqrt(k))); 
					refraction =  Ray.trace(scene, new Ray(Vec3.add(pointOfIntersect, Vec3.multiply(normalOfIntersect, bias)), refractionDirection), depth + 1);
				}
				surfaceColor = Vec3.add(reflection, Vec3.multiply(refraction, closestSurface.getColor())); 
			} else {
				for (int i = 0; i < scene.getLights().size(); i++) {
					illumination = Vec3.add(illumination, scene.getLights().get(i).illuminate(ray, closestSurface, normalOfIntersect, pointOfIntersect));
					specularComponent = specularComponent + scene.getLights().get(i).getSpecularComponent();
				}
				surfaceColor = Vec3.maxOne(Vec3.add(Vec3.multiply(closestSurface.getColor(), illumination), new Vec3(specularComponent)));
			}			
		} else if (closestSurface instanceof Mesh) {
			for (int i = 0; i < scene.getLights().size(); i++) {
				illumination = Vec3.add(illumination, scene.getLights().get(i).illuminate(ray, closestSurface, normalOfIntersect, pointOfIntersect));
				specularComponent = specularComponent + scene.getLights().get(i).getSpecularComponent();
				if (scene.getLights().get(i).getAmbientColor() != null) ambientColor = scene.getLights().get(i).getAmbientColor();
				inShadow = scene.getLights().get(i).inShadow(scene, ray, pointOfIntersect, normalOfIntersect);
			}
			if (!inShadow) surfaceColor = Vec3.maxOne(Vec3.add(Vec3.multiply(closestSurface.getColor(), illumination), new Vec3(specularComponent)));
			else surfaceColor = Vec3.multiply(closestSurface.getColor(), ambientColor);
			inShadow = false;
		}
		return surfaceColor;
	}
}
