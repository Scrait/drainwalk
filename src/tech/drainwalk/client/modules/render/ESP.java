package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.*;
import net.optifine.Config;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScissorService;
import tech.drainwalk.services.render.ScreenService;
import tech.drainwalk.utils.minecraft.CastUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ESP extends Module implements ITheme {
    private final List<Entity> collectedEntities = new ArrayList<>();
    public final MultiOption widgets = new MultiOption("Widgets", new MultiOptionValue("Box", false), new MultiOptionValue("Item", false), new MultiOptionValue("Health Bar", false), new MultiOptionValue("Armor Bar", false));
    private final MultiOption targets =
            new MultiOption("Targets", new MultiOptionValue("Players", true), new MultiOptionValue("Friends", true), new MultiOptionValue("Self", true), new MultiOptionValue("Blocks", false));
    public final MultiOption blocks = new MultiOption("Blocks", new MultiOptionValue("Chest", true), new MultiOptionValue("Shulker", true)).addVisibleCondition(() -> targets.isSelected("Blocks"));

    public ESP() {
        super("ESP", Category.RENDER);
    }

    @EventTarget
    public void onRender3D(EventRender2D event) {
        collectEntities();
        for (final Entity entity : collectedEntities) {
            renderEntityESP((PlayerEntity) entity, event.getMatrixStack(), event.getPartialTicks());
        }
    }

    private void renderEntityESP(PlayerEntity entity, MatrixStack matrixStack, float partialTicks) {
        AxisAlignedBB boundingBox = entity.getBoundingBox().offset(-entity.getPosX(), -entity.getPosY(), -entity.getPosZ());
        Vector3d interpolatedPos = entity.getLerpedPos(partialTicks);
        boundingBox = boundingBox.offset(interpolatedPos);
        boundingBox = boundingBox.expand(0, 0.1, 0);

        Vector3d center = boundingBox.getCenter();

        Vector2f screenPos = ScreenService.worldToScreen(center);
        screenPos = new Vector2f((float) (screenPos.x * mc.getMainWindow().getGuiScaleFactor()), (float) (screenPos.y * mc.getMainWindow().getGuiScaleFactor()));

        if (screenPos.x != Float.MAX_VALUE && screenPos.y != Float.MAX_VALUE) {
            double distance = mc.getRenderManager().info.getProjectedView().distanceTo(center);
            float screenHeight = (float) mc.getMainWindow().getFramebufferHeight();

            float scaleFactor = (float) (1 / distance) * 100;
            if (Config.zoomMode) scaleFactor *= 4;

            float normalizedScaleFactor = scaleFactor * screenHeight / 1080.0f; // 1080 - базовое разрешение
            float width = 9 * normalizedScaleFactor;
            float height = 16 * normalizedScaleFactor;

            float cornerSize = 3 * normalizedScaleFactor;

            // Левый верхний угол
            ScissorService.enableScissor((int) (screenPos.x - width / 2), (int) (screenPos.y - height / 2), (int) cornerSize, (int) cornerSize);
            RenderService.drawRoundedOutlineDiagonalGradientRect(matrixStack, screenPos.x - width / 2, screenPos.y - height / 2, width, height, 3, 3, backgroundFirstColor, backgroundSecondColor);
            ScissorService.disableScissor();

            // Правый верхний угол
            ScissorService.enableScissor((int) (screenPos.x + width / 2 - cornerSize), (int) (screenPos.y - height / 2), (int) cornerSize, (int) cornerSize);
            RenderService.drawRoundedOutlineDiagonalGradientRect(matrixStack, screenPos.x - width / 2, screenPos.y - height / 2, width, height, 3, 3, backgroundFirstColor, backgroundSecondColor);
            ScissorService.disableScissor();

            // Левый нижний угол
            ScissorService.enableScissor((int) (screenPos.x - width / 2), (int) (screenPos.y + height / 2 - cornerSize), (int) cornerSize, (int) cornerSize);
            RenderService.drawRoundedOutlineDiagonalGradientRect(matrixStack, screenPos.x - width / 2, screenPos.y - height / 2, width, height, 3, 3, backgroundFirstColor, backgroundSecondColor);
            ScissorService.disableScissor();

            // Правый нижний угол
            ScissorService.enableScissor((int) (screenPos.x + width / 2 - cornerSize), (int) (screenPos.y + height / 2 - cornerSize), (int) cornerSize, (int) cornerSize);
            RenderService.drawRoundedOutlineDiagonalGradientRect(matrixStack, screenPos.x - width / 2, screenPos.y - height / 2, width, height, 3, 3, backgroundFirstColor, backgroundSecondColor);
            ScissorService.disableScissor();
        }
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

}
