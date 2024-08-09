package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.math.MathematicUtils;
import tech.drainwalk.utils.math.Project;
import tech.drainwalk.utils.minecraft.CastUtil;
import tech.drainwalk.utils.render.RenderUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class NameTags extends Module {

    private final List<Entity> collectedEntities = new ArrayList<>();
    public final MultiOption widgets = new MultiOption("Widgets", new MultiOptionValue("Name", false), new MultiOptionValue("Armor", false));
    private final MultiOption targets =
            new MultiOption("Targets", new MultiOptionValue("Players", true), new MultiOptionValue("Friends", true), new MultiOptionValue("Self", true));

    private final IntBuffer viewport = GLAllocation.createDirectByteBuffer(16).asIntBuffer();
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    public NameTags() {
        super("NameTags", Category.RENDER);
        register(widgets, targets);
    }

    @EventTarget
    public void onRender3D(EventRender3D.PreHand event) {
        final float partialTicks = event.getPartialTicks();
        collectEntities();
        for (final Entity entity : collectedEntities) {
            final double x = MathematicUtils.interpolate(entity.getPosX(), entity.lastTickPosX, partialTicks);
            final double y = MathematicUtils.interpolate(entity.getPosY(), entity.lastTickPosY, partialTicks);
            final double z = MathematicUtils.interpolate(entity.getPosZ(), entity.lastTickPosZ, partialTicks);
            final double width = entity.getWidth() / 1.5;
            final double n = entity.getHeight() + 0.2f;
            final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + n, z + width);
            final Vector3d[] vectors = {new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
            GlStateManager.pushMatrix();
            Vector4f position = null;
            for (Vector3d vector : vectors) {
                GlStateManager.pushMatrix();
                mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), mc.getRenderPartialTicks());
                vector = this.project2D(2, vector.x - mc.gameRenderer.getActiveRenderInfo().getProjectedView().x , vector.y - mc.gameRenderer.getActiveRenderInfo().getProjectedView().y, vector.z - mc.gameRenderer.getActiveRenderInfo().getProjectedView().z);
                if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                    mc.gameRenderer.sukazaebalrabotai(event.getMatrixStack(), mc.getRenderPartialTicks());
                    if (position == null) {
                        position = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 1.0f);
                    }
                    position.set((float) Math.min(vector.x, position.getX()), (float) Math.min(vector.y, position.getY()), (float) Math.max(vector.x, position.getZ()), (float) Math.max(vector.y, position.getW()));
                }
                GlStateManager.popMatrix();
            }
            if (position != null) {
                GLService.INSTANCE.rescale(2);
                final double posX = position.getX();
                final double posY = position.getY();
                final double endPosX = position.getZ();
                final LivingEntity entityLivingBase = (LivingEntity) entity;
                final double dif = (endPosX - posX) / 2.0D;
                if (entityLivingBase.getHealth() > 0.0f) {
                    if (widgets.isSelected("Name")) {
                        if (entity == mc.player || mc.player.getDistance(entity) > 4) {
                            String name = entity.getName().getUnformattedComponentText() + " | <" + Math.round(entityLivingBase.getHealth()) + "HP>";
                            if (dw.getApiMain().getFriendManager().existsByName(entity.getName().getUnformattedComponentText())) {
                                name += ("  " + TextFormatting.GREEN + "[F]");
                            } else if (name.toString().contains("CIT-")) {
                                name += ("  " + TextFormatting.YELLOW + "[NPC]");
                            }
                            final double textWidth = (FontManager.MEDIUM_16.getStringWidth(name)) + 5;
                            RenderUtils.drawRoundedRect((float) ((posX + dif - textWidth / 2.0D)) - 2, (float) (posY) - 14, (float) (textWidth + 2), 11, 2, ColorService.rgba(35, 35, 35, 200));
                            FontManager.MEDIUM_16.drawString(event.getMatrixStack(), name, (float) ((posX + dif - textWidth / 2.0D)) + 2, (float) (posY) - 11, -1);
                        }
                    }
                    if (widgets.isSelected("Armor")) {
                        if (entity == mc.player || mc.player.getDistance(entity) > 4) {
                            int offset = 0;
                            for (ItemStack itemStack : entity.getArmorInventoryList()) {
                                if (itemStack != ItemStack.EMPTY) {
                                    drawItemStack(itemStack, (float) ((posX + dif - 10 / 2.0D)) - 33 + offset,
                                            posY - 30, false);
                                    FontManager.PIX_14.drawStringWithShadow(event.getMatrixStack(), "Pr: " + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack), (float) ((posX + dif - 10 / 2.0D)) - 33 + offset,
                                            posY - 34, ClientColor.textMain);
                                    offset += 20;
                                }
                            }
                        }

                    }
                }
                GLService.INSTANCE.rescaleMC();
            }
            GlStateManager.popMatrix();
        }
    }

    private void drawItemStack(ItemStack stack, double x, double y, boolean withoutOverlay) {
        RenderSystem.pushMatrix();
        RenderSystem.translated(x, y, 0);
        mc.getItemRenderer().renderItemModelIntoGUI3D(stack, 0, 0);
        if (!withoutOverlay)
            mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, stack, 0, 0);
        RenderSystem.popMatrix();
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
        if (targets.isSelected("Items")) {
            castHelper.apply(CastUtil.EntityType.ITEMS);
        }
        return CastUtil.isInstanceof(entity, castHelper.build()) != null;
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        for (final Entity entity : mc.world.getAllEntities()) {
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
