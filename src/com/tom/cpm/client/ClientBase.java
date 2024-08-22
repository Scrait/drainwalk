package com.tom.cpm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import com.tom.cpl.text.FormatText;
import com.tom.cpm.CustomPlayerModels;
import com.tom.cpm.common.PlayerAnimUpdater;
import com.tom.cpm.mixinplugin.IrisDetector;
import com.tom.cpm.mixinplugin.OFDetector;
import com.tom.cpm.mixinplugin.VRDetector;
import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.model.RenderManager;
import com.tom.cpm.shared.network.NetHandler;
import com.tom.cpm.shared.util.Log;

import io.netty.buffer.Unpooled;

public class ClientBase {
	public static final ResourceLocation DEFAULT_CAPE = new ResourceLocation("cpm:textures/template/cape.png");
	public static boolean optifineLoaded, vrLoaded, irisLoaded;
	public static MinecraftObject mc;
	public Minecraft minecraft;
	public RenderManager<GameProfile, PlayerEntity, Model, IRenderTypeBuffer> manager;
	public NetHandler<ResourceLocation, PlayerEntity, ClientPlayNetHandler> netHandler;

	public void init0() {
		minecraft = Minecraft.getInstance();
		mc = new MinecraftObject(minecraft);
		optifineLoaded = OFDetector.doApply();
		vrLoaded = VRDetector.doApply();
		irisLoaded = IrisDetector.doApply();
		if(optifineLoaded)Log.info("Optifine detected, enabling optifine compatibility");
		if(vrLoaded)Log.info("ViveCraft detected, enabling ViveCraft compatibility");
		if(irisLoaded)Log.info("Iris detected, enabling iris compatibility");
	}

	public void init1() {
		manager = new RenderManager<>(mc.getPlayerRenderManager(), mc.getDefinitionLoader(), PlayerEntity::getGameProfile);
		manager.setGPGetters(GameProfile::getProperties, Property::getValue);
		netHandler = new NetHandler<>(ResourceLocation::new);
		netHandler.setExecutor(() -> minecraft);
		netHandler.setSendPacketClient(d -> new PacketBuffer(Unpooled.wrappedBuffer(d)), (c, rl, pb) -> c.sendPacket(new CCustomPayloadPacket(rl, pb)));
		netHandler.setPlayerToLoader(PlayerEntity::getGameProfile);
		netHandler.setGetPlayerById(id -> {
			Entity ent = Minecraft.getInstance().world.getEntityByID(id);
			if(ent instanceof PlayerEntity) {
				return (PlayerEntity) ent;
			}
			return null;
		});
		netHandler.setGetClient(() -> minecraft.player);
		netHandler.setGetNet(c -> ((ClientPlayerEntity)c).connection);
		netHandler.setDisplayText(f -> minecraft.player.sendStatusMessage(f.remap(), false));
		netHandler.setGetPlayerAnimGetters(new PlayerAnimUpdater());
	}

	public static void apiInit() {
		CustomPlayerModels.api.buildClient().voicePlayer(PlayerEntity.class, PlayerEntity::getUniqueID).
		renderApi(Model.class, ResourceLocation.class, RenderType.class, IRenderTypeBuffer.class, GameProfile.class, ModelTexture::new).
		localModelApi(GameProfile::new).init();
	}

	public void playerRenderPre(PlayerEntity player, IRenderTypeBuffer buffer, BipedModel<?>  model) {
		manager.bindPlayer(player, buffer, model);
	}

	public void playerRenderPost(IRenderTypeBuffer buffer, BipedModel<?>  model) {
		if(buffer instanceof IRenderTypeBuffer.Impl)((IRenderTypeBuffer.Impl)buffer).finish();
		manager.unbindClear(model);
	}

	public void renderHand(IRenderTypeBuffer buffer, BipedModel<?> model) {
		manager.bindHand(Minecraft.getInstance().player, buffer, model);
	}

	public void renderHandPost(IRenderTypeBuffer buffer, BipedModel<?> model) {
		if(buffer instanceof IRenderTypeBuffer.Impl)((IRenderTypeBuffer.Impl)buffer).finish();
		manager.unbindClear(model);
	}

	public void renderSkull(Model skullModel, GameProfile profile, IRenderTypeBuffer buffer) {
		manager.bindSkull(profile, buffer, skullModel);
	}

	public void renderSkullPost(IRenderTypeBuffer buffer, Model model) {
		if(buffer instanceof IRenderTypeBuffer.Impl)((IRenderTypeBuffer.Impl)buffer).finish();
		manager.unbindFlush(model);
	}

	public void renderElytra(BipedModel<LivingEntity> player, ElytraModel<LivingEntity> model) {
		manager.bindElytra(player, model);
	}

