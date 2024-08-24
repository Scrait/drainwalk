package tech.drainwalk.client.ui.dwmenu.elements.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.ui.dwmenu.elements.Element;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.math.MathService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScreenService;

public class SliderElement extends Element<FloatOption> {

    @Getter
    private float animatedSliderValue;
    private boolean sliderActive;

    public SliderElement(FloatOption option) {
        super(128.75f, 17f, option);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        animatedSliderValue = AnimationService.animation(animatedSliderValue, option.getValue(), partialTicks);

        final int whiteWith3Alpha = ColorService.getColorWithAlpha(-1, 0.03f);
        final int whiteWith40Alpha = ColorService.getColorWithAlpha(-1, 0.13f);
        final int textColor = ColorService.interpolateColor(whiteWith40Alpha,
                textFirstColor, withModuleEnabledAnimation.getAnimationValue());
        final int bgFirstColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(sliderAdditionalFirstColor, 0.4f),
                additionalFirstColor, withModuleEnabledAnimation.getAnimationValue());
        final int bgSecondColor = ColorService.interpolateColor(whiteWith40Alpha,
                additionalSecondColor, withModuleEnabledAnimation.getAnimationValue());
        final int circleColor = ColorService.interpolateColor(sliderAdditionalSecondColor,
                additionalFirstColor, withModuleEnabledAnimation.getAnimationValue());
        final int circleOutlineColor = ColorService.interpolateColor(whiteWith3Alpha,
                textFirstColor, withModuleEnabledAnimation.getAnimationValue());

        final double roundedAnimValue = MathService.round(animatedSliderValue, option.getInc());
        String sliderValueText = String.valueOf(roundedAnimValue);
        if (roundedAnimValue % 1 == 0) sliderValueText = sliderValueText.split("\\.")[0];
        SFPD_REGULAR.drawText(matrixStack, sliderValueText, x - SFPD_REGULAR.getWidth(sliderValueText, 12) - 5, y, textColor, 12);

        RenderService.drawRoundedRect(matrixStack, x, y + 4, width, 4, 2, whiteWith3Alpha);
        RenderService.drawRoundedOutlineRect(matrixStack, x, y + 4, width, 4, 2, 2, whiteWith3Alpha);

        RenderService.drawRoundedHorLinearGradientRect(matrixStack, x, y + 4, width * getPos(), 4, 2, bgFirstColor, bgSecondColor);

        RenderService.drawRoundedRect(matrixStack, x + 5 + (width - 10) * getPos() - 5, y + 6 - 5, 10, 10, 5, circleColor);
        RenderService.drawRoundedOutlineRect(matrixStack, x + 3 + (width - 10) * getPos() - 5, y + 6 - 7, 14, 14, 5, 2, circleOutlineColor);
        if (sliderActive) {
            option.setValue(createSlider(x, mouseX));
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (ScreenService.isHovered((int) mouseX, (int) mouseY, x, y, width, height)) {
            System.out.println("FDF");
            sliderActive = true;
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {

    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        sliderActive = false;
    }

    private float createSlider(float posX, float mouseX) {
        float delta = (int) (option.getMax() - option.getMin());
        float clickedX = mouseX - posX;
        float value = clickedX / width;
        float outValue = option.getMin() + delta * value;
        return MathService.clamp((float) MathService.round(outValue, option.getInc()), option.getMin(), option.getMax());
    }

    private float getPos() {
        float delta = (option.getMax() - option.getMin());
        return ((animatedSliderValue - option.getMin()) / delta);
    }

}
