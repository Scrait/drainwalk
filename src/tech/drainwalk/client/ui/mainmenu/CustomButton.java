package tech.drainwalk.client.ui.mainmenu;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;

import javax.annotation.Nonnull;

public class CustomButton extends Button implements IFonts, IInstanceAccess {

    private final Animation hoverAnimation = new Animation();
    private final boolean isAlternativeButton;

    public CustomButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, boolean isAlternativeButton) {
        super(x, y, width, height, title, pressedAction);
        this.isAlternativeButton = isAlternativeButton;
    }

    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        // anim
        // TODO: update method must be called in tick method
        hoverAnimation.update(isHovered);

        hoverAnimation.animate(0, 1, 0.04f, EasingList.NONE, partialTicks);

        // colors
        final int hoveredBgFirstColor = isAlternativeButton ? ColorService.hexToRgb("#FF4242") : ColorService.hexToRgb("#9CB6DD");
        final int hoveredBgSecondColor = isAlternativeButton ? ColorService.hexToRgb("#FF7A7A") : ColorService.hexToRgb("#BAD4FB");
        final int bgFirstColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(-1, 0.05f), hoveredBgFirstColor, hoverAnimation.getAnimationValue());
        final int bgSecondColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(-1, 0.05f), hoveredBgSecondColor, hoverAnimation.getAnimationValue());
        final int linesColor = ColorService.interpolateColor(ColorService.getColorWithAlpha(-1, 0.02f), ColorService.hexToRgb("#92ACD3"), hoverAnimation.getAnimationValue());

        // bg
        RenderService.drawRoundedHorLinearGradientRect(matrixStack, x, y, width, height, 4, bgFirstColor, bgSecondColor);

        // lines
        if (!isAlternativeButton) {
            final float linesWidth = (float) (30 / mc.getMainWindow().getGuiScaleFactor());
            RenderService.drawImage(matrixStack, new DrainwalkResource("textures/mainmenu/lines.png"), x + width - linesWidth * 2, y + 1, linesWidth, height - 2, linesColor);
        }

        // text
        final float fontSize = (float) (12 / mc.getMainWindow().getGuiScaleFactor());
        SFPD_MEDIUM.drawCenteredText(matrixStack, getMessage().getString(), x + width / 2f, y - SFPD_MEDIUM.getHeight(fontSize) / 2 + height / 2f,
                ColorService.getColorWithAlpha(ColorService.hexToRgb("#F2F7FB"), 0.5f + 0.5f * hoverAnimation.getAnimationValue()), fontSize);
    }

}
