package tech.drainwalk.client.draggables;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.client.modules.combat.Aura;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.math.MathService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.utils.time.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TargetInfo extends DraggableComponent {

    private final FloatOption padding = new FloatOption("Padding", 6, 0, 10).addIncrementValue(0.1f);
    private double healthBarWidth;
    private LivingEntity target = null;


    public TargetInfo() {
        super("TargetInfo", new Vector2f(10, 10 * 3 + 26 + 66), 128, 56);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        boolean direction;
        if (dw.getApiMain().getModuleManager().findByClass(Aura.class).getTarget() == null) {
            if (mc.player != null && mc.currentScreen instanceof ChatScreen) {
                target = mc.player;
                direction = true;
            } else {
                direction = false;
            }
        } else {
            target = dw.getApiMain().getModuleManager().findByClass(Aura.class).getTarget();
            direction = true;
        }
        getShowAnimation().update(direction);
    }

    @Override
    public void onRender2D(EventRender2D event) {
        final int[] backgroundColors = getBackgroundColorsWithAlpha();
        final float x = getDraggableOption().getValue().x;
        final float y = getDraggableOption().getValue().y;
        final MatrixStack matrixStack = event.getMatrixStack();
        getShowAnimation().animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
        if (target == null) return;

        matrixStack.push();
//        matrixStack.scale(2, 2,2);
        GLService.INSTANCE.scaleAnimation(matrixStack, x, y, getDraggableOption().getWidth(), getDraggableOption().getHeight(), getShowAnimation().getAnimationValue());
        // bg
        RenderService.drawRoundedDiagonalGradientRect(matrixStack, x, y, getDraggableOption().getWidth(), getDraggableOption().getHeight(), getRound().getValue(), backgroundColors[0], backgroundColors[1]);

        // head
        RenderService.drawRoundedTexture(matrixStack, new ResourceLocation("drainwalk/images/deus_mode.png"),x + padding.getValue(), y + padding.getValue(), 28, 28, 2, 1);

        // main info
        SFPD_REGULAR.drawText(matrixStack, target.getName().getString(), x + padding.getValue() * 2 + 28, y + padding.getValue(), textFirstColor, 13);

        // items
        {
            if (target instanceof PlayerEntity)
                drawItemTargetHUD(matrixStack, (PlayerEntity) target, x + padding.getValue() * 2 + 28, y + padding.getValue() * 2 + SFPD_REGULAR.getHeight(13));
        }

        // health bar
        {
            final float maxBarWidth = 116;
            final double healthWid = MathService.clamp(target.getHealth() / target.getMaxHealth() * maxBarWidth, 0.0D, maxBarWidth);
            healthBarWidth = AnimationService.animation((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
            RenderService.drawRoundedHorLinearGradientRect(matrixStack, x + padding.getValue(), y + padding.getValue() * 2 + 28, maxBarWidth, 10, 2,
                    ColorService.getColorWithAlpha(additionalFirstColor, 0.5f),
                    ColorService.getColorWithAlpha(additionalSecondColor, 0.5f)
            );
            RenderService.drawRoundedHorLinearGradientRect(matrixStack, x + padding.getValue(), y + padding.getValue() * 2 + 28, (float) healthBarWidth, 10, 2, additionalFirstColor, additionalSecondColor);
            SFPD_REGULAR.drawCenteredText(matrixStack, String.valueOf((int) target.getHealth()), x + padding.getValue() + maxBarWidth / 2, y + padding.getValue() * 2 + 28.5f, textFirstColor, 9);
            healthBarWidth = AnimationService.getAnimationState((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
        }

        matrixStack.pop();
    }

    private ItemStack getEquipmentInSlot(int item, PlayerEntity player) {
        return item == 0 ? player.inventory.getCurrentItem() : player.inventory.armorInventory.get(item - 1);
    }

    private void drawItemTargetHUD(MatrixStack matrixStack, PlayerEntity player, float posX, float posY) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(player.getHeldItemMainhand(), player.getHeldItemOffhand()));
        for (int i = 1; i < 5; ++i) {
            ItemStack getEquipmentInSlot = getEquipmentInSlot(i, player);
            list.add(getEquipmentInSlot);
        }
        for (ItemStack itemStack : list) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.pushMatrix();
            GLService.INSTANCE.scaleAnimation(getDraggableOption().getValue().x, getDraggableOption().getValue().y, getDraggableOption().getWidth(), getDraggableOption().getHeight(), getShowAnimation().getAnimationValue());
            GlStateManager.translatef(posX, posY, 1.0f);
            GlStateManager.scalef(0.9f, 0.9f, 0.9f);
            GlStateManager.translatef(-posX - 1, -posY - 4, 1.0f);
            renderItem(itemStack, posX, posY);
            GlStateManager.popMatrix();
            posX += 14.0f;
        }
    }

    private void renderItem(ItemStack itemStack, float x, float y) {
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepthTest();
        GlStateManager.disableAlphaTest();

        mc.getItemRenderer().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();

        mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack, (int) x, (int) y);
        mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, itemStack, (int) x, (int) y);
        RenderHelper.disableStandardItemLighting();
        mc.getItemRenderer().zLevel = 0.0f;
        GlStateManager.enableAlphaTest();
    }

}
