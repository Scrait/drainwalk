package tech.drainwalk.services.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

public class GLService implements IInstanceAccess {

    public static final GLService INSTANCE = new GLService();

    public void rescale(double factor) {
        if (Minecraft.IS_RUNNING_ON_MAC) factor *= 2;
        rescale(mc.getMainWindow().getFramebufferWidth() / factor, mc.getMainWindow().getFramebufferHeight() / factor);
    }

    public void rescaleMC() {
        MainWindow mainWindow = mc.getMainWindow();
        rescale(mainWindow.getFramebufferWidth() / mainWindow.getGuiScaleFactor(), mainWindow.getFramebufferHeight() / mainWindow.getGuiScaleFactor());
    }

    public void rescale(double width, double height) {
        RenderSystem.clear(256, Minecraft.IS_RUNNING_ON_MAC);
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        RenderSystem.matrixMode(5888);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
    }

    public void scaleAnimation(MatrixStack matrixStack, float x, float y, float width, float height, float scaleValue) {
        matrixStack.translate((x + width / 2), (y + height / 2), 0);
        matrixStack.scale(scaleValue, scaleValue, scaleValue);
        matrixStack.translate(-(x + width / 2), -(y + height / 2), 0);
    }

    public void scaleAnimation(float x, float y, float width, float height, float scaleValue) {
        RenderSystem.translatef((x + width / 2), (y + height / 2), 0);
        RenderSystem.scalef(scaleValue, scaleValue, scaleValue);
        RenderSystem.translatef(-(x + width / 2), -(y + height / 2), 0);
    }

    public Vector2f normalizeCords(double mouseX, double mouseY, double factor) {
        if (Minecraft.IS_RUNNING_ON_MAC) factor *= 2;
        return new Vector2f((float) (mouseX * mc.getMainWindow().getGuiScaleFactor() / factor), (float) (mouseY * mc.getMainWindow().getGuiScaleFactor() / factor));
    }

    public void setupRenderState() {
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
    }

    public void resetRenderState() {
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        RenderSystem.clearCurrentColor();
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableAlphaTest();
    }

}
