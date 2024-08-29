package tech.drainwalk.services.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.IShaders;
import tech.drainwalk.utils.shader.Shader;

@UtilityClass
public class RenderService extends AbstractGui implements IInstanceAccess, IShaders {

    public void drawRoundedRectWithOutline(MatrixStack matrixStack, float x, float y, float width, float height, float round, float thickness, int color, int outlineColor) {
//        drawRoundedRect(matrixStack, x, y, width, height, round, color);
//        drawRoundedOutlineRect(matrixStack, x - 0.5f, y - 0.5f, width + 1, height + 1, round, thickness, outlineColor);
    }

    public void drawCircle(MatrixStack matrixStack, float x, float y, float start, float end, float radius, int color, int linewidth) {
    }

    public void drawRoundedShadow(MatrixStack matrixStack, float x, float y, float width, float height, float softness, float radius, int color) {
        float[] c = ColorService.getRGBAf(color);
        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();

        SHADOW.useProgram();
        SHADOW.setupUniform2f("size", (width - radius) * 2, (height - radius) * 2);
        SHADOW.setupUniform1f("softness", softness);
        SHADOW.setupUniform1f("radius", radius);
        SHADOW.setupUniform4f("color", c[0], c[1], c[2], c[3]);

        allocateTextureRect(matrixStack, x - (softness / 2f), y - (softness / 2f), width + (softness), height + (softness));
        SHADOW.unloadProgram();

        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }

