package tech.drainwalk.client.ui.dwmenu.elements.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.ui.dwmenu.elements.Element;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;

public class TogglerElement extends Element<BooleanOption> {

    public TogglerElement(BooleanOption option) {
        super(30, 18, option);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // anims
        activeAnimation.animate(0, 1, 0.15f, EasingList.NONE, mc.getTimer().renderPartialTicks);
        RenderService.drawRoundedRect(matrixStack, x, y, width, height, 10, ColorService.getColorWithAlpha(-1, 0.05f * openAnimation.getAnimationValue()));
        RenderService.drawRoundedRect(matrixStack, x + 2 + (width - 4 - (height - 4)) * activeAnimation.getAnimationValue(), y + 2, height - 4, height - 4, (height - 4) / 2, ColorService.getColorWithAlpha(-1, 1 * openAnimation.getAnimationValue()));
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

    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {

    }

}
