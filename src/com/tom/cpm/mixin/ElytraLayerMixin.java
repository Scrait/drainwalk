//package com.tom.cpm.mixin;
//
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.IEntityRenderer;
//import net.minecraft.client.renderer.entity.layers.ElytraLayer;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.client.renderer.entity.model.BipedModel;
//import net.minecraft.client.renderer.entity.model.ElytraModel;
//import net.minecraft.client.renderer.entity.model.EntityModel;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.util.ResourceLocation;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//
//import com.tom.cpm.client.CustomPlayerModelsClient;
//import com.tom.cpm.client.ModelTexture;
//import com.tom.cpm.client.PlayerRenderManager;
//import com.tom.cpm.shared.model.TextureSheetType;
//
//@Mixin(ElytraLayer.class)
//public abstract class ElytraLayerMixin extends LayerRenderer<LivingEntity, EntityModel<LivingEntity>> {
//	public ElytraLayerMixin(IEntityRenderer<LivingEntity, EntityModel<LivingEntity>> p_i50926_1_) {
//		super(p_i50926_1_);
//	}
//
//	private @Shadow @Final ElytraModel<LivingEntity> elytraModel;
//
//	@Inject(at = @At(
//			value = "INVOKE",
//			target = "Lcom/mojang/blaze3d/matrix/MatrixStack;pushPose()V"),
//			method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I"
//					+ "Lnet/minecraft/entity/LivingEntity;FFFFFF)V")
//	public void preRender(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo cbi) {
//		if(getParentModel() instanceof BipedModel) {
//			CustomPlayerModelsClient.INSTANCE.renderElytra((BipedModel<LivingEntity>) getParentModel(), elytraModel);
//		}
//	}
//
//	@Inject(at = @At(
//			value = "INVOKE",
//			target = "Lcom/mojang/blaze3d/matrix/MatrixStack;popPose()V"),
//			method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I"
//					+ "Lnet/minecraft/entity/LivingEntity;FFFFFF)V")
//	public void postRender(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo cbi) {
//		CustomPlayerModelsClient.INSTANCE.manager.unbind(elytraModel);
//	}
//
//	@Redirect(at = @At(
//			value = "INVOKE",
//			target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull("
//					+ "Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;",
//					ordinal = 0
//			),
//			method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I"
//					+ "Lnet/minecraft/entity/LivingEntity;FFFFFF)V")
//	private RenderType onGetRenderTypeNoSkin(ResourceLocation resLoc, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//		if(getParentModel() instanceof BipedModel) {
//			ModelTexture mt = new ModelTexture(resLoc, PlayerRenderManager.armor);
//			CustomPlayerModelsClient.mc.getPlayerRenderManager().bindSkin(elytraModel, mt, TextureSheetType.ELYTRA);
//			return mt.getRenderType();
//		}
//		return RenderType.armorCutoutNoCull(resLoc);
//	}
//}
