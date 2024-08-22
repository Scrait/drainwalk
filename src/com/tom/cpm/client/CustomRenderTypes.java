package com.tom.cpm.client;

import java.util.OptionalDouble;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class CustomRenderTypes extends RenderType {
	private static final RenderType LINES_NO_DEPTH = makeType("cpm:lines_no_depth", DefaultVertexFormats.POSITION_COLOR, 1, 256, RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.empty())).layer(field_239235_M_).transparency(TRANSLUCENT_TRANSPARENCY).target(field_241712_U_).depthTest(DEPTH_ALWAYS).writeMask(COLOR_DEPTH_WRITE).build(false));

	private CustomRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn,
			boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType entityColorTranslucent() {
		return getEntityTranslucent(Platform.WHITE);
	}

	public static RenderType glowingEyesColor() {
		return glowingEyes(Platform.WHITE);
	}

	public static RenderType linesNoDepth() {
		return LINES_NO_DEPTH;
	}

	public static RenderType glowingEyes(ResourceLocation rl) {
		RenderType rt = RenderType.getEyes(rl);
//		if (ClientBase.irisLoaded)
//			((BlendingStateHolderEx) rt).setTransparencyType(TransparencyType.DECAL);
		return rt;
	}
}
