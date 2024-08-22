package com.tom.cpm.mixin;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import com.tom.cpm.client.CustomPlayerModelsClient;
import com.tom.cpm.client.ModelTexture;
import com.tom.cpm.shared.config.Player;
import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.model.RootModelType;
import com.tom.cpm.shared.model.TextureSheetType;

@UtilityClass
public class CapeLayerController {

	public boolean onRender(BipedModel<?> entityModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
							AbstractClientPlayerEntity entitylivingbaseIn, float partialTicks) {
		Player<?> pl = CustomPlayerModelsClient.INSTANCE.manager.getBoundPlayer();
		if(pl != null) {
			ModelDefinition def = pl.getModelDefinition();
			if(def != null && def.hasRoot(RootModelType.CAPE)) {
				ItemStack chestplate = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
				if(!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(PlayerModelPart.CAPE) && chestplate.getItem() != Items.ELYTRA) {
					ResourceLocation defLoc = entitylivingbaseIn.getLocationCape();
					if(defLoc == null)defLoc = CustomPlayerModelsClient.DEFAULT_CAPE;
					ModelTexture mt = new ModelTexture(defLoc);
					CustomPlayerModelsClient.mc.getPlayerRenderManager().rebindModel(entityModel);
					CustomPlayerModelsClient.mc.getPlayerRenderManager().bindSkin(entityModel, mt, TextureSheetType.CAPE);
					if(mt.getTexture() != null) {
						IVertexBuilder buffer = bufferIn.getBuffer(mt.getRenderType());
//						CustomPlayerModelsClient.renderCape(matrixStackIn, buffer, packedLightIn, entitylivingbaseIn, partialTicks, entityModel, def);
					}
				}
				return false;
			}
		}
        return true;
	}
}
