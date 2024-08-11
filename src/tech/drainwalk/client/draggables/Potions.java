package tech.drainwalk.client.draggables;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.font.Icon;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.utils.time.Timer;

import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Potions extends DraggableComponent {

    private final FloatOption padding = new FloatOption("Padding", 6, 0, 10).addIncrementValue(0.1f);
    private final FloatOption offsetOption = new FloatOption("Offset", 16, 10, 20).addIncrementValue(0.1f);

    private float widthAnim = 0;
    private final float FONT_SIZE = 13;

    public Potions() {
        super("Potions", new Vector2f(10 * 2 + 145, 10 * 2 + 26), 145, 66);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        for (EffectInstance effectInstance : mc.player.getActivePotionEffects()) {
            effectInstance.getPotionsAnimation().update(effectInstance.getDuration() > 10);
        }
        try {
            Collections.max(mc.player.getActivePotionEffects(),
                    Comparator.comparing(
                            s -> SFPD_REGULAR.getWidth(
                                    s.getPotion().getDisplayName().getString() + " " + I18n.format("enchantment.level." + (s.getAmplifier() + 1)
                                    ) + EffectUtils.getPotionDurationString(s, 1), FONT_SIZE)
                    ));
            getShowAnimation().update(true);
        } catch (NoSuchElementException ignored) {
            getShowAnimation().update(false);
        }
    }

    @Override
    public void onRender2D(EventRender2D event) {
        final float x = getDraggableOption().getValue().x;
        final float y = getDraggableOption().getValue().y;
        final float round = getRound().getValue();
        final float titleHeight = 20;
        final int[] backgroundColors = getBackgroundColorsWithAlpha();
        final MatrixStack matrixStack = event.getMatrixStack();

        float offset = 0;
        for (EffectInstance effectInstance : mc.player.getActivePotionEffects()) {
            effectInstance.getPotionsAnimation().animate(0, 1, 0.25f, EasingList.CIRC_OUT, mc.getTimer().renderPartialTicks);
            if (effectInstance.getPotionsAnimation().getAnimationValue() > 0.1) {
                offset += offsetOption.getValue() * effectInstance.getPotionsAnimation().getAnimationValue();
            }
        }

        getShowAnimation().animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);

        EffectInstance potionMax;
        try {
            potionMax = Collections.max(mc.player.getActivePotionEffects(),
                    Comparator.comparing(
                            s -> SFPD_REGULAR.getWidth(
                                    getPotionString(s), FONT_SIZE)
                    ));
            final float width = SFPD_REGULAR.getWidth(getPotionString(potionMax), FONT_SIZE) + 10 + padding.getValue();
            widthAnim = AnimationService.animation(widthAnim, width, (float) (Timer.deltaTime()));
        } catch (NoSuchElementException ignored) {
        }

        widthAnim = Math.max(145, widthAnim);

        getDraggableOption().setWidth(widthAnim);
        getDraggableOption().setHeight(offset + titleHeight + padding.getValue() * 2);

        // render start
        {
            matrixStack.push();
//            matrixStack.scale(2, 2, 2);
            GLService.INSTANCE.scaleAnimation(matrixStack, x, y, widthAnim, getDraggableOption().getHeight(), getShowAnimation().getAnimationValue());

            // background
            RenderService.drawRoundedLinearGradientRect(matrixStack, x, y, widthAnim, getDraggableOption().getHeight(), round, backgroundColors[0], backgroundColors[1]);
            // title
            {
                // left
                RenderService.drawRoundedOutlineRect(matrixStack, x + padding.getValue(), y + padding.getValue(), 16, 16, 3,1, borderColor);
                ICONS.drawText(matrixStack, String.valueOf(Icon.POTION.getSymbol()), x + padding.getValue() + 3.75f, y + padding.getValue() + 3f, textSecondColor, 12);

                RenderService.drawRoundedOutlineRect(matrixStack, x + padding.getValue() + 20, y + padding.getValue(), 16, 16, 3,1, borderColor);
                SFPD_REGULAR.drawText(matrixStack,
                        String.valueOf(mc.player.getActivePotionEffects().size()),
                        x + padding.getValue() + 24.5f, y + padding.getValue() + 1f, textSecondColor, FONT_SIZE);


                //right
                final float closeWidth = ICONS.getWidth(String.valueOf(Icon.CLOSE.getSymbol()), 10);
                final float optionsWidth = ICONS.getWidth(String.valueOf(Icon.OPTIONS.getSymbol()), 10);
                ICONS.drawText(matrixStack, String.valueOf(Icon.CLOSE.getSymbol()), x + widthAnim - padding.getValue() - closeWidth, y + padding.getValue() + 5f, ColorService.getColorWithAlpha(textFirstColor, 0.2f), 10);
                ICONS.drawText(matrixStack, String.valueOf(Icon.OPTIONS.getSymbol()), x + widthAnim - padding.getValue() - optionsWidth - closeWidth - 2, y + padding.getValue() + 5f, ColorService.getColorWithAlpha(textFirstColor, 0.2f), 10);
            }

            offset = 1;
            PotionSpriteUploader potionspriteuploader = mc.getPotionSpriteUploader();
            for (EffectInstance effectInstance : mc.player.getActivePotionEffects()) {
                if (effectInstance.getPotionsAnimation().getAnimationValue() > 0.1) {
                    final float currentY = y + offset + titleHeight + padding.getValue();
                    final float categoryIconWidth = 8;
                    TextureAtlasSprite textureatlassprite = potionspriteuploader.getSprite(effectInstance.getPotion());
                    mc.getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
                    RenderService.blit(matrixStack, (int) (x + padding.getValue()), (int) currentY, 0, 100, 100, textureatlassprite);
                    ICONS.drawText(matrixStack, String.valueOf(Icon.POTION.getSymbol()), x + padding.getValue(), currentY + 2f, textSecondColor, 12);
                    SFPD_REGULAR.drawText(matrixStack, effectInstance.getPotion().getDisplayName().getString() + " " + I18n.format("enchantment.level." + (effectInstance.getAmplifier() + 1)
                    ), x + padding.getValue() + categoryIconWidth + 4, currentY, ColorService.getColorWithAlpha(textFirstColor, effectInstance.getPotionsAnimation().getAnimationValue()), FONT_SIZE);
                    SFPD_REGULAR.drawText(matrixStack, EffectUtils.getPotionDurationString(effectInstance, 1),
                            (x + widthAnim - padding.getValue()) - SFPD_REGULAR.getWidth(EffectUtils.getPotionDurationString(effectInstance, 1), FONT_SIZE), currentY, ColorService.getColorWithAlpha(textSecondColor, effectInstance.getPotionsAnimation().getAnimationValue()), FONT_SIZE);
                    offset += offsetOption.getValue() * effectInstance.getPotionsAnimation().getAnimationValue();
                }
            }

            matrixStack.pop();
        }
        // render end
    }

    private String getPotionString(EffectInstance effectInstance) {
        return effectInstance.getPotion().getDisplayName().getString() + " " + I18n.format("enchantment.level." + (effectInstance.getAmplifier() + 1)
        ) + EffectUtils.getPotionDurationString(effectInstance, 1);
    }

}
