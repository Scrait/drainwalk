package com.tom.cpm.shared.model;

import com.tom.cpl.math.BoundingBox;
import com.tom.cpl.math.Rotation;
import com.tom.cpl.math.Vec3f;
import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.model.render.VanillaModelPart;

public class RootModelElement extends RenderedCube {
	private VanillaModelPart part;
	public Vec3f posN, defPos;
	public Rotation rotN, defRot;
	private ModelDefinition def;
	private boolean rotAdd, posAdd;
	public boolean disableVanilla;
	private boolean hidden;

	public RootModelElement(VanillaModelPart part, ModelDefinition def) {
		this.part = part;
		this.posN = new Vec3f();
		this.rotN = new Rotation();
		this.pos = new Vec3f();
		this.rotation = new Rotation();
		this.defPos = new Vec3f();
		this.defRot = new Rotation();
		this.def = def;
	}

	public VanillaModelPart getPart() {
		return part;
	}

	@Override
	public void reset() {
		this.pos = new Vec3f();
		this.rotation = new Rotation();
		this.renderScale = new Vec3f(1, 1, 1);
		display = !hidden;
		posAdd = true;
		rotAdd = true;
	}

	@Override
	public void setColor(float x, float y, float z) {}

	@Override
	public void setVisible(boolean v) {}

	@Override
	public int getId() {
		return part.getId(this);
	}

	@Override
	public BoundingBox getBounds() {
		PartValues v = part.getDefaultSize(def.getSkinType());
		float f = 0.001f;
		float g = f * 2;
		float scale = 1 / 16f;
		Vec3f offset = v.getOffset();
		Vec3f size = v.getSize();
		return BoundingBox.create(offset.x * scale - f, offset.y * scale - f, offset.z * scale - f,
				size.x * scale + g, size.y * scale + g, size.z * scale + g);
	}

	public void setPosAndRot(float px, float py, float pz, float rx, float ry, float rz) {
		if(!disableVanilla) {
			defPos.x = px + posN.x;
			defPos.y = py + posN.y;
			defPos.z = pz + posN.z;
			defRot.x = rx + rotN.x;
			defRot.y = ry + rotN.y;
			defRot.z = rz + rotN.z;
		} else {
			PartValues v = part.getDefaultSize(def.getSkinType());
			Vec3f p = v.getPos();
			defPos.x = p.x + posN.x;
			defPos.y = p.y + posN.y;
			defPos.z = p.z + posN.z;
			defRot.x = rotN.x;
			defRot.y = rotN.y;
			defRot.z = rotN.z;
		}
	}

	public void setPosAndRot(PartRoot cpy) {
		if(!disableVanilla) {
			RootModelElement e = cpy.getMainRoot();
			defPos.x = e.defPos.x - e.posN.x + posN.x;
			defPos.y = e.defPos.y - e.posN.y + posN.y;
			defPos.z = e.defPos.z - e.posN.z + posN.z;
			defRot.x = e.defRot.x - e.rotN.x + rotN.x;
			defRot.y = e.defRot.y - e.rotN.y + rotN.y;
			defRot.z = e.defRot.z - e.rotN.z + rotN.z;
		} else {
			PartValues v = part.getDefaultSize(def.getSkinType());
			Vec3f p = v.getPos();
			defPos.x = p.x + posN.x;
			defPos.y = p.y + posN.y;
			defPos.z = p.z + posN.z;
			defRot.x = rotN.x;
			defRot.y = rotN.y;
			defRot.z = rotN.z;
		}
	}

	@Override
	public void setPosition(boolean add, float x, float y, float z) {
		super.setPosition(add, x, y, z);
		posAdd &= add;
	}

	@Override
	public void setRotation(boolean add, float x, float y, float z) {
		super.setRotation(add, x, y, z);
		rotAdd &= add;
	}

	public Vec3f getPos() {
		if(posAdd) {
			return pos.add(defPos);
		} else {
			return pos;
		}
	}

	public Rotation getRot() {
		if(rotAdd) {
			return rotation.add(defRot);
		} else {
			return rotation;
		}
	}

	public boolean renderPart() {
		return true;
	}

	@Override
	public Vec3f getRenderScale() {
		return new Vec3f(1, 1, 1);
	}

	@Override
	public Vec3f getTransformPosition() {
		return getPos();
	}

	@Override
	public Rotation getTransformRotation() {
		return getRot();
	}

	@Override
	public void setHidden(boolean v) {
		hidden = v;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}
}
