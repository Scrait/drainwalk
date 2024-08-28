package tech.drainwalk.client.ui.dwmenu.elements.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.client.ui.dwmenu.elements.Element;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScreenService;

import java.util.Arrays;

public class DropdownElement extends Element<SelectOption> {

    private boolean direction;

    public DropdownElement(SelectOption option) {
        super(80, 21, option);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        float finalHeight = 21;
        for (SelectOptionValue value : option.getValues()) {
            value.getOpenAnimation().animate(0, 1, 0.3f, EasingList.NONE, mc.getTimer().renderPartialTicks);
            finalHeight += 20 * value.getOpenAnimation().getAnimationValue();
        }
        setHeight(finalHeight);
        RenderService.drawRoundedRect(matrixStack, x, y, width, finalHeight, 3, -1);
    }

    @Override
    public void tick() {
        Arrays.stream(option.getValues()).toList().forEach(selectOptionValue -> selectOptionValue.getOpenAnimation().update(direction));
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (ScreenService.isHovered((int) mouseX, (int) mouseY, x, y, width, height)) {
            direction = !direction;
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {

    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }
}
