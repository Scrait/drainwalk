package tech.drainwalk.services.lootbeams;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

public class LootBeamController implements IInstanceAccess {

    public static void onRenderNameplate(MatrixStack matrixStack, IRenderTypeBuffer renderType, float partialTicks, Entity entity) {
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            if (mc.player.getDistanceSq(itemEntity) > 24 * 24) {
                return;
            }

            boolean shouldRender = true;
            if (shouldRender) {
                LootBeamRenderer.renderLootBeam(matrixStack, renderType, partialTicks, mc.world.getGameTime(), itemEntity);
            }
        }
    }

}
