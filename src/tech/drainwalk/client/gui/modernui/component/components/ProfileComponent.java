package tech.drainwalk.client.gui.modernui.component.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.gui.modernui.component.Component;

public class ProfileComponent extends Component {

    public ProfileComponent(float x, float y, float width, float height, MenuMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //RenderUtils.drawRect(x, y, width, height, -1);
        //RenderUtils.drawRoundedTexture(new ResourceLocation("drainwalk/images/bebrik.png"), x + 10, y + 15, 20, 20, 14, 1);
        //CustomFontRenderer customFontRenderer = new CustomFontRenderer();

        RenderService.drawRoundedTexture(matrixStack, new ResourceLocation("drainwalk/images/bebrik.png"), x + 10, y + 15, 20, 20, 9, 1);
        RenderService.drawCircle(matrixStack, x + 20, y + 25, 0, 360, 11, ClientColor.textMain, 1);
        FontManager.MEDIUM_20.drawString(matrixStack, dw.getProfile().getUSERNAME(), x + 40, y + 18, ClientColor.textMain);
        FontManager.MEDIUM_16.drawString(matrixStack, dw.getProfile().getSUB_TYPE(), x + 40, y + 28, ClientColor.textMain);
        //new RenderService(matrixStack).renderRoundedGradientRect(x + 0.5f, y, width, height, 5, 1, ClientColor.main, ClientColor.panel, ClientColor.main, ClientColor.main);
        //customFontRenderer.drawString(matrixStack, "ggqokertonkmvbs`09470651.", x, y, -1);
        //new RenderService(matrixStack).renderCircle((int) (x + 10), (int) (y + 15), 10, -1);
        // RenderUtils.drawC
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

}
