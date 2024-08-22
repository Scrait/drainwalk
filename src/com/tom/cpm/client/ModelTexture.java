package com.tom.cpm.client;

import java.util.function.Function;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;

import com.tom.cpl.render.RenderTypeBuilder.TextureHandler;

public class ModelTexture implements TextureHandler<ResourceLocation, RenderType> {
	private ResourceLocation texture;
	private Function<ResourceLocation, RenderType> renderType;

	public ModelTexture(ResourceLocation texture,
			Function<ResourceLocation, RenderType> renderType) {
		this.texture = texture;
		this.renderType = renderType;
	}

	public ModelTexture(ResourceLocation texture) {
		this(texture, PlayerRenderManager.entity);
	}


	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	@Override
	public RenderType getRenderType() {
		return renderType.apply(getTexture());
	}
}
