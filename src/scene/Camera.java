package scene;

public class Camera {
	private Vec3 position;
	private Vec3 lookAt;
	private Vec3 up;
	private double horizontalFOV;
	private Vec2 resolution;
	private double maxBounces;
	public Camera(Vec3 position, Vec3 lookAt, Vec3 up, double horizontalFOV, Vec2 resolution, double maxBounces) {
		this.position = position;
		this.lookAt = lookAt;
		this.up = up;
		this.horizontalFOV = horizontalFOV;
		this.resolution = resolution;
		this.maxBounces = maxBounces;
	}
	
	public Vec3 getPosition() {
		return position;
	}

	public Vec3 getLookAt() {
		return lookAt;
	}

	public Vec3 getUp() {
		return up;
	}

	public double getHorizontalFOV() {
		return horizontalFOV;
	}

	public Vec2 getResolution() {
		return resolution;
	}

	public double getMaxBounces() {
		return maxBounces;
	}

	public String toString() {
		String result = "Camera:\nPosition: \n";
		result += position.toString();
		result += "\nLookAt: \n";
		result += lookAt.toString();
		result += "\nUp: \n";
		result += up.toString();
		result += "\nHorizontal FOV: \n";
		result += Double.toString(horizontalFOV);
		result += "\nResolution: \n";
		result += resolution.toString();
		result += "\nMax bounces: \n";
		result += Double.toString(maxBounces);
		return result;
	}
}
