package tech.drainwalk.utils.render;

import com.jhlabs.image.GaussianFilter;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.services.render.StencilService;
import tech.drainwalk.utils.Utils;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.shader.Shader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils extends Utils {
    private static final Tessellator TESSELLATOR = Tessellator.getInstance();
    private static final BufferBuilder BUILDER = TESSELLATOR.getBuffer();
    private static final HashMap<Integer, Integer> shadowCache = new HashMap<>();

    private static final Shader ROUNDED = new Shader("drainwalk/shaders/rounded.fsh", true);
    private static final Shader ROUNDED_GRADIENT = new Shader("drainwalk/shaders/rounded_gradient.fsh", true);
    private static final Shader ROUNDED_OUTLINE = new Shader("drainwalk/shaders/rounded_outline.fsh", true);
    private static final Shader SHADOW = new Shader("drainwalk/shaders/shadow.fsh", true);
    private static final Shader CIRCLE = new Shader("drainwalk/shaders/outline_circle.fsh", true);
    private static final Shader ROUNDED_TEXTURE = new Shader("drainwalk/shaders/rounded_texture.fsh", true);


    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
        drawRoundedGradientRect(x, y, x2, y2, round, 1, color, color, color, color);
    }

    public static void drawRoundedRectWithOutline(float x, float y, float x2, float y2, float round, float thickness, int color, int outlineColor) {
        drawRoundedGradientRect(x, y, x2, y2, round, 1, color, color, color, color);
        drawRoundedOutlineRect(x - 0.5f, y - 0.5f, x2 + 1, y2 + 1, round, thickness, outlineColor);
    }

    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, float swapX, float swapY, int firstColor, int secondColor) {
        float[] c = ColorService.getRGBAf(firstColor);
        float[] c1 = ColorService.getRGBAf(secondColor);

        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);

        ROUNDED.useProgram();
        ROUNDED.setupUniform2f("size", (x2 - round) * 2, (y2 - round) * 2);
        ROUNDED.setupUniform1f("round", round);
        ROUNDED.setupUniform2f("smoothness", 0.f, 1.5f);
        ROUNDED.setupUniform2f("swap", swapX, swapY);
        ROUNDED.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
        ROUNDED.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c[3]);

        allocTextureRectangle(x, y, x2, y2);
        ROUNDED.unloadProgram();

        RenderSystem.disableBlend();
    }

    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, int color1, int color2, int color3, int color4) {
        drawRoundedGradientRect(x, y, x2, y2, round, 1, color1, color2, color3, color4);
    }

    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, float value, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorService.getRGBAf(color1);
        float[] c2 = ColorService.getRGBAf(color2);
        float[] c3 = ColorService.getRGBAf(color3);
        float[] c4 = ColorService.getRGBAf(color4);

        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        ROUNDED_GRADIENT.useProgram();
        ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2, y2 * 2);
        ROUNDED_GRADIENT.setupUniform4f("round", round, round, round, round);
        ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.f, 1.5f);
        ROUNDED_GRADIENT.setupUniform1f("value", value);
        ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
        ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
        ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
        ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);

        allocTextureRectangle(x, y, x2, y2);
        ROUNDED_GRADIENT.unloadProgram();
        RenderSystem.disableBlend();
    }

    public static void drawRoundedShadow(float x, float y, float x2, float y2, float softness, float radius, int color) {
        float[] c = ColorService.getRGBAf(color);
        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);

        SHADOW.useProgram();
        SHADOW.setupUniform2f("size", (x2 - radius) * 2, (y2 - radius) * 2);
        SHADOW.setupUniform1f("softness", softness);
        SHADOW.setupUniform1f("radius", radius);
        SHADOW.setupUniform4f("color", c[0], c[1], c[2], c[3]);

        allocTextureRectangle(x - (softness / 2f), y - (softness / 2f), x2 + (softness), y2 + (softness));
        SHADOW.unloadProgram();

        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }

    public static void drawRoundedOutlineShadow(float x, float y, float x2, float y2, float softness, float radius, int color) {
        x -= 4;
        y -= 4;
        x2 += 8;
        y2 += 8;
        StencilService.initStencilToWrite();
        drawRoundedOutlineRect(x, y, x2, y2, radius + 3, 4f, color);
        StencilService.readStencilBuffer(1);
        drawRoundedShadow(x + 2, y + 2, x2 - 4, y2 - 4, softness, radius, color);
        StencilService.uninitStencilBuffer();
    }

    public static void drawRoundedOutlineRect(float x, float y, float x2, float y2, float round, float thickness, int color) {
        float[] c = ColorService.getRGBAf(color);

        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        ROUNDED_OUTLINE.useProgram();
        ROUNDED_OUTLINE.setupUniform2f("size", x2 * 2, y2 * 2);
        ROUNDED_OUTLINE.setupUniform1f("round", round);
        ROUNDED_OUTLINE.setupUniform1f("thickness", thickness);
        ROUNDED_OUTLINE.setupUniform2f("smoothness", thickness - 1.5f, thickness);
        ROUNDED_OUTLINE.setupUniform4f("color", c[0], c[1], c[2], c[3]);
        allocTextureRectangle(x, y, x2, y2);
        ROUNDED_OUTLINE.unloadProgram();
        RenderSystem.disableBlend();
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, float borderThickness, int color) {
        drawARCCircle(x, y, radius, progress, 100, borderThickness, color, color);
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, int change, float borderThickness, int color) {
        drawARCCircle(x, y, radius, progress, change, borderThickness, color, color);
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, float borderThickness, int firstColor, int secondColor) {
        drawARCCircle(x, y, radius, progress, 100, borderThickness, firstColor, secondColor);
    }

    public static void drawARCCircle(float x, float y, float radius, float progress, int change, float borderThickness, int firstColor, int secondColor) {
        float[] c = ColorService.getRGBAf(firstColor);
        float[] c1 = ColorService.getRGBAf(secondColor);
        RenderSystem.color4f(0, 0, 0, 0);
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        CIRCLE.useProgram();
        CIRCLE.setupUniform2f("pos", x * 2 - radius, ((Minecraft.getInstance().getMainWindow().getHeight() - (radius * 2)) - (y * 2)) + radius - 1);
        CIRCLE.setupUniform1f("radius", radius);
        CIRCLE.setupUniform1f("radialSmoothness", 1.0f);
        CIRCLE.setupUniform1f("borderThickness", borderThickness);
        CIRCLE.setupUniform1f("progress", progress);
        CIRCLE.setupUniform1i("change", change);
        CIRCLE.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
        CIRCLE.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c1[3]);
        CIRCLE.setupUniform2f("gradient", 0.2f, 2f);
        allocTextureRectangle(0, 0, mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());
        CIRCLE.unloadProgram();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }

    public static void drawCircle(final float x, final float y, float start, float end, final float radius, final int color, final int linewidth) {
        RenderSystem.color4f(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            final float endOffset = end;
            end = start;
            start = endOffset;
        }
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderUtils.enableSmoothLine((float)linewidth);
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        GL11.glBegin(3);
        for (float i = end; i >= start; i -= 4.0f) {
            ColorService.glHexColor(color, ColorService.getRGBAf(color)[3] * 255);
            final float cos = (float)(Math.cos(i * Math.PI / 180.0) * radius * 1.0);
            final float sin = (float)(Math.sin(i * Math.PI / 180.0) * radius * 1.0);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        RenderUtils.disableSmoothLine();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawLoadingCircle(final float x, final float y, float start, float end, final float radius, final int color, final int linewidth) {
        RenderSystem.color4f(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            final float endOffset = end;
            end = start;
            start = endOffset;
        }
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderUtils.enableSmoothLine((float)linewidth);
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        GL11.glBegin(3);

        float alpha = ColorService.getRGBAf(color)[3] * 255;
        for (float i = end; i >= start; i -= 4.0f) {
            alpha -= 2.8f;
            ColorService.glHexColor(color, alpha);
            final float cos = (float)(Math.cos(i * 3.141592653589793 / 180.0) * radius * 1.0);
            final float sin = (float)(Math.sin(i * 3.141592653589793 / 180.0) * radius * 1.0);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        RenderUtils.disableSmoothLine();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawImage(ResourceLocation tex, float x, float y, float x2, float y2) {
        mc.getTextureManager().bindTexture(tex);
        RenderSystem.color4f(1, 1, 1, 1);
        allocTextureRectangle(x, y, x2, y2);
        RenderSystem.bindTexture(0);
    }

    public static void drawImage(ResourceLocation tex, float x, float y, float x2, float y2, int color) {
        mc.getTextureManager().bindTexture(tex);
        float[] colorRGBA = ColorService.getRGBAf(color);
        RenderSystem.color4f(colorRGBA[0], colorRGBA[1], colorRGBA[2], colorRGBA[3]);
        allocTextureRectangle(x, y, x2, y2);
        RenderSystem.bindTexture(0);
    }

    public static void drawRoundedTexture(ResourceLocation tex,float x, float y, float x2, float y2, float round, float alpha) {
        RenderSystem.color4f(100, 234, 1, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        RenderSystem.disableAlphaTest();
        ROUNDED_TEXTURE.useProgram();
        ROUNDED_TEXTURE.setupUniform2f("size", (x2 - round) * 2, (y2 - round) * 2);
        ROUNDED_TEXTURE.setupUniform1f("round", round);
        ROUNDED_TEXTURE.setupUniform1f("alpha", alpha);
        drawImage(tex,x,y,x2,y2);
        ROUNDED_TEXTURE.unloadProgram();

        RenderSystem.disableBlend();
    }

    public static void drawRoundedTextureHead(ResourceLocation tex, float x, float y, float x2, float y2, float round, float alpha) {
        RenderSystem.color4f(100, 234, 1, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        RenderSystem.disableAlphaTest();
        ROUNDED_TEXTURE.useProgram();
        ROUNDED_TEXTURE.setupUniform2f("size", (x2 - round) * 2, (y2 - round) * 2);
        ROUNDED_TEXTURE.setupUniform1f("round", round);
        ROUNDED_TEXTURE.setupUniform1f("alpha", alpha);
        mc.getTextureManager().bindTexture(tex);
        RenderSystem.color4f(1, 1, 1, 1);
        allocTextureRectangle(x, y, x2, y2);
        drawScaledCustomSizeModalRect((int) x, (int) y, 8f, 8f, (int) 8f, (int) 8f, (int) x2, (int) y2, (int) 64f, (int) 64f);
        RenderSystem.bindTexture(0);
        ROUNDED_TEXTURE.unloadProgram();
        RenderSystem.disableBlend();
    }

    public static void allocTextureRectangle(float x, float y, float width, float height) {
        if (mc.gameSettings.ofFastRender) return;
        BUILDER.begin(7, DefaultVertexFormats.POSITION_TEX);
        BUILDER.pos(x, y, 0).tex(0, 0).endVertex();
        BUILDER.pos(x, y + height, 0).tex(0, 1).endVertex();
        BUILDER.pos(x + width, y + height, 0).tex(1, 1).endVertex();
        BUILDER.pos(x + width, y, 0).tex(1, 0).endVertex();
        TESSELLATOR.draw();
    }

    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float width, float height, int color) {
        drawGradientRect(x, y, width, height, color, color, color, color);
    }

    public static void drawVRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        RenderSystem.color4f(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawFace(int alpha, float d, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight, AbstractClientPlayerEntity target) {
        try {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            ResourceLocation skin = target.getLocationSkin();
            Minecraft.getInstance().getTextureManager().bindTexture(skin);
            final float hurtPercent = getHurtPercent(target);
            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, alpha / 255f);
            //drawScaledCustomSizeModalRect((int) d, (int) y, u, v, (int) uWidth, (int) vHeight, (int) width, (int) height, tileWidth, tileHeight);
            drawScaledCustomSizeModalRect((int) d, (int) y, u, v, (int) uWidth, (int) vHeight, (int) width, (int) height, (int) tileWidth, (int) tileHeight);
            RenderSystem.clearCurrentColor();
            GL11.glPopMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((u * f), ((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex(((u + (float)uWidth) * f), ((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex(((u + (float)uWidth) * f), (v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((u * f), (v * f1)).endVertex();
        tessellator.draw();
    }

    private static float getRenderHurtTime(LivingEntity hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.getTimer().renderPartialTicks : 0.0f);
    }

    private static float getHurtPercent(LivingEntity hurt) {
        return getRenderHurtTime(hurt) / (float) 15;
    }

    public static void drawCRect(float x, float y, float width, float height, int color) {
        drawGradientRect(x, y, width - x, height, color, color, color, color);
    }

    public static void drawGradientRect(float x, float y, float width, float height, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorService.getRGBAf(color1);
        float[] c2 = ColorService.getRGBAf(color2);
        float[] c3 = ColorService.getRGBAf(color3);
        float[] c4 = ColorService.getRGBAf(color4);

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);

        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(x, height + y, 0.0D).color(c1[0], c1[1], c1[2], c1[3]).endVertex();
        BUILDER.pos(width + x, height + y, 0.0D).color(c2[0], c2[1], c2[2], c2[3]).endVertex();
        BUILDER.pos(width + x, y, 0.0D).color(c3[0], c3[1], c3[2], c3[3]).endVertex();
        BUILDER.pos(x, y, 0.0D).color(c4[0], c4[1], c4[2], c4[3]).endVertex();
        TESSELLATOR.draw();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawAnimatedGradientRect(float x, float y, float width, float height, int speed, int index, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorService.getRGBAf(ColorService.interpolateColorsBackAndForth(speed, index, color1, color2));
        float[] c2 = ColorService.getRGBAf(ColorService.interpolateColorsBackAndForth(speed, index, color2, color3));
        float[] c3 = ColorService.getRGBAf(ColorService.interpolateColorsBackAndForth(speed, index, color4, color4));
        float[] c4 = ColorService.getRGBAf(ColorService.interpolateColorsBackAndForth(speed, index, color4, color1));

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);

        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(x, height + y, 0.0D).color(c1[0], c1[1], c1[2], c1[3]).endVertex();
        BUILDER.pos(width + x, height + y, 0.0D).color(c2[0], c2[1], c2[2], c2[3]).endVertex();
        BUILDER.pos(width + x, y, 0.0D).color(c3[0], c3[1], c3[2], c3[3]).endVertex();
        BUILDER.pos(x, y, 0.0D).color(c4[0], c4[1], c4[2], c4[3]).endVertex();
        TESSELLATOR.draw();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRectForMac(float x, float y, float width, float height, int softness, int color) {
        float[] c = ColorService.getRGBAf(color);

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);

        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(x, height + y, 0.0D).color(c[0], c[1], c[2], (c[3] - 150)).endVertex();
        BUILDER.pos(width + x, height + y, 0.0D).color(c[0], c[1], c[2], (c[3] - 170)).endVertex();
        BUILDER.pos(width + x, y, 0.0D).color(c[0], c[1], c[2], (c[3] - 170)).endVertex();
        BUILDER.pos(x, y, 0.0D).color(c[0], c[1], c[2], (c[3] - 170)).endVertex();
        //allocTextureRectangleMac(x - (softness / 2f), y - (softness / 2f), width + (softness), height + (softness));
        TESSELLATOR.draw();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    public static void drawTriangleSingle(float x, float z, float distance, int color) {
        float pt = mc.getRenderPartialTicks();
        float playerX = (float) (mc.player.prevPosX + (mc.player.getPosX() - mc.player.prevPosX) * pt);
        float playerZ = (float) (mc.player.prevPosZ + (mc.player.getPosZ() - mc.player.prevPosZ) * pt);
        float playerYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * pt;
        float radian = (float) (Math.atan2(z - playerZ, x - playerX) - Math.toRadians(playerYaw + 180));
        float degree = (float) Math.toDegrees(radian);
        float cos = MathHelper.cos(radian);
        float sin = MathHelper.sin(radian);
        float centerX = mc.getMainWindow().getWidth() / 4f;
        float centerY = mc.getMainWindow().getHeight() / 4f;
        RenderSystem.enableBlend();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(centerX + distance * cos, centerY + distance * sin, 0);
        RenderSystem.rotatef(degree + 90, 0, 0, 1);
        float[] colors = ColorService.getRGBAf(color);
        float width = 6, height = 12;
        RenderSystem.disableTexture();
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(0, 0 - height, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        BUILDER.pos(0 - width, 0, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        BUILDER.pos(0, -3, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        float r = Math.max(colors[0] - 0.1f, 0);
        float g = Math.max(colors[1] - 0.1f, 0);
        float b = Math.max(colors[2] - 0.1f, 0);
        BUILDER.pos(0, 0 - height, 0).color(r, g, b, colors[3]).endVertex();
        BUILDER.pos(0, -3, 0).color(r, g, b, colors[3]).endVertex();
        BUILDER.pos(0 + width, 0, 0).color(r, g, b, colors[3]).endVertex();
        TESSELLATOR.draw();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
        RenderSystem.disableBlend();
    }

    public static void drawTriangleCustom(int color) {
        RenderSystem.enableBlend();
        float[] colors = ColorService.getRGBAf(color);
        float width = 6, height = 12;
        RenderSystem.disableTexture();
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        BUILDER.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        BUILDER.pos(0, 0 - height, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        BUILDER.pos(0 - width, 0, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        BUILDER.pos(0, -3, 0).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        float r = Math.max(colors[0] - 0.1f, 0);
        float g = Math.max(colors[1] - 0.1f, 0);
        float b = Math.max(colors[2] - 0.1f, 0);
        BUILDER.pos(0, 0 - height, 0).color(r, g, b, colors[3]).endVertex();
        BUILDER.pos(0, -3, 0).color(r, g, b, colors[3]).endVertex();
        BUILDER.pos(0 + width, 0, 0).color(r, g, b, colors[3]).endVertex();
        TESSELLATOR.draw();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawBlur(IRenderCall data) {
        GaussianBlur.registerRenderCall(data);
    }

//    public static void drawBlur(Runnable data) {
//        StencilUtils.initStencilToWrite();
//        data.run();
//        StencilUtils.readStencilBuffer(1);
//        GaussianBlurShader gaussianBlurShader = new GaussianBlurShader();
//        gaussianBlurShader.run();
//        StencilUtils.uninitStencilBuffer();
//    }

    public static void drawHorizontalGlow(float x, float y, float width, float height, int glowRadius, int color1, int color2) {
//
//        BufferedImage original = null;
//        GaussianFilter op = null;
//        RenderSystem.pushMatrix();
//        RenderSystem.enableBlend();
//        RenderSystem.disableAlphaTest();
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
//                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
//                GlStateManager.DestFactor.ZERO);
//        RenderSystem.shadeModel(7425);
//        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.01f);
//        width = width + glowRadius * 2;
//        height = height + glowRadius * 2;
//        x = x - glowRadius;
//        y = y - glowRadius;
//        float _X = x - 0.25f;
//        float _Y = y + 0.25f;
//        int identifier = String.valueOf(width * height + width + 1000000000 * glowRadius + glowRadius).hashCode();
//
//        RenderSystem.enableTexture();
//        RenderSystem.disableCull();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enableBlend();
//        int texId = -1;
//
//
//        if (shadowCache.containsKey(identifier)) {
//            texId = shadowCache.get(identifier);
//            RenderSystem.bindTexture(texId);
//        } else {
//            if (width <= 0) {
//                width = 1;
//            }
//
//            if (height <= 0) {
//                height = 1;
//            }
//            original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
//
//            Graphics g = original.getGraphics();
//            g.setColor(Color.white);
//            g.fillRect(glowRadius, glowRadius, (int) width - glowRadius * 2, (int) height - glowRadius * 2);
//            g.dispose();
//
//            op = new GaussianFilter(glowRadius);
//
//            BufferedImage blurred = op.filter(original, null);
//            texId = TextureUtils.uploadTextureImageAllocate(GlStateManager.genTexture(), blurred, true, false);
//            shadowCache.put(identifier, texId);
//        }
//
//
//        GL11.glBegin(GL11.GL_QUADS);
//        ColorUtils.glHexColor(color1);
//        GL11.glTexCoord2f(0, 0); // top left
//        GL11.glVertex2f(_X, _Y);
//        GL11.glTexCoord2f(0, 1); // bottom left
//        GL11.glVertex2f(_X, _Y + height);
//        ColorUtils.glHexColor(color2);
//        GL11.glTexCoord2f(1, 1); // bottom right
//        GL11.glVertex2f(_X + width, _Y + height);
//        GL11.glTexCoord2f(1, 0); // top right
//        GL11.glVertex2f(_X + width, _Y);
//
//        GL11.glEnd();
//        RenderSystem.enableTexture();
//        RenderSystem.shadeModel(7424);
//        RenderSystem.disableBlend();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.clearCurrentColor();
//        RenderSystem.enableCull();
//        RenderSystem.popMatrix();

        BufferedImage original = null;
        GaussianFilter op = null;
        glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
        width = width + glowRadius * 2;
        height = height + glowRadius * 2;
        x = x - glowRadius;
        y = y - glowRadius;
        float _X = x - 0.25f;
        float _Y = y + 0.25f;
        int identifier = String.valueOf(width * height + width + 1000000000 * glowRadius + glowRadius).hashCode();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableBlend();
        int texId = -1;


        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }
            original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            Graphics g = original.getGraphics();
            g.setColor(Color.white);
            g.fillRect(glowRadius, glowRadius, (int) width - glowRadius * 2, (int) height - glowRadius * 2);
            g.dispose();

            op = new GaussianFilter(glowRadius);

            BufferedImage blurred = op.filter(original, null);
            texId = TextureUtils.uploadTextureImageAllocate(GlStateManager.genTexture(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }


        GL11.glBegin(GL11.GL_QUADS);
        ColorService.glHexColor(color1);
        GL11.glTexCoord2f(0, 0); // top left
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0, 1); // bottom left
        GL11.glVertex2f(_X, _Y + height);
        ColorService.glHexColor(color2);
        GL11.glTexCoord2f(1, 1); // bottom right
        GL11.glVertex2f(_X + width, _Y + height);
        GL11.glTexCoord2f(1, 0); // top right
        GL11.glVertex2f(_X + width, _Y);

        GL11.glEnd();
        GlStateManager.enableTexture();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.clearCurrentColor();
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    public static void drawGlow(float x, float y, float width, float height, int glowRadius, int color) {
        BufferedImage original = null;
        GaussianFilter op = null;
        RenderSystem.pushMatrix();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.01f);
        width = width + glowRadius * 2;
        height = height + glowRadius * 2;
        x = x - glowRadius;
        y = y - glowRadius;
        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;
        final int identifier = String.valueOf(width * height + width + 1000000000 * glowRadius + glowRadius).hashCode();

        RenderSystem.enableTexture();
        RenderSystem.disableCull();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        int texId = -1;


        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            RenderSystem.bindTexture(texId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }
            if (original == null) {
                original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
            }

            final Graphics g = original.getGraphics();
            g.setColor(Color.white);
            g.fillRect(glowRadius, glowRadius, (int) (width - glowRadius * 2), (int) (height - glowRadius * 2));
            g.dispose();

            if (op == null) {
                op = new GaussianFilter(glowRadius);
            }

            final BufferedImage blurred = op.filter(original, null);
            texId = TextureUtils.uploadTextureImageAllocate(GlStateManager.genTexture(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }

        ColorService.glHexColor(color, 100);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); // top left
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0, 1); // bottom left
        GL11.glVertex2f(_X, _Y + height);
        GL11.glTexCoord2f(1, 1); // bottom right
        GL11.glVertex2f(_X + width, _Y + height);
        GL11.glTexCoord2f(1, 0); // top right
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.clearCurrentColor();
        RenderSystem.enableCull();
        RenderSystem.popMatrix();
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static void drawRoundedGradientRect(int i, int i1, int i2, int i3, int i4, int i5, String main, String textMain) {
    }
}
