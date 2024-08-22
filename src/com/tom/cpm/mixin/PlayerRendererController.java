package com.tom.cpm.mixin;

import lombok.experimental.UtilityClass;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.tom.cpm.client.CustomPlayerModelsClient;
import com.tom.cpm.client.ModelTexture;
import com.tom.cpm.shared.config.Player;
import com.tom.cpm.shared.definition.ModelDefinitionLoader;
import com.tom.cpm.shared.model.TextureSheetType;

@UtilityClass
public class PlayerRendererController {

	public ResourceLocation onGetEntityTexture( BipedModel<?> entityModel, ResourceLocation cbi) {
		final ModelTexture modelTexture = new ModelTexture(cbi);
		CustomPlayerModelsClient.mc.getPlayerRenderManager().bindSkin(entityModel, modelTexture, TextureSheetType.SKIN);
		return modelTexture.getTexture();
	}

	public void onRenderRightArmPre(BipedModel<?> entityModel, IRenderTypeBuffer vertexConsumers) {
		CustomPlayerModelsClient.INSTANCE.renderHand(vertexConsumers, entityModel);
	}

	public void onRenderLeftArmPre(BipedModel<?> entityModel, IRenderTypeBuffer vertexConsumers) {
		CustomPlayerModelsClient.INSTANCE.renderHand(vertexConsumers, entityModel);
	}

	public void onRenderRightArmPost(BipedModel<?> entityModel, IRenderTypeBuffer vertexConsumers) {
		CustomPlayerModelsClient.INSTANCE.renderHandPost(vertexConsumers, entityModel);
	}

	public void onRenderLeftArmPost(BipedModel<?> entityModel, IRenderTypeBuffer vertexConsumers) {
		CustomPlayerModelsClient.INSTANCE.renderHandPost(vertexConsumers, entityModel);
	}

	public boolean onRenderName1() {
        return Player.isEnableNames();
    }

	public void onRenderName2(PlayerRenderer r, AbstractClientPlayerEntity entityIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if(Player.isEnableLoadingInfo())
			CustomPlayerModelsClient.INSTANCE.renderNameTag(r, entityIn, entityIn.getGameProfile(), ModelDefinitionLoader.PLAYER_UNIQUE, matrixStackIn, bufferIn, packedLightIn);
	}

	public RenderType getArmLayer(PlayerModel model, ResourceLocation locationIn) {
		return CustomPlayerModelsClient.mc.getPlayerRenderManager().isBound(model) ? RenderType.getEntityTranslucent(locationIn) : RenderType.getEntitySolid(locationIn);
	}

}
