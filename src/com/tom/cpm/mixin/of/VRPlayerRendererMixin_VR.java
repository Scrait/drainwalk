//package com.tom.cpm.mixin.of;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.vivecraft.render.VRPlayerModel;
//import org.vivecraft.render.VRPlayerRenderer;
//
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.entity.LivingRenderer;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.ITextComponent;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//
//import com.tom.cpm.client.ClientBase.PlayerNameTagRenderer;
//import com.tom.cpm.client.CustomPlayerModelsClient;
//import com.tom.cpm.client.ModelTexture;
//import com.tom.cpm.shared.config.Player;
//import com.tom.cpm.shared.definition.ModelDefinitionLoader;
//import com.tom.cpm.shared.model.TextureSheetType;
//
//@Mixin(VRPlayerRenderer.class)
//public abstract class VRPlayerRendererMixin_VR extends LivingRenderer<AbstractClientPlayerEntity, VRPlayerModel<AbstractClientPlayerEntity>> implements PlayerNameTagRenderer<AbstractClientPlayerEntity> {
//
//	public VRPlayerRendererMixin_VR(EntityRendererManager p_i50965_1_, VRPlayerModel<AbstractClientPlayerEntity> p_i50965_2_,
//			float p_i50965_3_) {
//		super(p_i50965_1_, p_i50965_2_, p_i50965_3_);
//	}
//
//	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FF"
//			+ "Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//			remap = false)
//	public void onRenderPre(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.bindPlayer(entityIn, bufferIn, getModel());
//	}
//
//	@Inject(at = @At("RETURN"), method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FF"
//			+ "Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//			remap = false)
//	public void onRenderPost(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.unbindClear(getModel());
//	}
//
//	@Inject(
//			at = @At("RETURN"),
//			method = {
//					"getEntityTexture(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)Lnet/minecraft/util/ResourceLocation;"
//			},
//			cancellable = true, remap = false)
//	public void onGetEntityTexture(AbstractClientPlayerEntity entity, CallbackInfoReturnable<ResourceLocation> cbi) {
//		CustomPlayerModelsClient.mc.getPlayerRenderManager().bindSkin(getModel(), new ModelTexture(cbi), TextureSheetType.SKIN);
//	}
//
//	@Redirect(at =
//			@At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;"
//							+ "getSkinTextureLocation()Lnet/minecraft/util/ResourceLocation;",
//							remap = true
//					),
//			method = "renderItem("
//					+ "Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;"
//					+ "ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;"
//					+ "Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;)V",
//					remap = false
//			)
//	public ResourceLocation getSkinTex(AbstractClientPlayerEntity player) {
//		return getTextureLocation(player);
//	}
//
//	@Inject(at = @At("HEAD"), method = {
//			"renderRightArm(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V"
//	}, remap = false)
//	public void onRenderRightArmPre(MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.bindHand(player, vertexConsumers, getModel());
//	}
//
//	@Inject(at = @At("HEAD"), method = {
//			"renderLeftArm(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V"
//	}, remap = false)
//	public void onRenderLeftArmPre(MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.bindHand(player, vertexConsumers, getModel());
//	}
//
//	@Inject(at = @At("RETURN"), method = {
//			"renderRightArm(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V"
//	}, remap = false)
//	public void onRenderRightArmPost(MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.unbindClear(getModel());
//	}
//
//	@Inject(at = @At("RETURN"), method = {
//			"renderLeftArm(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V"
//	}, remap = false)
//	public void onRenderLeftArmPost(MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.unbindClear(getModel());
//	}
//
//	@Inject(at = @At("HEAD"), method = {
//			"renderNameTag(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//			"func_225629_a_(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//			"renderName(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"
//	}, cancellable = true, remap = false)
//	public void onRenderName1(AbstractClientPlayerEntity entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo cbi) {
//		if(!Player.isEnableNames())cbi.cancel();
//	}
//
//	@Inject(at = @At(value = "INVOKE",
//			target = "Lnet/minecraft/client/renderer/entity/LivingRenderer;renderNameTag(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//			ordinal = 1, remap = true),
//			method = {
//					"renderNameTag(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//					"func_225629_a_(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
//					"renderName(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/text/ITextComponent;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"
//	}, remap = false)
//	public void onRenderName2(AbstractClientPlayerEntity entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo cbi) {
//		if(Player.isEnableLoadingInfo())
//			CustomPlayerModelsClient.INSTANCE.renderNameTag(this, entityIn, entityIn.getGameProfile(), ModelDefinitionLoader.PLAYER_UNIQUE, matrixStackIn, bufferIn, packedLightIn);
//	}
//
//	@Redirect(at =
//			@At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/client/renderer/RenderType;entitySolid("
//							+ "Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;",
//							remap = true
//					),
//			method = "renderItem("
//					+ "Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;"
//					+ "ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;"
//					+ "Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;)V",
//					require = 0,
//					remap = false
//			)
//	public RenderType getArmLayer(ResourceLocation loc, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, ModelRenderer rendererArmIn, ModelRenderer rendererArmwearIn) {
//		return CustomPlayerModelsClient.mc.getPlayerRenderManager().isBound(getModel()) ? RenderType.entityTranslucent(getTextureLocation(playerIn)) : RenderType.entitySolid(getTextureLocation(playerIn));
//	}
//
//	@Override
//	public void cpm$renderNameTag(AbstractClientPlayerEntity entityIn, ITextComponent displayNameIn,
//			MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
//		super.renderNameTag(entityIn, displayNameIn, matrixStackIn, bufferIn, packedLightIn);
//	}
//
//	@Override
//	public EntityRendererManager cpm$entityRenderDispatcher() {
//		return entityRenderDispatcher;
//	}
//}
