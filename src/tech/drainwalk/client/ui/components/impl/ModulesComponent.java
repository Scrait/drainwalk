package tech.drainwalk.client.ui.components.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.ui.UIMain;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.font.Icon;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScissorService;
import tech.drainwalk.services.render.ScreenService;

import java.util.List;

public class ModulesComponent extends Component {

    private float scrollOffset = 0;
    private float targetScrollOffset = 0;
    private final float PADDING = 16;
    private final int COLUMNS = 2;

    public ModulesComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final float MODULE_PADDING = 12;

        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float elementWidth = (width - (COLUMNS + 1) * PADDING) / COLUMNS;

        scrollOffset += (targetScrollOffset - scrollOffset) * 0.2f;

        float centerX = x + width / 2;
        float centerY = y + height / 2;

        float scaleValue = parent.getAnimation().getAnimationValue();
        float scaledX = centerX - (centerX - x) * scaleValue;
        float scaledY = centerY - (centerY - y) * scaleValue;
        float scaledWidth = width * scaleValue;
        float scaledHeight = height * scaleValue;

        ScissorService.enableScissor((int) scaledX, (int) scaledY, (int) scaledWidth, (int) scaledHeight);

        float[] columnHeights = new float[COLUMNS];
        for (Module module : modules) {
            module.getAnimation().animate(0, 1, 0.15f, EasingList.NONE, mc.getTimer().renderPartialTicks);

            int i = modules.indexOf(module);
            int column = i % COLUMNS;

            float elementX = x + PADDING + column * (elementWidth + PADDING);
            float elementY = y + PADDING + columnHeights[column] - scrollOffset;

            float elementHeight = getModuleHeight(module);
            final float animationValue = module.getAnimation().getAnimationValue();
            final int borderColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(uiBorderColor, 0.3f), ColorService.getColorWithAlpha(uiBorderColor, 0.5f), animationValue);
            final int backgroundColor = ColorService.interpolateColor(moduleDisabledBackgroundColor, moduleEnabledBackgroundColor, animationValue);
            final int moduleNameColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(-1, 0.4f), textFirstColor, animationValue);
            final int moduleDescColor = ColorService.getColorWithAlpha(-1, 0.2f);
            final int optionIconColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(additionalSecondColor, 0.2f), ColorService.getColorWithAlpha(additionalSecondColor, 0.4f), animationValue);
            final int bindColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(additionalSecondColor, 0.2f), ColorService.getColorWithAlpha(textFirstColor, 0.8f), animationValue);
            final int bindBgColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(uiBorderColor, 0.2f), ColorService.getColorWithAlpha(uiBorderColor, 0.5f), animationValue);

            RenderService.drawRoundedVerLinearGradientRect(matrixStack, elementX, elementY, elementWidth, elementHeight, 8,
                    backgroundColor,  ColorService.getColorWithAlpha(backgroundColor, 0));
            RenderService.drawRoundedOutlineRect(matrixStack, elementX, elementY, elementWidth, elementHeight, 8, 1,
                    borderColor);

            SFPD_REGULAR.drawText(matrixStack, module.getName(), elementX + MODULE_PADDING, elementY + MODULE_PADDING, moduleNameColor, 16);
            SFPD_REGULAR.drawText(matrixStack, module.getDescription(), elementX + MODULE_PADDING, elementY + MODULE_PADDING * 2 + SFPD_REGULAR.getHeight(16), moduleDescColor, 14);

            // poloska
            RenderService.drawRect(matrixStack, elementX + 1, elementY + MODULE_PADDING * 3 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14), elementWidth - 2, 1, borderColor);

            ICONS.drawText(matrixStack, Icon.OPTIONS.getSymbolString(),
                    elementX + MODULE_PADDING,
                    elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) + 1,
                    optionIconColor, 14);

            final String keyString = module.hasBind() ? new TranslationTextComponent(InputMappings.getInputByCode(module.getCurrentKey(), GLFW.GLFW_KEY_UNKNOWN).func_237520_d_().getString()).getString().toUpperCase() : "NONE";
            final float keyWidth = SFPD_REGULAR.getWidth(keyString, 12) + 10;
            RenderService.drawRoundedRect(matrixStack,
                    elementX + MODULE_PADDING + 25,
                    elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) - 1,
                    keyWidth, 18, 4, bindBgColor);

            SFPD_REGULAR.drawCenteredText(matrixStack, keyString,
                    elementX + MODULE_PADDING + 25 + keyWidth / 2,
                    elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) + 2,
                    bindColor, 12);


            columnHeights[column] += elementHeight + PADDING;
        }

        ScissorService.disableScissor();
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {
        final float SCROLL_SPEED = 30f;
        targetScrollOffset -= (float) (delta * SCROLL_SPEED);

        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float totalHeight = 0;
        float[] columnHeights = new float[COLUMNS];

        for (Module module : modules) {
            int i = modules.indexOf(module);
            int column = i % COLUMNS;
            float elementHeight = getModuleHeight(module);
            columnHeights[column] += elementHeight + PADDING;

            if (column == COLUMNS - 1 || i == modules.size() - 1) {
                totalHeight = Math.max(totalHeight, columnHeights[column]);
            }
        }

        float maxScroll = Math.max(0, totalHeight - height + PADDING);
        targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScroll));
    }

    private float getModuleHeight(Module module) {
        // Определение высоты элемента на основе его контента.
        // Например, можно использовать фиксированное значение, или рассчитывать в зависимости от количества строк текста, изображения и т.д.
        return 105;
    }

    @Override
    public void tick() {
        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();
        for (Module module : modules) {
            module.getAnimation().update(module.isEnabled());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float elementWidth = (width - (COLUMNS + 1) * PADDING) / COLUMNS;

        float[] columnHeights = new float[COLUMNS];
        for (Module module : modules) {
            module.getAnimation().animate(0, 1, 0.15f, EasingList.NONE, mc.getTimer().renderPartialTicks);

            int i = modules.indexOf(module);
            int column = i % COLUMNS;

            float elementX = x + PADDING + column * (elementWidth + PADDING);
            float elementY = y + PADDING + columnHeights[column] - scrollOffset;

            float elementHeight = getModuleHeight(module);

            if (ScreenService.isHovered((int) mouseX, (int) mouseY, elementX, elementY, elementWidth, elementHeight)) module.toggle();

            columnHeights[column] += elementHeight + PADDING;
        }
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {}

}
