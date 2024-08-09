package tech.drainwalk.utils.cape;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class VanillaCapeRenderer implements CapeRenderer {

    public IVertexBuilder vertexConsumer = null;

    @Override
    public void render(AbstractClientPlayerEntity player, int part, ModelRenderer model, MatrixStack poseStack,
                       IRenderTypeBuffer multiBufferSource, int light, int overlay) {
        model.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
    }

    @Override
    public IVertexBuilder getVertexConsumer(IRenderTypeBuffer multiBufferSource, AbstractClientPlayerEntity player) {
        return multiBufferSource.getBuffer(RenderType.getEntityCutout(new ResourceLocation("drainwalk/images/cape.png")));
    }

    @Override
    public boolean vanillaUvValues() {
        return true;
    }

}