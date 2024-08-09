package tech.drainwalk.utils.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.utils.Utils;
import tech.drainwalk.services.render.ColorService;

import java.util.ArrayList;
import java.util.List;

public class BackTrackUtils extends Utils {
    public final Entity entity;
    public final List<Point> positions = new ArrayList<>();
    public BackTrackUtils(Entity entityCoord) {
        this.entity = entityCoord;
    }

    public void tick() {
//
//        if (entity instanceof ClientPlayerEntity) return;
//
//        if (Math.abs(entity.getPosX() - entity.prevPosX) > 0 || Math.abs(entity.getPosZ() - entity.prevPosZ) > 0 || Math.abs(entity.getPosY() - entity.prevPosY) > 0) {
//            final Point p = new Point(entity.getPositionVec().add(-.5, 0, -0.5));
//            p.yaw = (int) entity.rotationYaw;
//            positions.add(p);
//
//        }
//        if (positions.size() > Drainwalk.getInstance().getModuleManager().backTrack.range.getValue()) {
//            positions.remove(0);
//        }
//        positions.removeIf(point -> point.ticks > Drainwalk.getInstance().getModuleManager().backTrack.range.getValue());
    }

//    public void click() {
//        for (Point point : positions) {
//            point.ticks++;
//            if (entity.getDistance(positions.get(0).vec3d.x, positions.get(0).vec3d.y, positions.get(0).vec3d.z) == 0.7071067690849304)
//                continue;
//            if (positions.size() > 0) {
//                final Vector3d src = mc.player.getEyePosition(mc.getRenderPartialTicks());
//                final Vector3d vectorForRotation = mc.player.getLookVec();
//                final Vector3d dest = src.add(vectorForRotation.x * 3.1, vectorForRotation.y * 3.1, vectorForRotation.z * 3.1);
//                final RayTraceResult rayTraceResult = new AxisAlignedBB(point.vec3d.x + 0.8, point.vec3d.y, point.vec3d.z + 0.2, point.vec3d.x + 0.2, point.vec3d.y + 1.8, point.vec3d.z + 0.8).calculateIntercept(src, dest);
//                if (rayTraceResult != null) {
//                    mc.playerController.attackEntity(mc.player, entity);
//                    break;
//                }
//            }
//        }
//    }

    public void clickForAura() {
        for (Point point : positions) {
            point.ticks++;
            if (entity.getDistance(positions.get(0).vec3d.x, positions.get(0).vec3d.y, positions.get(0).vec3d.z) == 0.7071067690849304)
                continue;
            if (positions.size() > 0) {
                mc.playerController.attackEntity(mc.player, entity);
                break;
            }
        }
    }


    public boolean checkClick(float dist, final BackTrackUtils entityCoord) {
//        ChatUtils.addChatMessage(String.valueOf(entity.getDistance(Drainwalk.getInstance().getModuleManager().backTrack.nearestPosition(entityCoord).x, Drainwalk.getInstance().getModuleManager().backTrack.nearestPosition(entityCoord).y, Drainwalk.getInstance().getModuleManager().backTrack.nearestPosition(entityCoord).z)));
//        if (entity.getDistance(Drainwalk.getInstance().getModuleManager().backTrack.nearestPosition(entityCoord).x, Drainwalk.getInstance().getModuleManager().backTrack.nearestPosition(entityCoord).y, Drainwalk.getInstance().getModuleManager().backTrack.nearestPosition(entityCoord).z) > dist)
//            return false;
//        else
            return true;
    }

    private Vector3d getVectorForRotation(float pitch, float yaw)
    {
        final float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        final float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        final float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        final float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vector3d((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public void renderPositions() {
        for (Point pos : positions) {
            final Vector3d vec3d = pos.vec3d;
            if (entity.getDistance(positions.get(0).vec3d.x, positions.get(0).vec3d.y, positions.get(0).vec3d.z) == 0.7071067690849304)
                continue;
            double x = vec3d.x - mc.getRenderManager().info.getPos().x;
            final double y = vec3d.y - mc.getRenderManager().info.getPos().y;
            double z = vec3d.z - mc.getRenderManager().info.getPos().z;

            x += 0.5f;
            z += 0.5f;

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            final float[] color = ColorService.getRGBAf(ClientColor.main);
            GlStateManager.color4f(color[0], color[2], color[3], 0.1f);
            EntityRenderer<Entity> render = null;
            try {
                render = mc.getRenderManager().getEntityRenderMap().get(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (render != null) {
                (LivingRenderer.class.cast(render)).render((LivingEntity) entity, pos.yaw, mc.getRenderPartialTicks(), new MatrixStack(), Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), LightTexture.MAX_BRIGHTNESS);
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.clearCurrentColor();
            GL11.glPopMatrix();
        }
    }

    public static class Point {
        public Vector3d vec3d;
        public int ticks;
        public int yaw;
        public Point(Vector3d vec3d) {
            this.vec3d = vec3d;
        }

        public void update() {
            ticks++;
        }
    }
}
