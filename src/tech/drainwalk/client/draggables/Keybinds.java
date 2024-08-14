package tech.drainwalk.client.draggables;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.math.vector.Vector2f;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.option.options.FloatOption;
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

public class Keybinds extends DraggableComponent {

    private final FloatOption padding = new FloatOption("Padding", 6, 0, 10).addIncrementValue(0.1f);
    private final FloatOption offsetOption = new FloatOption("Offset", 16, 10, 20).addIncrementValue(0.1f);

    private float widthAnim = 0;
    private final float FONT_SIZE = 13;

    public Keybinds() {
        super("Keybinds", new Vector2f(10, 10 * 2 + 26), 145, 66);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        for (Module module : dw.getApiMain().getModuleManager().stream().filter(Module::hasBind).toList()) {
            module.getKeyBindsAnimation().update(module.isEnabled());
        }
        try {
            Collections.max(dw.getApiMain().getModuleManager().stream().
                    filter(Module::isEnabled).filter(Module::hasBind).toList(), Comparator.comparing(s -> SFPD_REGULAR.getWidth(s.getName() + getKeyString(s), FONT_SIZE)));
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
        for (Module module : dw.getApiMain().getModuleManager().stream().filter(Module::hasBind).toList()) {
            module.getKeyBindsAnimation().animate(0, 1, 0.25f, EasingList.CIRC_OUT, mc.getTimer().renderPartialTicks);
            if (module.getKeyBindsAnimation().getAnimationValue() > 0.1) {
                offset += offsetOption.getValue() * module.getKeyBindsAnimation().getAnimationValue();
            }
        }

        getShowAnimation().animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);

        Module moduleMax;
        try {
            moduleMax = Collections.max(dw.getApiMain().getModuleManager().stream().
                    filter(Module::isEnabled).filter(Module::hasBind).toList(), Comparator.comparing(s -> SFPD_REGULAR.getWidth(s.getName() + getKeyString(s), FONT_SIZE)));
            final float width = SFPD_REGULAR.getWidth(moduleMax.getName() + getKeyString(moduleMax), FONT_SIZE) + 50 + padding.getValue();
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
            RenderService.drawRoundedDiagonalGradientRect(matrixStack, x, y, widthAnim, getDraggableOption().getHeight(), round, backgroundColors[0], backgroundColors[1]);
            // title
            {
                // left
                RenderService.drawRoundedOutlineRect(matrixStack, x + padding.getValue(), y + padding.getValue(), 16, 16, 3,1, borderColor);
                ICONS.drawText(matrixStack, String.valueOf(Icon.BIND.getSymbol()), x + padding.getValue() + 2.25f, y + padding.getValue() + 3f, textSecondColor, 12);

                RenderService.drawRoundedOutlineRect(matrixStack, x + padding.getValue() + 20, y + padding.getValue(), 16, 16, 3,1, borderColor);
                SFPD_REGULAR.drawText(matrixStack,
                        String.valueOf(dw.getApiMain().getModuleManager().stream().filter(module -> module.hasBind() && module.isEnabled()).toList().size()),
                        x + padding.getValue() + 24.5f, y + padding.getValue() + 1f, textSecondColor, FONT_SIZE);


                //right
                final float closeWidth = ICONS.getWidth(String.valueOf(Icon.CLOSE.getSymbol()), 10);
                final float optionsWidth = ICONS.getWidth(String.valueOf(Icon.OPTIONS.getSymbol()), 10);
                ICONS.drawText(matrixStack, String.valueOf(Icon.CLOSE.getSymbol()), x + widthAnim - padding.getValue() - closeWidth, y + padding.getValue() + 4f, ColorService.getColorWithAlpha(textFirstColor, 0.2f), 10);
                ICONS.drawText(matrixStack, String.valueOf(Icon.OPTIONS.getSymbol()), x + widthAnim - padding.getValue() - optionsWidth - closeWidth - 2, y + padding.getValue() + 4f, ColorService.getColorWithAlpha(textFirstColor, 0.2f), 10);
            }

            offset = 1;
            for (Module module : dw.getApiMain().getModuleManager().stream().filter(Module::hasBind).toList()) {
                if (module.getKeyBindsAnimation().getAnimationValue() > 0.1) {
                    final float currentY = y + offset + titleHeight + padding.getValue() - offsetOption.getValue() + offsetOption.getValue() * module.getKeyBindsAnimation().getAnimationValue();
                    float iconSize = 14;
                    // idiotical shit dont work with enum case
                    switch (module.getCategory().getIcon().getSymbol()) {
                        case 'e':
                            iconSize = 12.5f;
                            break;
                        case 'a':
                            iconSize = 11.5f;
                            break;
                    };
                    final String icon = String.valueOf(module.getCategory().getIcon().getSymbol());
                    final float categoryIconWidth = ICONS.getWidth(icon, iconSize);
                    ICONS.drawText(matrixStack, icon, x + padding.getValue(), currentY - ICONS.getHeight(iconSize) + 15, ColorService.getColorWithAlpha(textSecondColor, module.getKeyBindsAnimation().getAnimationValue()), iconSize);
                    SFPD_REGULAR.drawText(matrixStack, module.getName(), x + padding.getValue() + categoryIconWidth + 4, currentY, ColorService.getColorWithAlpha(textFirstColor, module.getKeyBindsAnimation().getAnimationValue()), FONT_SIZE);
                    SFPD_REGULAR.drawText(matrixStack, getKeyString(module),
                            (x + widthAnim - padding.getValue()) - SFPD_REGULAR.getWidth(getKeyString(module), FONT_SIZE), currentY, ColorService.getColorWithAlpha(textSecondColor, module.getKeyBindsAnimation().getAnimationValue()), FONT_SIZE);
                    offset += offsetOption.getValue() * module.getKeyBindsAnimation().getAnimationValue();
                }
            }

            matrixStack.pop();
        }
        // render end
    }

    private String getKeyString(Module module) {
        return InputMappings.getInputByCode(module.getCurrentKey(), GLFW.GLFW_KEY_UNKNOWN).getTranslationKeySplit().toUpperCase();
    }

}
