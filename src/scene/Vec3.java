package scene;

public class Vec3 {
	private double x;
	private double y;
	private double z;
	public Vec3() {
	}
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vec3(double a) {
		this.x = a;
		this.y = a;
		this.z = a;
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public double getZ() {
		return this.z;
	}
	public void setX(double num) {
		this.x = num;
	}
	public void setY(double num) {
		this.y = num;
	}
	public void setZ(double num) {
		this.z = num;
	}
	public String toString() {
		String result = "";
		result = "x = " + Double.toString(x);
		result += "\ny = " + Double.toString(y);
		result += "\nz = " + Double.toString(z);
		return result;
	}
	
	public Vec2 toVec2() {
		return new Vec2(this.x, this.y);
	}
	
	public static Vec3 cross(Vec3 a, Vec3 b) {
		double x = a.getY()*b.getZ() - a.getZ()*b.getY();
		double y = a.getZ()*b.getX() - a.getX()*b.getZ();
		double z = a.getX()*b.getY() - a.getY()*b.getX();
		return new Vec3(x, y, z);
 	}
	
	public static double dot(Vec3 a, Vec3 b) {
		return (a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ());
	}
	
	public static double lengthSquared(Vec3 a) {
		return (a.getX() * a.getX() + a.getY() * a.getY() + a.getZ() * a.getZ());
	}
	
	public static double length(Vec3 a) {
		return Math.sqrt(Vec3.lengthSquared(a));
	}
	
	public static Vec3 normalize(Vec3 a) {
		double length = Vec3.length(a);
		return new Vec3(a.getX()/length, a.getY()/length, a.getZ()/length);
	}
	
	public static Vec3 add(Vec3 a, Vec3 b) {
		return new Vec3(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
	}
	
	public static Vec3 subtract(Vec3 a, Vec3 b) {
		return new Vec3(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());

	}
	
	public static Vec3 multiply(Vec3 a, double s) {
		return new Vec3(a.getX() * s, a.getY() * s, a.getZ()*s);
	}
	
	public static Vec3 divide(Vec3 a, double s) {
		return new Vec3(a.getX() / s, a.getY() / s, a.getZ() / s);
	}
	
	public static Vec3 multiply(Vec3 a, Vec3 b) {
		return new Vec3(a.getX() * b.getX(), a.getY() * b.getY(), a.getZ() * b.getZ());
	}
	
	public static Vec3 divide(Vec3 a, Vec3 b) {
		return new Vec3(a.getX() / b.getX(), a.getY() / b.getY(), a.getZ() / b.getZ());
	}
	
	public static Vec3 pow(Vec3 a, Vec3 b) {
		return new Vec3(Math.pow(a.getX(), b.getX()), Math.pow(a.getY(), b.getY()), Math.pow(a.getZ(), b.getZ()));
	}
	
	public static Vec3 maxOne(Vec3 a) {
		return new Vec3(Math.min(a.getX(), 1.0), Math.min(a.getY(), 1.0), Math.min(a.getZ(), 1.0));
	}
	
}
