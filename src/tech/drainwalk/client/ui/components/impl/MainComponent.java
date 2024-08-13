package tech.drainwalk.client.ui.components.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.client.ui.UIMain;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;

public class MainComponent extends Component {

    public MainComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderService.drawRoundedDiagonalGradientRect(matrixStack, x, y, width, height, parent.getROUND(), backgroundFirstColor, backgroundSecondColor);
        final int uiBorderColorWithAlpha = ColorService.getColorWithAlpha(uiBorderColor, 0);
        RenderService.drawRoundedDiagonalGradientRect(matrixStack, x, y, width, height, parent.getROUND(), 1, uiBorderColor, uiBorderColorWithAlpha);

        // TODO mb find more logical place for this separators
        // left area separator
        final float leftAreaWidth = 64;
        RenderService.drawGradintRect(matrixStack, x + leftAreaWidth, y, 1, height, uiBorderColor, uiBorderColorWithAlpha);
        // header separator
        RenderService.drawGradintRect(matrixStack, x + leftAreaWidth, y + 59, width - leftAreaWidth, 1, uiBorderColorWithAlpha, uiBorderColor, uiBorderColor, uiBorderColorWithAlpha);
    }

    @Override
    public void tick() {}

    @Override
    public void charTyped(char codePoint, int modifiers) {}

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {}

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {}

}
