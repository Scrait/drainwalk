package com.tom.cpm.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.LivingEntity;

public interface LivingRendererAccess {
	RenderType cpm$onGetRenderType(LivingEntity player, boolean pTranslucent, boolean pGlowing, RenderType cbi);
}
