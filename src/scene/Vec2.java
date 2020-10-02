package scene;

public class Vec2 {
	private double h;
	private double v;
	public Vec2() {
	}
	public Vec2(double h, double v) {
		this.h = h;
		this.v = v;
	}
	public double getH() {
		return this.h;
	}
	public double getV() {
		return this.v;
	}
	public void setH(double num) {
		this.h = num;
	}
	public void setV(double num) {
		this.v = num;
	}
	public String toString() {
		String result = "";
		result = "h = " + Double.toString(h);
		result += "\nv = " + Double.toString(v);
		return result;
	}
	public Vec3 toVec3() {
		return new Vec3(this.h, this.v, 0);
	}
}