    public void drawGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, int colorFrom, int colorTo) {
        drawGradientRect(matrixStack, x, y, width, height, colorFrom, colorFrom, colorTo, colorTo);
    }

    public void drawGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, int colorFrom, int colorTo, int colorFrom2, int colorTo2) {
        matrixStack.push();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(width, height, 1);
        fillGradient(matrixStack, 0, 0, 1, 1, colorFrom, colorTo, colorFrom2, colorTo2);
        matrixStack.pop();
    }

    public void drawRoundedRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, int color) {
        drawRoundedGradientRect(matrixStack, x, y, width, height, round, color, color, color, color);
    }

    public void drawRoundedHorLinearGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, int colorLeft, int colorRight) {
        drawRoundedGradientRect(matrixStack, x, y, width, height, round, colorLeft, colorRight, colorRight, colorLeft);
    }

    public void drawRoundedVerLinearGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, int colorTop, int colorBottom) {
        drawRoundedGradientRect(matrixStack, x, y, width, height, round, colorTop, colorTop, colorBottom, colorBottom);
    }

    public void drawRoundedDiagonalGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, int color1, int color2) {
        final int cornerColor = ColorService.interpolateColor(color1, color2, 0.5);
        drawRoundedGradientRect(matrixStack, x, y, width, height, round, color1, cornerColor, color2, cornerColor);
    }

    public void drawRoundedGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, int color1, int color2, int color3, int color4) {
        drawRoundedGradientRect(matrixStack, x, y, width, height, round, round, round, round, color1, color2, color3, color4);
    }

    public void drawRoundedGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float roundTopLeft, float roundTopRight, float roundBottomRight, float roundBottomLeft, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorService.getRGBAf(color1);
        float[] c2 = ColorService.getRGBAf(color2);
        float[] c3 = ColorService.getRGBAf(color4);
        float[] c4 = ColorService.getRGBAf(color3);

        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.0f);
        ROUNDED_GRADIENT.useProgram();
        ROUNDED_GRADIENT.setupUniform2f("size", width, height);
        ROUNDED_GRADIENT.setupUniform4f("round", roundTopLeft, roundTopRight, roundBottomRight, roundBottomLeft);
        ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
        ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
        ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
        ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);

        allocateTextureRect(matrixStack, x, y, width, height);

        ROUNDED_GRADIENT.unloadProgram();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }

    public void drawRoundedOutlineRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, float outlineWidth, int color) {
        drawRoundedOutlineGradientRect(matrixStack, x, y, width, height, round, outlineWidth, color, color, color, color);
    }

    public void drawRoundedOutlineHorLinearGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, float outlineWidth, int colorLeft, int colorRight) {
        drawRoundedOutlineGradientRect(matrixStack, x, y, width, height, round, outlineWidth, colorLeft, colorRight, colorRight, colorLeft);
    }

    public void drawRoundedOutlineVerLinearGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, float outlineWidth, int colorTop, int colorBottom) {
        drawRoundedOutlineGradientRect(matrixStack, x, y, width, height, round, outlineWidth, colorTop, colorTop, colorBottom, colorBottom);
    }

    public void drawRoundedOutlineDiagonalGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, float outlineWidth, int color1, int color2) {
        final int cornerColor = ColorService.interpolateColor(color1, color2, 0.5);
        drawRoundedOutlineGradientRect(matrixStack, x, y, width, height, round, outlineWidth, color1, cornerColor, color2, cornerColor);
    }

    public void drawRoundedOutlineGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float round, float outlineWidth, int color1, int color2, int color3, int color4) {
        drawRoundedOutlineGradientRect(matrixStack, x, y, width, height, round, round, round, round, outlineWidth, color1, color2, color3, color4);
    }

    public void drawRoundedOutlineGradientRect(MatrixStack matrixStack, float x, float y, float width, float height, float roundTopLeft, float roundTopRight, float roundBottomRight, float roundBottomLeft, float outlineWidth, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorService.getRGBAf(color1);
        float[] c2 = ColorService.getRGBAf(color2);
        float[] c3 = ColorService.getRGBAf(color4);
        float[] c4 = ColorService.getRGBAf(color3);

        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.0f);
        ROUNDED_OUTLINE.useProgram();
        ROUNDED_OUTLINE.setupUniform2f("size", width, height);
        ROUNDED_OUTLINE.setupUniform4f("round", roundTopLeft, roundTopRight, roundBottomRight, roundBottomLeft);
        ROUNDED_OUTLINE.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
        ROUNDED_OUTLINE.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
        ROUNDED_OUTLINE.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
        ROUNDED_OUTLINE.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
        ROUNDED_OUTLINE.setupUniform1f("outlineWidth", outlineWidth);

        allocateTextureRect(matrixStack, x, y, width, height);

        ROUNDED_OUTLINE.unloadProgram();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }

    public void drawRect(MatrixStack matrixStack, float x, float y, float width, float height, int color) {
        matrixStack.push();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(width, height, 1);
        fill(matrixStack, 0, 0, 1, 1, color);
        matrixStack.pop();
    }

    public void drawRoundedTexture(MatrixStack matrixStack, ResourceLocation tex, float x, float y, float x2, float y2, float round, float alpha) {
        RenderSystem.color4f(100, 234, 1, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        RenderSystem.disableAlphaTest();
        ROUNDED_TEXTURE.useProgram();
        ROUNDED_TEXTURE.setupUniform2f("size", (x2 - round) * 2, (y2 - round) * 2);
        ROUNDED_TEXTURE.setupUniform1f("round", round);
        ROUNDED_TEXTURE.setupUniform1f("alpha", alpha);
        drawImage(matrixStack, tex, x, y, x2, y2);
        ROUNDED_TEXTURE.unloadProgram();
        RenderSystem.disableBlend();
    }

    public void drawImage(MatrixStack matrixStack, ResourceLocation resourceLocation, float x, float y, float width, float height) {
        drawImage(matrixStack, resourceLocation, x, y, width, height, ColorService.rgbaFloat(1, 1, 1, 1));
    }

    public void drawImage(MatrixStack matrixStack, ResourceLocation resourceLocation, float x, float y, float width, float height, int color) {
        mc.getTextureManager().bindTexture(resourceLocation);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        final float[] c = ColorService.getRGBAf(color);
        RenderSystem.color4f(c[0], c[1], c[2], c[3]);

        allocateTextureRect(matrixStack, x, y, width, height);

        RenderSystem.disableBlend();
    }

    public void drawImage(MatrixStack matrixStack, ResourceLocation resourceLocation, float x, float y, float width, float height,
                                 int color1, int color2, int color3, int color4) {
        float[] c1 = ColorService.getRGBAf(color2);
        float[] c2 = ColorService.getRGBAf(color1);
        float[] c3 = ColorService.getRGBAf(color4);
        float[] c4 = ColorService.getRGBAf(color3);
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        mc.getTextureManager().bindTexture(resourceLocation);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        {
            bufferbuilder.pos(matrix4f, x, y, 0).tex(0, 0).color(c1[0], c1[1], c1[2], c1[3]).endVertex();
            bufferbuilder.pos(matrix4f, x, y + height, 0).tex(0, 1).color(c2[0], c2[1], c2[2], c2[3]).endVertex();
            bufferbuilder.pos(matrix4f, x + width, y + height, 0).tex(1, 1).color(c3[0], c3[1], c3[2], c3[3]).endVertex();
            bufferbuilder.pos(matrix4f, x + width, y, 0).tex(1, 0).color(c4[0], c4[1], c4[2], c4[3]).endVertex();
        }
        tessellator.draw();

        RenderSystem.shadeModel(7424);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.popMatrix();
    }

    public void drawProgressBar(MatrixStack matrixStack, float x, float y, float width, float height, float progress, int color) {
        float[] c = ColorService.getRGBAf(color);
        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        PROGRESS_BAR.useProgram();
        PROGRESS_BAR.setupUniform2f("iResolution", width, height);
//        PROGRESS_BAR.setupUniform2f("resolution", mc.getMainWindow().getScaledWidth(), mc.getMainWindow().getScaledHeight());
        PROGRESS_BAR.setupUniform1f("iTime", progress * 10);
//        PROGRESS_BAR.setupUniform("iProgress", progress);
//            PROGRESS_BAR.drawQuads();
//        allocateTextureRect(matrixStack, x, y, width, height);
        PROGRESS_BAR.unloadProgram();
        RenderSystem.disableBlend();
    }

    public void drawMainMenuShader(float width, float height, int mouseX, int mouseY) {
        MAIN_MENU.useProgram();
        MAIN_MENU.setupUniform2f("u_resolution", width, height); // Set screen resolution
        MAIN_MENU.setupUniform2f("u_mouse", mouseX, mouseY); // Set mouse position
        MAIN_MENU.setupUniform1f("u_time", (float) (System.currentTimeMillis() % 100000) / 1000f); // Set time

        // Set color uniforms
        // Значения для u_colors[0]
        float[] color1 = {
                0.4980392156862745f,
                0.47843137254901963f,
                0.611764705882353f,
                1
        };

        // Значения для u_colors[1]
        float[] color2 = {
                0.36470588235294116f,
                0.3254901960784314f,
                0.611764705882353f,
                1
        };

        // Установка униформы u_colors[0]
        MAIN_MENU.setupUniform4f("u_colors[0]", color1[0], color1[1], color1[2], color1[3]);

        // Установка униформы u_colors[1]
        MAIN_MENU.setupUniform4f("u_colors[1]", color2[0], color2[1], color2[2], color2[3]);

        MAIN_MENU.setupUniform1f("u_intensity", 0.6f);
        MAIN_MENU.setupUniform1f("u_rays", 0.078f);
        MAIN_MENU.setupUniform1f("u_reach", 0.061f);
        Shader.drawQuads();
        MAIN_MENU.unloadProgram();
    }



    //utils functions

    private void allocateTextureRect(MatrixStack matrixStack, float x, float y, float width, float height) {
        matrixStack.push();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(width, height, 1);
        blit(matrixStack, 0, 0, 0, 0, 1, 1, 1, 1);
        matrixStack.pop();
    }

}
