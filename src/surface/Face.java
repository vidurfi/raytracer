package surface;

import scene.Vec2;
import scene.Vec3;

public class Face {
	private Vec3 a;
	private Vec3 b;
	private Vec3 c;
	private Vec2 textureA;
	private Vec2 textureB;
	private Vec2 textureC;
	private Vec3 normal;
	
	public Face(Vec3 a, Vec3 b, Vec3 c, Vec2 textureA, Vec2 textureB, Vec2 textureC, Vec3 normal) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.textureA = textureA;
		this.textureB = textureB;
		this.textureC = textureC;
		this.normal = normal;
	}
	public Vec3 getA() {
		return a;
	}
	public Vec3 getB() {
		return b;
	}
	public Vec3 getC() {
		return c;
	}
	public Vec2 getTextureA() {
		return textureA;
	}
	public Vec2 getTextureB() {
		return textureB;
	}
	public Vec2 getTextureC() {
		return textureC;
	}
	public Vec3 getNormal() {
		return normal;
	}
	public void setA(Vec3 a) {
		this.a = a;
	}
	public void setB(Vec3 b) {
		this.b = b;
	}
	public void setC(Vec3 c) {
		this.c = c;
	}
	public void setNormal(Vec3 normal) {
		this.normal = normal;
	}
	@Override
	public String toString() {
		return "Face [a=" + a + ", b=" + b + ", c=" + c + ", textureA=" + textureA + ", textureB=" + textureB
				+ ", textureC=" + textureC + ", normal=" + normal + "]";
	}
		
}
