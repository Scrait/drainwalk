package tech.drainwalk.services.cape;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;

public interface CapeRenderer {

    public void render(AbstractClientPlayerEntity player, int part, ModelRenderer model, MatrixStack poseStack, IRenderTypeBuffer multiBufferSource, int light, int overlay);

    public default IVertexBuilder getVertexConsumer(IRenderTypeBuffer multiBufferSource, AbstractClientPlayerEntity player) {
        return null;
    }

    public boolean vanillaUvValues();

}