	public void renderArmor(BipedModel<LivingEntity> modelArmor, BipedModel<LivingEntity> modelLeggings,
			BipedModel<LivingEntity> player) {
		manager.bindArmor(player, modelArmor, 1);
		manager.bindArmor(player, modelLeggings, 2);
	}

	public void updateJump() {
		if(minecraft.player.isOnGround() && minecraft.player.movementInput.jump) {
			manager.jump(minecraft.player);
		}
	}

	//Copy from CapeLayer
	public static void renderCape(MatrixStack matrixStackIn, IVertexBuilder buffer, int packedLightIn,
			AbstractClientPlayerEntity playerIn, float partialTicks, PlayerModel<AbstractClientPlayerEntity> model,
			ModelDefinition modelDefinition) {
		matrixStackIn.push();

		float f1, f2, f3;

		if(playerIn != null) {
			double d0 = MathHelper.lerp((double)partialTicks, playerIn.prevChasingPosX, playerIn.chasingPosX) - MathHelper.lerp((double)partialTicks, playerIn.prevPosX, playerIn.getPosX());
			double d1 = MathHelper.lerp((double)partialTicks, playerIn.prevChasingPosY, playerIn.chasingPosY) - MathHelper.lerp((double)partialTicks, playerIn.prevPosY, playerIn.getPosY());
			double d2 = MathHelper.lerp((double)partialTicks, playerIn.prevChasingPosZ, playerIn.chasingPosZ) - MathHelper.lerp((double)partialTicks, playerIn.prevPosZ, playerIn.getPosZ());
			float f = playerIn.prevRenderYawOffset + (playerIn.renderYawOffset - playerIn.prevRenderYawOffset);
			double d3 = (double)MathHelper.sin(f * ((float)Math.PI / 180F));
			double d4 = (double)(-MathHelper.cos(f * ((float)Math.PI / 180F)));
			f1 = (float) d1 * 10.0F;
			f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
			f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
			f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
			f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
			f3 = MathHelper.clamp(f3, -20.0F, 20.0F);
			if (f2 < 0.0F) {
				f2 = 0.0F;
			}

			float f4 = MathHelper.lerp(partialTicks, playerIn.prevCameraYaw, playerIn.cameraYaw);
			f1 = f1 + MathHelper.sin(MathHelper.lerp(partialTicks, playerIn.prevDistanceWalkedModified, playerIn.distanceWalkedModified) * 6.0F) * 32.0F * f4;

			if (playerIn.isCrouching()) {
				f1 += 25.0F;
			}
			if (playerIn.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
				if (playerIn.isCrouching()) {
					model.bipedCape.rotationPointZ = 1.4F + 0.125F * 3;
					model.bipedCape.rotationPointY = 1.85F + 1 - 0.125F * 4;
				} else {
					model.bipedCape.rotationPointZ = 0.0F + 0.125F * 16f;
					model.bipedCape.rotationPointY = 0.0F;
				}
			} else if (playerIn.isCrouching()) {
				model.bipedCape.rotationPointZ = 0.3F + 0.125F * 16f;
				model.bipedCape.rotationPointY = 0.8F + 0.3f;
			} else {
				model.bipedCape.rotationPointZ = -1.1F + 0.125F * 32f;
				model.bipedCape.rotationPointY = -0.85F + 1;
			}
		} else {
			f1 = 0;
			f2 = 0;
			f3 = 0;
		}
		model.bipedCape.rotateAngleX = (float) -Math.toRadians(6.0F + f2 / 2.0F + f1);
		model.bipedCape.rotateAngleY = (float) Math.toRadians(180.0F - f3 / 2.0F);
		model.bipedCape.rotateAngleZ = (float) Math.toRadians(f3 / 2.0F);
		mc.getPlayerRenderManager().setModelPose(model);
		model.bipedCape.rotateAngleX = 0;
		model.bipedCape.rotateAngleY = 0;
		model.bipedCape.rotateAngleZ = 0;
		model.renderCape(matrixStackIn, buffer, packedLightIn, OverlayTexture.NO_OVERLAY);
		matrixStackIn.pop();
	}

	public static interface PlayerNameTagRenderer<E extends Entity> {
		void cpm$renderNameTag(E entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn);
		EntityRendererManager cpm$entityRenderDispatcher();
	}

	public <E extends Entity> void renderNameTag(PlayerNameTagRenderer<E> r, E entityIn, GameProfile gprofile, String unique, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		double d0 = r.cpm$entityRenderDispatcher().squareDistanceTo(entityIn);
		if (d0 < 32*32) {
			FormatText st = manager.getStatus(gprofile, unique);
			if(st != null) {
				matrixStackIn.push();
				matrixStackIn.translate(0.0D, 1.3F, 0.0D);
				matrixStackIn.scale(0.5f, 0.5f, 0.5f);
				r.cpm$renderNameTag(entityIn, st.remap(), matrixStackIn, bufferIn, packedLightIn);
				matrixStackIn.pop();
			}
		}
	}
}
