package com.tom.cpm.shared.effects;

import java.io.IOException;

import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.io.IOHelper;
import com.tom.cpm.shared.model.RenderedCube;
import com.tom.cpm.shared.model.render.PerFaceUV;

public class EffectPerFaceUV implements IRenderEffect {
	private int id;
	private PerFaceUV uv;

	public EffectPerFaceUV() {
	}

	public EffectPerFaceUV(int id, PerFaceUV uv) {
		this.id = id;
		this.uv = uv;
	}

	@Override
	public void load(IOHelper in) throws IOException {
		id = in.readVarInt();
		uv = new PerFaceUV();
		uv.readFaces(in);
	}

	@Override
	public void write(IOHelper out) throws IOException {
		out.writeVarInt(id);
		uv.writeFaces(out);
	}

	@Override
	public void apply(ModelDefinition def) {
		RenderedCube cube = def.getElementById(id);
		if(cube != null) {
			cube.faceUVs = uv;
		}
	}

	@Override
	public RenderEffects getEffect() {
		return RenderEffects.PER_FACE_UV;
	}

	@Override
	public String toString() {
		return "Per Face UV [" + id + "] " + uv;
	}
}
