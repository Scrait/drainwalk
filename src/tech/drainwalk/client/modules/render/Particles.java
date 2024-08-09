package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.math.MathematicUtils;
import tech.drainwalk.utils.render.RenderUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Particles extends Module {

    private final ArrayList<Partical> particals = new ArrayList();
    private final FloatOption deleteAfter = new FloatOption("Time", 10000f, 100f,20000).addIncrementValue(1f);


    public Particles() {
        super("Particles", Category.RENDER);
        register(deleteAfter);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.world != null) {
            for (Entity entity : mc.world.getPlayers()) {
                if (entity != null && (entity instanceof PlayerEntity)) {
                    final LivingEntity player = (LivingEntity) entity;
                    if (( player.hurtTime ) > 0 && player != mc.player) {
                        this.particals.add(new Partical(player.getPosX() + (double) MathematicUtils.randomNumber(-0.05f, 0.05f), MathematicUtils.randomNumber((float)(player.getPosY() + (double)player.getHealth()), (float)player.getPosY()), player.getPosZ() + (double)MathematicUtils.randomNumber(-0.05f, 0.05f)));
                        this.particals.add(new Partical(MathematicUtils.randomNumber((float)(player.getPosX() - (double)player.getWidth()), (float)(player.getPosX() + (double)0.1f)), MathematicUtils.randomNumber((float)(player.getPosY() + (double)player.getHealth()), (float)(player.getPosY() + (double)0.1f)), MathematicUtils.randomNumber((float)(player.getPosZ() - (double)player.getWidth()), (float)(player.getPosZ() + (double)0.1f))));
                        this.particals.add(new Partical(MathematicUtils.randomNumber((float)(player.getPosX() + (double)player.getWidth()), (float)(player.getPosX() + (double)0.1f)), MathematicUtils.randomNumber((float)(player.getPosY() + (double)player.getHealth()), (float)(player.getPosY() + (double)0.1f)), MathematicUtils.randomNumber((float)(player.getPosZ() + (double)player.getWidth()), (float)(player.getPosZ() + (double)0.1f))));
                        this.particals.add(new Partical(player.getPosX() + (double)MathematicUtils.randomNumber(-0.05f, 0.05f), MathematicUtils.randomNumber((float)(player.getPosY() + (double)player.getHealth()), (float)player.getPosY()), player.getPosZ() + (double)MathematicUtils.randomNumber(-0.05f, 0.05f)));
                    }
                    for (int i = 0; i < this.particals.size(); ++i) {
                        if (System.currentTimeMillis() - this.particals.get(i).getTime() < deleteAfter.getValue()) continue;
                        this.particals.remove(i);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (mc.player != null && mc.world != null) {
            for (Partical partical2 : this.particals) {
                final float color = ColorService.getSkyRainbow(15f, (int) partical2.time);
                partical2.render((int) color);
            }
        }
    }



    public class Partical {
        private double x;
        private double y;
        private double z;
        private double motionX;
        private double motionY;
        private double motionZ;
        private final long time;
        private int alpha = 180;

        public Partical(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.motionX = MathematicUtils.randomNumber(-0.04f, 0.06f);
            this.motionY = MathematicUtils.randomNumber(-0.02f, 0.04f);
            this.motionZ = MathematicUtils.randomNumber(-0.08f, 0.04f);
            this.time = System.currentTimeMillis();
        }

        public long getTime() {
            return this.time;
        }

        public void update() {
            final double yEx = 0.0;
            final double sp = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.x += this.motionX;
            this.y += this.motionY;
            if (this.posBlock(this.x, this.y, this.z)) {
                this.motionY = -this.motionY / 1.1;
            } else if (this.posBlock(this.x, this.y, this.z) || this.posBlock(this.x, this.y - yEx, this.z) || this.posBlock(this.x, this.y + yEx, this.z) || this.posBlock(this.x - sp, this.y, this.z - sp) || this.posBlock(this.x + sp, this.y, this.z + sp) || this.posBlock(this.x + sp, this.y, this.z - sp) || this.posBlock(this.x - sp, this.y, this.z + sp) || this.posBlock(this.x + sp, this.y, this.z) || this.posBlock(this.x - sp, this.y, this.z) || this.posBlock(this.x, this.y, this.z + sp) || this.posBlock(this.x, this.y, this.z - sp) || this.posBlock(this.x - sp, this.y - yEx, this.z - sp) || this.posBlock(this.x + sp, this.y - yEx, this.z + sp) || this.posBlock(this.x + sp, this.y - yEx, this.z - sp) || this.posBlock(this.x - sp, this.y - yEx, this.z + sp) || this.posBlock(this.x + sp, this.y - yEx, this.z) || this.posBlock(this.x - sp, this.y - yEx, this.z) || this.posBlock(this.x, this.y - yEx, this.z + sp) || this.posBlock(this.x, this.y - yEx, this.z - sp) || this.posBlock(this.x - sp, this.y + yEx, this.z - sp) || this.posBlock(this.x + sp, this.y + yEx, this.z + sp) || this.posBlock(this.x + sp, this.y + yEx, this.z - sp) || this.posBlock(this.x - sp, this.y + yEx, this.z + sp) || this.posBlock(this.x + sp, this.y + yEx, this.z) || this.posBlock(this.x - sp, this.y + yEx, this.z) || this.posBlock(this.x, this.y + yEx, this.z + sp) || this.posBlock(this.x, this.y + yEx, this.z - sp)) {
                this.motionX = -this.motionX + this.motionZ;
                this.motionZ = -this.motionZ + this.motionX;
            }
            this.z += this.motionZ;
            this.motionX /= 1.005;
            this.motionZ /= 1.005;
            this.motionY /= 1.005;
        }

        public void render(int color) {
            this.update();
            this.alpha = (int)((double)this.alpha - 0.1);
            float scale = 0.07f;
            RenderSystem.disableDepthTest();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glBlendFunc(770, 771);
            try {
                ActiveRenderInfo renderManager = mc.gameRenderer.getActiveRenderInfo();
                final double posX = this.x - renderManager.getProjectedView().x;
                final double posY = this.y - renderManager.getProjectedView().y;
                final double posZ = this.z - renderManager.getProjectedView().z;
                final double distanceFromPlayer = mc.player.getDistance(this.x, this.y - 1.0, this.z);
                int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
                if (quality > 350) {
                    quality = 350;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(posX, posY, posZ);
                GL11.glScalef(-scale, -scale, -scale);
                GL11.glRotated(-mc.getRenderManager().getCameraOrientation().getY(), 0.0, 1.0, 0.0);
                GL11.glRotated(mc.getRenderManager().getCameraOrientation().getX(), 1.0, 0.0, 0.0);
                final Color c = new Color(color);
                RenderUtils.drawFilledCircleNoGL(0, 0, 0.7, color, quality);
                if (distanceFromPlayer < 4.0) {
                    RenderUtils.drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);
                }
                RenderUtils.drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);
                GL11.glScalef(0.8f, 0.8f, 0.8f);
                GL11.glPopMatrix();
            }
            catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            RenderSystem.enableDepthTest();
            GL11.glColor3d(255.0, 255.0, 255.0);
        }

        private boolean posBlock(double x, double y, double z) {
            return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.AIR && mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DEAD_BUSH && mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.VINE && mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.REDSTONE_WIRE && mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SNOW;
        }
    }
}
