package com.tom.cpl.math;

import java.util.HashMap;
import java.util.Map;

import com.tom.cpm.shared.editor.project.JsonMap;

public class Vec3f {
	public static final Vec3f ZERO = new Vec3f(0.0F, 0.0F, 0.0F);
	public static final Vec3f NEGATIVE_X = new Vec3f(-1.0F, 0.0F, 0.0F);
	public static final Vec3f POSITIVE_X = new Vec3f(1.0F, 0.0F, 0.0F);
	public static final Vec3f NEGATIVE_Y = new Vec3f(0.0F, -1.0F, 0.0F);
	public static final Vec3f POSITIVE_Y = new Vec3f(0.0F, 1.0F, 0.0F);
	public static final Vec3f NEGATIVE_Z = new Vec3f(0.0F, 0.0F, -1.0F);
	public static final Vec3f POSITIVE_Z = new Vec3f(0.0F, 0.0F, 1.0F);
	public static final int MAX_POS = 3 * 16;
	public float x, y, z;

	public Vec3f() {
	}

	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3f(Vec3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public Vec3f(Vec2f v, float z) {
		x = v.x;
		y = v.y;
		this.z = z;
	}

	public Vec3f(JsonMap m, Vec3f def) {
		this(def);
		if(m == null)return;
		x = m.getFloat("x");
		y = m.getFloat("y");
		z = m.getFloat("z");
	}

	public Vec3f(Map<String, Object> m, Vec3f def) {
		this(def);
		if(m == null)return;
		x = ((Number)m.get("x")).floatValue();
		y = ((Number)m.get("y")).floatValue();
		z = ((Number)m.get("z")).floatValue();
	}

	public Vec3f mul(float v) {
		return new Vec3f(x*v, y*v, z*v);
	}

	public Vec3f add(Vec3f v) {
		return new Vec3f(x + v.x, y + v.y, z + v.z);
	}

	public Vec3f sub(Vec3f v) {
		return new Vec3f(x - v.x, y - v.y, z - v.z);
	}

	public Vec3f sub(float v) {
		return new Vec3f(x - v, y - v, z - v);
	}

	public float getYaw() {
		float norm;

		norm = (float) (1.0/Math.sqrt(this.x*this.x + this.z*this.z));
		float x = this.x * norm;//dot
		float y = this.z * norm;

		return (float) Math.acos( x ) * Math.signum(y);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("x", x);
		map.put("y", y);
		map.put("z", z);
		return map;
	}

	@Override
	public String toString() {
		return String.format("Vec3[%.3f, %.3f, %.3f]", x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vec3f other = (Vec3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) return false;
		return true;
	}

	public void round(int i) {
		x = Math.round(x * i) / (float) i;
		y = Math.round(y * i) / (float) i;
		z = Math.round(z * i) / (float) i;
	}

	public Quaternion getDegreesQuaternion(float angle) {
		return new Quaternion(this, angle, true);
	}

	public Quaternion getRadialQuaternion(float angle) {
		return new Quaternion(this, angle, false);
	}

	public void mul(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
	}

	public Vec3f copy() {
		return new Vec3f(this);
	}

	public void transform(Mat3f matrixIn) {
		float f = this.x;
		float f1 = this.y;
		float f2 = this.z;
		this.x = matrixIn.m00 * f + matrixIn.m01 * f1 + matrixIn.m02 * f2;
		this.y = matrixIn.m10 * f + matrixIn.m11 * f1 + matrixIn.m12 * f2;
		this.z = matrixIn.m20 * f + matrixIn.m21 * f1 + matrixIn.m22 * f2;
	}

	public void normalize() {
		float invLength = 1.0f / MathHelper.sqrt(x * x + y * y + z * z);
		x *= invLength;
		y *= invLength;
		z *= invLength;
	}

	public boolean epsilon(float e) {
		return Math.abs(x) < e && Math.abs(y) < e && Math.abs(z) < e;
	}
}