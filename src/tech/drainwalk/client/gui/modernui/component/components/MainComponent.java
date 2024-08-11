package tech.drainwalk.client.gui.modernui.component.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.gui.modernui.component.Component;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.StencilService;

public class MainComponent extends Component {

    public MainComponent(float x, float y, float width, float height, MenuMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        NORMAL_POST_BLOOM_RUNNABLES.add(() -> RenderService.drawRoundedGradientRect(matrixStack, x, y, width, height, 15, ClientColor.panelMain, ClientColor.panel, ClientColor.panelMain, ClientColor.panel));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1, 1, 1, 0.5f);
        RenderService.drawRoundedRect(matrixStack, x, y, width, height, 15, ClientColor.panelMain);

//        RenderService.drawRoundedGradientRect(matrixStack, x, y, 120, height, 15, ColorService.getColorWithAlpha(ClientColor.panelMain, parent.getRandAnimation().getAnimationValue()), ColorService.getColorWithAlpha(ClientColor.panelMain, parent.getRandAnimation().getAnimationValue()), ColorService.getColorWithAlpha(ClientColor.panelMain, parent.getRandAnimation().getAnimationValue()), ColorService.getColorWithAlpha(ClientColor.panel, parent.getRandAnimation().getAnimationValue()));
//        RenderService.drawRoundedGradientRect(matrixStack, x + 110, y, width - 110, height, 15, ColorService.getColorWithAlpha(ClientColor.panelMain, parent.getRandAnimation().getAnimationValue()), ColorService.getColorWithAlpha(ClientColor.panel, parent.getRandAnimation().getAnimationValue()), ColorService.getColorWithAlpha(ClientColor.panelMain, parent.getRandAnimation().getAnimationValue()), ColorService.getColorWithAlpha(ClientColor.panelMain, parent.getRandAnimation().getAnimationValue()));
        StencilService.initStencilToWrite();
        RenderService.drawRoundedRect(matrixStack, x, y, width, height, 15, ClientColor.panelMain);
        StencilService.readStencilBuffer(1);
        RenderService.drawImage(matrixStack, new ResourceLocation("drainwalk/images/pattern.png"), x + 115, y, width, height, -1);
        StencilService.uninitStencilBuffer();
        RenderService.drawRoundedOutlineRect(matrixStack, x - 1, y - 1, width + 2, height + 2, 15, 1.5f, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.3f));
        RenderService.drawRect(matrixStack, x + 115, y, 1f, height, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.3f));
        RenderService.drawRect(matrixStack, x, y + height - 30, 115, 1f, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.3f));
        FontManager.SEMI_BOLD_18.drawCenteredString(matrixStack, dw.getClientInfo().getCLIENT_NAME(), x + (float) 115 / 2, y + height - 17, ClientColor.textMain);
        mc.getMainWindow().setRawMouseInput(true);
        RenderSystem.disableBlend();
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
