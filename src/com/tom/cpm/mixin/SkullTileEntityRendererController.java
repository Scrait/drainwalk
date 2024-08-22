package com.tom.cpm.mixin;

import lombok.experimental.UtilityClass;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.util.ResourceLocation;

import com.mojang.authlib.GameProfile;

import com.tom.cpm.client.CustomPlayerModelsClient;
import com.tom.cpm.client.ModelTexture;
import com.tom.cpm.shared.model.TextureSheetType;

@UtilityClass
public class SkullTileEntityRendererController {

	public RenderType onGetRenderType(GenericHeadModel model, ResourceLocation resLoc) {
		ModelTexture mt = new ModelTexture(resLoc);
		CustomPlayerModelsClient.mc.getPlayerRenderManager().bindSkin(model, mt, TextureSheetType.SKIN);
		return mt.getRenderType();
	}

	public RenderType onGetRenderTypeNoSkin(GenericHeadModel model, ResourceLocation resLoc) {
		ModelTexture mt = new ModelTexture(resLoc);
		CustomPlayerModelsClient.mc.getPlayerRenderManager().bindSkin(model, mt, TextureSheetType.SKIN);
		return mt.getRenderType();
	}

	public void onRenderPre(GenericHeadModel model, SkullBlock.ISkullType skullType, GameProfile gameProfileIn, IRenderTypeBuffer buffer) {
		if (skullType == SkullBlock.Types.PLAYER && gameProfileIn != null) {
			CustomPlayerModelsClient.INSTANCE.renderSkull(model, gameProfileIn, buffer);
		}
	}

	public void onRenderPost(GenericHeadModel model, SkullBlock.ISkullType skullType, GameProfile gameProfileIn, IRenderTypeBuffer buffer) {
		if (skullType == SkullBlock.Types.PLAYER && gameProfileIn != null) {
			CustomPlayerModelsClient.INSTANCE.renderSkullPost(buffer, model);
		}
	}
}
