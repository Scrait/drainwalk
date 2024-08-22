package com.tom.cpm.mixin;

import lombok.experimental.UtilityClass;

import net.minecraft.entity.player.PlayerEntity;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.tom.cpl.util.ItemSlot;
import com.tom.cpm.client.CustomPlayerModelsClient;
import com.tom.cpm.client.PlayerRenderManager;
import com.tom.cpm.shared.config.Player;
import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.model.render.ItemTransform;

@UtilityClass
public class ParrotVariantLayerController {

	public void onRenderPre(MatrixStack matrixStackIn, PlayerEntity entitylivingbaseIn, boolean leftShoulderIn) {
		matrixStackIn.push();
		Player<?> pl = CustomPlayerModelsClient.INSTANCE.manager.getBoundPlayer();
		if(pl != null) {
			ModelDefinition def = pl.getModelDefinition();
			if(def != null) {
				ItemTransform tr = def.getTransform(leftShoulderIn ? ItemSlot.LEFT_SHOULDER : ItemSlot.RIGHT_SHOULDER);
				if(tr != null) {
					PlayerRenderManager.multiplyStacks(tr.getMatrix(), matrixStackIn);
					if(entitylivingbaseIn.isCrouching())
						matrixStackIn.translate(0, -0.2f, 0);
				}
			}
		}
	}

	public void onRenderPost(MatrixStack matrixStackIn) {
		matrixStackIn.pop();
	}

}
