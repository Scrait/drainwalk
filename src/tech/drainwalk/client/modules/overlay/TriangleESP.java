package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.utils.render.RenderUtils;

public class TriangleESP extends Module {

    public TriangleESP() {
        super("TriangleESP", Category.OVERLAY);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
//        GLUtils.INSTANCE.rescale(2);
//        for (Entity entity : mc.world.getAllEntities()) {
//            if (entity == mc.player) continue;
//            if (!(entity instanceof PlayerEntity)) continue;
//            double x = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * mc.getTimer().renderPartialTicks - mc.getRenderManager().info.getProjectedView().x;
//            double z = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * mc.getTimer().renderPartialTicks - mc.getRenderManager().info.getProjectedView().z;
//            double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
//            double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
//            double rotY = -(z * cos - x * sin);
//            double rotX = -(x * cos + z * sin);
//
//            float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
//            float cos2 = MathHelper.cos((float) Math.toRadians(angle));
//            float sin2 = MathHelper.sin((float) Math.toRadians(angle));
//            float centerX = mc.getMainWindow().getWidth() / 4f;
//            float centerY = mc.getMainWindow().getHeight() / 4f;
//
//            GlStateManager.pushMatrix();
//            GlStateManager.translatef(centerX + 26 * cos2, centerY + 26 * sin2, 0);
//            GlStateManager.rotatef(angle + 90, 0, 0, 1);
//            RenderUtils.drawTriangleCustom(dw.getApiMain().getFriendManager().existsByName(entity.getName().getString()) ? ClientColor.main : ClientColor.panelMain);
//            GlStateManager.popMatrix();
//        }
//        GLUtils.INSTANCE.rescaleMC();
    }
}
