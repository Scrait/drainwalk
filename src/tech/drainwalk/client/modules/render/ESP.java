package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.*;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.utils.math.MathematicUtils;
import tech.drainwalk.utils.math.Project;
import tech.drainwalk.utils.minecraft.CastUtil;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ESP extends Module {
    private final List<Entity> collectedEntities = new ArrayList<>();
    public final MultiOption widgets = new MultiOption("Widgets", new MultiOptionValue("Box", false), new MultiOptionValue("Item", false), new MultiOptionValue("Health Bar", false), new MultiOptionValue("Armor Bar", false));
    private final MultiOption targets =
            new MultiOption("Targets", new MultiOptionValue("Players", true), new MultiOptionValue("Friends", true), new MultiOptionValue("Self", true), new MultiOptionValue("Blocks", false));
    public final MultiOption blocks = new MultiOption("Blocks", new MultiOptionValue("Chest", true), new MultiOptionValue("Shulker", true)).addVisibleCondition(() -> targets.isSelected("Blocks"));
    private final IntBuffer viewport = GLAllocation.createDirectByteBuffer(16).asIntBuffer();
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    public ESP() {
        super("ESP", Category.RENDER);
        register(
                widgets,
                targets,
                blocks
        );
    }

    @EventTarget
    public void onRender3D(EventRender2D event) {
        final float partialTicks = event.getPartialTicks();
//        renderRects(partialTicks);
        collectEntities();
        for (final Entity entity : collectedEntities) {
            final double x = MathematicUtils.interpolate(entity.getPosX(), entity.lastTickPosX, partialTicks);
            final double y = MathematicUtils.interpolate(entity.getPosY(), entity.lastTickPosY, partialTicks);
            final double z = MathematicUtils.interpolate(entity.getPosZ(), entity.lastTickPosZ, partialTicks);
//            final double width = entity.getWidth() / 1.5;
//            final double n = entity.getHeight() + 0.2f;
//            final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + n, z + width);
//            final Vector3d[] vectors = {new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
//            GlStateManager.pushMatrix();
//            Vector4f position = null;
//            for (Vector3d vector : vectors) {
//                GlStateManager.pushMatrix();
//                mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), mc.getRenderPartialTicks());
//                vector = this.project2D(2, vector.x - mc.gameRenderer.getActiveRenderInfo().getProjectedView().x , vector.y - mc.gameRenderer.getActiveRenderInfo().getProjectedView().y, vector.z - mc.gameRenderer.getActiveRenderInfo().getProjectedView().z);
//                if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
//                    mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), mc.getRenderPartialTicks());
//                    if (position == null) {
//                        position = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 1.0f);
//                    }
//                    position.set((float) Math.min(vector.x, position.getX()), (float) Math.min(vector.y, position.getY()), (float) Math.max(vector.x, position.getZ()), (float) Math.max(vector.y, position.getW()));
//                }
//                GlStateManager.popMatrix();
//            }
//            if (position != null) {
//                GLUtils.INSTANCE.rescale(2);
//                final double posX = position.getX();
//                final double posY = position.getY();
//                final double endPosX = position.getZ();
//                final double endPosY = position.getW();
//                if (widgets.isSelected("Box")) {
//                    NORMAL_BLACK_RECT_RUNNABLES.add(() -> {
//                        RenderUtils.drawRect((float) posX, (float) posY, (float) (endPosX - posX), 1.5f, Color.BLACK.getRGB()); // Top
//                        RenderUtils.drawRect((float) posX, (float) posY, 1.5f, (float) (endPosY - posY + 1.5f), Color.BLACK.getRGB()); // Left
//                        RenderUtils.drawRect((float) endPosX, (float) posY, 1.5f, (float) (endPosY - posY + 1.5f), Color.BLACK.getRGB()); // Right
//                        RenderUtils.drawRect((float) posX, (float) endPosY, (float) (endPosX - posX), 1.5f, Color.BLACK.getRGB()); // Bottom
//                    });
//
//                    NORMAL_WHITE_RECT_RUNNABLES.add(() -> {
//                        RenderUtils.drawRect((float) (posX + 0.5), (float) (posY+ 0.5), (float) (endPosX - posX), 0.5f, -1);
//                        RenderUtils.drawRect((float) (posX + 0.5f), (float) (posY + 0.5f), 0.5f, (float) (endPosY - posY + 0.5f), -1);
//                        RenderUtils.drawRect((float) (endPosX + 0.5f), (float) (posY + 0.5f), 0.5f, (float) (endPosY - posY + 0.5f), -1);
//                        RenderUtils.drawRect((float) (posX + 0.5f), (float) (endPosY + 0.5f), (float) (endPosX - posX), 0.5f, -1);
//                    });
////                    RenderUtils.drawVRect((float) (posX - 1.0D), (float) posY, (float) (posX + 0.5D), (float) (endPosY + 0.5D), Color.black.getRGB());
////                    RenderUtils.drawVRect((float) (posX - 1.0D), (float) (posY - 0.5D), (float) (endPosX + 0.5D), (float) (posY + 0.5D + 0.5D), Color.black.getRGB());
////                    RenderUtils.drawVRect((float) (endPosX - 0.5D - 0.5D), (float) posY, (float) (endPosX + 0.5D), (float) (endPosY + 0.5D), Color.black.getRGB());
////                    RenderUtils.drawVRect((float) (posX - 1.0D), (float) (endPosY - 0.5D - 0.5D), (float) (endPosX + 0.5D), (float) (endPosY + 0.5D), Color.black.getRGB());
////                    RenderUtils.drawVRect((float) (posX - 0.5D), (float) posY, (float) (posX + 0.5D - 0.5D), (float) endPosY, -1);
////                    RenderUtils.drawVRect((float) posX, (float) (endPosY - 0.5D), (float) endPosX, (float) endPosY, -1);
////                    RenderUtils.drawVRect((float) (posX - 0.5D), (float) posY, (float) endPosX, (float) (posY + 0.5D), -1);
////                    RenderUtils.drawVRect((float) (endPosX - 0.5D), (float) posY, (float) endPosX, (float) endPosY, -1);
//                }
//                final LivingEntity entityLivingBase = (LivingEntity) entity;
//                final double hpPercentage = entityLivingBase.getHealth() / entityLivingBase.getMaxHealth();
//                final double hpHeight2 = (endPosY + 1f - posY) * hpPercentage;
//                final double hpHeight3 = (endPosY + 1f - posY);
//                final double armorPercentage = entityLivingBase.getTotalArmorValue();
//                final double armorWidth2 = (endPosX - posX) * armorPercentage / 20.0;
//                final double armorWidth3 = (endPosX - posX);
//                if (entityLivingBase.getHealth() > 0.0f) {
//                    if (widgets.isSelected("Health Bar")) {
//                        RenderUtils.drawRoundedShadow((float) (posX - 2.5 - 2), (float) (posY - 1), (float) (4), (float) (hpHeight3 + 2), 4, 0, ColorUtils.rgb(122,43,43));
//                        RenderUtils.drawRect((float) (posX - 3.5), (float) (posY), (float) (2), (float) (hpHeight3), ColorUtils.rgb(20, 20, 20));
//                        RenderUtils.drawGradientRect((float) (posX - 3), (float) (posY + 2), (float) (1), (float) (hpHeight3 - 2), ColorUtils.rgb(16, 9, 9), ColorUtils.rgb(16, 9, 9) , ColorUtils.rgb(255, 20, 20), ColorUtils.rgb(255, 20, 20));
//                        RenderUtils.drawRect((float) (posX - 3.5), (float) posY, (float) (2), (float) (hpHeight3 - hpHeight2), ColorUtils.rgba(0, 0, 0, 255));
//                    }
//
//                    if (widgets.isSelected("Armor Bar")) {
//                        RenderUtils.drawRoundedShadow((float) (posX - 1), (float) (endPosY - 1 + 2.3f), (float) (armorWidth3 + 4), (float) (4), 4, 0, ColorUtils.rgb(43, 84, 122));
//                        RenderUtils.drawRect((float) (posX), (float) (endPosY - 1 + 2 + 1.5f), (float) (armorWidth3 + 2), (float) (2), ColorUtils.rgb(20, 20, 20));
//                        RenderUtils.drawGradientRect((float) (posX + 1), (float) (endPosY + 1.5f + 1.5f), (float) (armorWidth3), (float) (1), ColorUtils.rgb(251, 75, 255), ColorUtils.rgb(0, 194, 255) , ColorUtils.rgb(0, 194, 255), ColorUtils.rgb(251, 75, 255));
//                        RenderUtils.drawRect((float) (posX + 2 + armorWidth2), (float) endPosY + 1 + 1.5f, (float) (armorWidth3 - armorWidth2 - 1), (float) (2), ColorUtils.rgba(0, 0, 0, 255));
//                    }
//                }
//                if (!entityLivingBase.getHeldItemMainhand().isEmpty()) {
//                    if (widgets.isSelected("Item"))
//                        FontManager.PIX_14.drawCenteredStringWithOutline(event.getMatrixStack(), entityLivingBase.getHeldItemMainhand().getDisplayName().getString(), (float) (posX + (endPosX - posX) / 2.0D), (float) (endPosY + 0.5D) + 9, -1);
//                }
//                GLUtils.INSTANCE.rescaleMC();
//            }
//            GlStateManager.popMatrix();
        }
//        for (TileEntity entity : mc.world.loadedTileEntityList) {
//            if (!targets.isSelected("Blocks")) return;
//            final double x = entity.getPos().getX() + 0.5f;
//            final double y = entity.getPos().getY() - 0.1f;
//            final double z = entity.getPos().getZ() + 0.5f;
//            final double width = 0.6f;
//            final double n = 1.2f;
//            final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + n, z + width);
//            final Vector3d[] vectors = {new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
//            GlStateManager.pushMatrix();
//            Vector4f position = null;
//            for (Vector3d vector : vectors) {
//                GlStateManager.pushMatrix();
//                mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), mc.getRenderPartialTicks());
//                vector = this.project2D(2, vector.x - mc.gameRenderer.getActiveRenderInfo().getProjectedView().x , vector.y - mc.gameRenderer.getActiveRenderInfo().getProjectedView().y, vector.z - mc.gameRenderer.getActiveRenderInfo().getProjectedView().z);
//                if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
//                    mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), mc.getRenderPartialTicks());
//                    if (position == null) {
//                        position = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 1.0f);
//                    }
//                    position.set((float) Math.min(vector.x, position.getX()), (float) Math.min(vector.y, position.getY()), (float) Math.max(vector.x, position.getZ()), (float) Math.max(vector.y, position.getW()));
//                }
//                GlStateManager.popMatrix();
//            }
//            if (position != null) {
//                GLUtils.INSTANCE.rescale(2);
//                final double posX = position.getX();
//                final double posY = position.getY();
//                final double endPosX = position.getZ();
//                final double endPosY = position.getW();
//                if (((entity instanceof ChestTileEntity && blocks.isSelected("Chest")) || (entity instanceof ShulkerBoxTileEntity && blocks.isSelected("Shulker"))) && widgets.isSelected("Box")) {
////                    NORMAL_WHITE_RECT_RUNNABLES.add(() -> {
////                        RenderUtils.drawRect((float) posX, (float) posY, (float) (endPosX - posX), 1.5f, -1); // Top
////                        RenderUtils.drawRect((float) posX, (float) posY, 1.5f, (float) (endPosY - posY + 1.5f), -1); // Left
////                        RenderUtils.drawRect((float) endPosX, (float) posY, 1.5f, (float) (endPosY - posY + 1.5f), -1); // Right
////                        RenderUtils.drawRect((float) posX, (float) endPosY, (float) (endPosX - posX), 1.5f, -1); // Bottom
////                    });
//                    NORMAL_BLACK_RECT_RUNNABLES.add(() -> {
//                        RenderUtils.drawRect((float) (posX + 0.5), (float) (posY+ 0.5), (float) (endPosX - posX), 0.5f, Color.BLACK.getRGB());
//                        RenderUtils.drawRect((float) (posX + 0.5f), (float) (posY + 0.5f), 0.5f, (float) (endPosY - posY + 0.5f), Color.BLACK.getRGB());
//                        RenderUtils.drawRect((float) (endPosX + 0.5f), (float) (posY + 0.5f), 0.5f, (float) (endPosY - posY + 0.5f), Color.BLACK.getRGB());
//                        RenderUtils.drawRect((float) (posX + 0.5f), (float) (endPosY + 0.5f), (float) (endPosX - posX), 0.5f, Color.BLACK.getRGB());
//                    });
//                }
//                GLUtils.INSTANCE.rescaleMC();
//            }
//            GlStateManager.popMatrix();
//        }
    }

    private int getHealthColor(final LivingEntity entity, final int c1, final int c2) {
        final float health = entity.getHealth();
        final float maxHealth = entity.getMaxHealth();
        final float hpPercentage = health / maxHealth;
        final int red = (int) ((c2 >> 16 & 0xFF) * hpPercentage + (c1 >> 16 & 0xFF) * (1.0f - hpPercentage));
        final int green = (int) ((c2 >> 8 & 0xFF) * hpPercentage + (c1 >> 8 & 0xFF) * (1.0f - hpPercentage));
        final int blue = (int) ((c2 & 0xFF) * hpPercentage + (c1 & 0xFF) * (1.0f - hpPercentage));
        return new Color(red, green, blue).getRGB();
    }

    private boolean isValid(final Entity entity) {
        final CastUtil castHelper = new CastUtil();
        if (targets.isSelected("Players")) {
            castHelper.apply(CastUtil.EntityType.PLAYERS);
        }
        if (targets.isSelected("Friends")) {
            castHelper.apply(CastUtil.EntityType.FRIENDS);
        }
        if (targets.isSelected("Self")) {
            castHelper.apply(CastUtil.EntityType.SELF);
        }
        return CastUtil.isInstanceof(entity, castHelper.build()) != null;
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        for (final Entity entity : mc.world.getPlayers()) {
            if (isValid(entity)) {
                this.collectedEntities.add(entity);
            }
        }
    }

    private Vector3d project2D(final float scaleFactor, final double x, final double y, final double z) {
        GL11.glGetFloatv(2982, this.modelview);
        GL11.glGetFloatv(2983, this.projection);
        GL11.glGetIntegerv(2978, this.viewport);
        if (Project.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector)) {
            return new Vector3d(this.vector.get(0) / scaleFactor, (mc.getMainWindow().getFramebufferHeight() - this.vector.get(1)) / scaleFactor, this.vector.get(2));
        }
        return null;
    }
}
