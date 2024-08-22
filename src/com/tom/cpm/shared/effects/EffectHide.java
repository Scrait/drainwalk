package com.tom.cpm.shared.effects;

import java.io.IOException;

import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.io.IOHelper;
import com.tom.cpm.shared.model.RenderedCube;

@Deprecated
public class EffectHide implements IRenderEffect {
	private int id;

	public EffectHide() {
	}

	public EffectHide(int id) {
		this.id = id;
	}

	@Override
	public void load(IOHelper in) throws IOException {
		id = in.readVarInt();
	}

	@Override
	public void write(IOHelper out) throws IOException {
		out.writeVarInt(id);
	}

	@Override
	public void apply(ModelDefinition def) {
		RenderedCube cube = def.getElementById(id);
		if(cube != null) {
			cube.setHidden(true);
		}
	}

	@Override
	public RenderEffects getEffect() {
		return RenderEffects.HIDE;
	}

	@Override
	public String toString() {
		return "Hide [" + id + "]";
	}
}
