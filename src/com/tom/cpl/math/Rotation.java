package com.tom.cpl.math;

import java.util.HashMap;
import java.util.Map;

import com.tom.cpl.math.Quaternion.RotationOrder;

public class Rotation {
	public float x;
	public float y;
	public float z;

	public Rotation(Vec3f zyx, boolean degrees) {
		x = zyx.x;
		y = zyx.y;
		z = zyx.z;

		if (degrees) {
			toRadians();
		}
	}

	private void toRadians() {
		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);
	}

	public Rotation() {
	}

	public Rotation(float x, float y, float z, boolean degrees) {
		this.x = x;
		this.y = y;
		this.z = z;

		if (degrees) {
			toRadians();
		}
	}

	public Rotation add(Rotation v) {
		return new Rotation(x + v.x, y + v.y, z + v.z, false);
	}

	public Vec3f asVec3f(boolean degrees) {
		if (degrees)return new Vec3f((float) Math.toDegrees(x), (float) Math.toDegrees(y), (float) Math.toDegrees(z));
		return new Vec3f(x, y, z);
	}

	public Quaternion asQ() {
		return new Quaternion(x, y, z, RotationOrder.ZYX, false);
	}

	public Map<String, Object> toMap3() {
		Map<String, Object> map = new HashMap<>();
		map.put("x", Math.toDegrees(x));
		map.put("y", Math.toDegrees(y));
		map.put("z", Math.toDegrees(z));
		return map;
	}
}
