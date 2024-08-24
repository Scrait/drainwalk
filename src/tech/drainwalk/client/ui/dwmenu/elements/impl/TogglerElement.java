package tech.drainwalk.client.ui.dwmenu.elements.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.ui.dwmenu.elements.Element;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScreenService;

public class TogglerElement extends Element<BooleanOption> {

    public TogglerElement(BooleanOption option) {
        super(30, 18, option);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // anims
        activeAnimation.animate(0, 1, 0.1f, EasingList.EXPO_BOTH, mc.getTimer().renderPartialTicks);

        // colors
        final int whiteWith3Alpha = ColorService.getColorWithAlpha(-1, 0.03f);
        final int whiteWith10Alpha = ColorService.getColorWithAlpha(-1, 0.1f);
        final int bgColor = ColorService.interpolateColor(whiteWith3Alpha,
                ColorService.getColorWithAlpha(additionalSecondColor, 0.1f), activeAnimation.getAnimationValue());
        final int circleColor = ColorService.interpolateColor(whiteWith10Alpha,
                additionalSecondColor, activeAnimation.getAnimationValue());

        RenderService.drawRoundedRect(matrixStack, x, y, width, height, 10, bgColor);
        RenderService.drawRoundedRect(matrixStack, x + 2 + (width - 4 - (height - 4)) * activeAnimation.getAnimationValue(), y + 2, height - 4, height - 4, (height - 4) / 2, circleColor);
    }

    @Override
    public void tick() {
        activeAnimation.update(option.getValue());
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (ScreenService.isHovered((int) mouseX, (int) mouseY, x, y, width, height)) option.setValue(!option.getValue());
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {

    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

}
