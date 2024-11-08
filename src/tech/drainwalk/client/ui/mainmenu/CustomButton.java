package tech.drainwalk.client.ui.mainmenu;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScissorService;

import javax.annotation.Nonnull;

public class CustomButton extends Button implements IFonts, IInstanceAccess {

    private final Animation hoverAnimation = new Animation();
    private final boolean isAlternativeButton;

    public CustomButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, boolean isAlternativeButton) {
        super(x, y, width, height, title, pressedAction);
        this.isAlternativeButton = isAlternativeButton;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mouseX, mouseY, 1);
        super.render(matrixStack, (int) fixedMouseCords.x, (int) fixedMouseCords.y, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mouseX, mouseY, 1);
        return super.mouseClicked(fixedMouseCords.x, fixedMouseCords.y, button);
    }

    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        // anim
        // TODO: update method must be called in tick method
        hoverAnimation.update(isHovered);

        hoverAnimation.animate(0, 1, 0.1f, EasingList.NONE, partialTicks);

        // colors
        final int buttonColor = isAlternativeButton ? ColorService.hexToRgb("#FF6969") : ColorService.hexToRgb("#A39BD6");
        final int bgColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(-1, 0.03f),
                ColorService.getColorWithAlpha(buttonColor, 0.05f), hoverAnimation.getAnimationValue());
        final int bgOutlineColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(-1, 0.05f),
                ColorService.getColorWithAlpha(buttonColor, 0.2f), hoverAnimation.getAnimationValue());
        final int additionalSecondColorWithAlpha = ColorService.getColorWithAlpha(buttonColor, 0);
        final int bgGradientColor = ColorService.interpolateColor(additionalSecondColorWithAlpha,
                ColorService.getColorWithAlpha(buttonColor, 0.2f), hoverAnimation.getAnimationValue());
        final int bgOutlineGradientColor = ColorService.interpolateColor(additionalSecondColorWithAlpha,
                buttonColor, hoverAnimation.getAnimationValue());

        // bg
        RenderService.drawRoundedRect(matrixStack, x, y, width, height, 6, bgColor);
        ScissorService.enableScissor(x, y, width / 2, height);
        RenderService.drawRoundedHorLinearGradientRect(matrixStack, x, y, width / 2f + 6, height, 6, additionalSecondColorWithAlpha, bgGradientColor);
        ScissorService.disableScissor();
        ScissorService.enableScissor(x + width / 2, y, width / 2, height);
        RenderService.drawRoundedHorLinearGradientRect(matrixStack, x + width / 2f - 6, y, width / 2f + 6, height, 6, bgGradientColor, additionalSecondColorWithAlpha);
        ScissorService.disableScissor();
        // outline
        RenderService.drawRoundedOutlineRect(matrixStack, x, y, width, height, 6, 1, bgOutlineColor);
        ScissorService.enableScissor(x, y, width / 2, height);
        RenderService.drawRoundedOutlineHorLinearGradientRect(matrixStack, x, y, width / 2f + 6, height, 6, 1, additionalSecondColorWithAlpha, bgOutlineGradientColor);
        ScissorService.disableScissor();
        ScissorService.enableScissor(x + width / 2, y, width / 2, height);
        RenderService.drawRoundedOutlineHorLinearGradientRect(matrixStack, x + width / 2f - 6, y, width / 2f + 6, height, 6, 1, bgOutlineGradientColor, additionalSecondColorWithAlpha);
        ScissorService.disableScissor();


//        // lines
//        final float linesWidth = (float) (30);
//        RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/lines.png"), x + width - linesWidth * 2, y + 1, linesWidth, height - 2, linesColor);

        // text
        final float fontSize = (float) (12);
        SFPD_MEDIUM.drawCenteredText(matrixStack, getMessage().getString(), x + width / 2f, y - SFPD_MEDIUM.getHeight(fontSize) / 2 + height / 2f,
                ColorService.getColorWithAlpha(-1, 0.5f + 0.5f * hoverAnimation.getAnimationValue()), fontSize);
    }

}
