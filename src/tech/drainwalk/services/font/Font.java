package tech.drainwalk.services.font;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.api.impl.interfaces.IShaders;
import tech.drainwalk.services.render.ColorService;

import java.awt.*;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR_TEX;

public class Font implements IShaders {

    private final MsdfFont font;

    public Font(String name) {
        this(name + ".png", name + ".json");
    }

    public Font(String atlas, String data) {
        font = MsdfFont.builder().withAtlas(atlas).withData(data).build();
    }

    public void drawText(MatrixStack stack, String text, float x, float y, int color, float size) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        FontData.AtlasData atlas = this.font.getAtlas();
        FONT.useProgram();
        FONT.setupUniformi("Sampler", 0);
        FONT.setupUniformf("EdgeStrength", 0.5f);
        FONT.setupUniformf("TextureSize", atlas.width(), atlas.height());
        FONT.setupUniformf("Range", atlas.range());
        FONT.setupUniformf("Thickness", 0f);
        FONT.setupUniformi("Outline", 0); // boolean
        FONT.setupUniformf("OutlineThickness", 0f);

        FONT.setupUniformf("OutlineColor", 1f, 1f, 1f, 1f);
        FONT.setupUniformf("color", ColorService.getRGBAf(color));

        this.font.bind();
        GlStateManager.enableBlend();
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, POSITION_COLOR_TEX);
        this.font.applyGlyphs(stack.getLast().getMatrix(), Tessellator.getInstance().getBuffer(), size, text, 0,
                x, y + font.getMetrics().baselineHeight() * size, 0, 255, 255, 255, 255);
        Tessellator.getInstance().draw();

        this.font.unbind();
        FONT.unloadProgram();
    }

    public void drawText(MatrixStack stack, ITextComponent text, float x, float y, float size, float alpha) {
        float offset = 0;
        for (ITextComponent it : text.getSiblings()) {

            for (ITextComponent it1 : it.getSiblings()) {
                String draw = it1.getString();

                if (it1.getStyle().getColor() != null) {
                    drawText(stack, draw, x + offset, y, ColorService.getColorWithAlpha(ColorService.hexToRgb(it1.getStyle().getColor().getHex()), alpha), size);
                } else
                    drawText(stack, draw, x + offset, y, ColorService.getColorWithAlpha(Color.GRAY.getRGB(), alpha), size);
                offset += getWidth(draw, size);
            }
            if (it.getSiblings().size() <= 1) {
                String draw = TextFormatting.getTextWithoutFormattingCodes(it.getString());

                drawText(stack, draw, x + offset, y, ColorService.getColorWithAlpha(it.getStyle().getColor() == null ? Color.GRAY.getRGB() : it.getStyle().getColor().getColor(), alpha), size);
                offset += getWidth(draw, size);
            }



        }
        if (text.getSiblings().isEmpty()) {
            String draw = TextFormatting.getTextWithoutFormattingCodes(text.getString());

            drawText(stack, draw, x + offset, y, ColorService.getColorWithAlpha(text.getStyle().getColor() == null ? Color.GRAY.getRGB() : text.getStyle().getColor().getColor(), alpha), size);
            offset += getWidth(draw, size);
        }
    }

    public float getWidth(ITextComponent text, float size) {
        float offset = 0;
        for (ITextComponent it : text.getSiblings()) {
            for (ITextComponent it1 : it.getSiblings()) {
                String draw = it1.getString();
                offset += getWidth(draw, size);
            }
            if (it.getSiblings().size() <= 1) {
                String draw = TextFormatting.getTextWithoutFormattingCodes(it.getString());
                offset += getWidth(draw, size);
            }
        }
        if (text.getSiblings().isEmpty()) {
            String draw = TextFormatting.getTextWithoutFormattingCodes(text.getString());
            offset += getWidth(draw, size);
        }
        return offset;
    }

    public void drawTextBuilding(MatrixStack stack, String text, float x, float y, int color, float size) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        FontData.AtlasData atlas = this.font.getAtlas();
        FONT.useProgram();
        FONT.setupUniformi("Sampler", 0);
        FONT.setupUniformf("EdgeStrength", 0.5f);
        FONT.setupUniformf("TextureSize", atlas.width(), atlas.height());
        FONT.setupUniformf("Range", atlas.range());
        FONT.setupUniformf("Thickness", 0f);
        FONT.setupUniformi("Outline", 0); // boolean
        FONT.setupUniformf("OutlineThickness", 0f);

        FONT.setupUniformf("OutlineColor", 1f, 1f, 1f, 1f);
        FONT.setupUniformf("color", ColorService.getRGBAf(color));

        this.font.bind();
        GlStateManager.enableBlend();
        // Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS,
        // POSITION_COLOR_TEX);
        this.font.applyGlyphs(stack.getLast().getMatrix(), Tessellator.getInstance().getBuffer(), size, text, 0,
                x, y + font.getMetrics().baselineHeight() * size, 0, 255, 255, 255, 255);
        // Tessellator.getInstance().draw();

        this.font.unbind();
        FONT.unloadProgram();
    }

    public void drawCenteredText(MatrixStack stack, String text, float x, float y, int color, float size) {
        drawText(stack, text, x - getWidth(text, size) / 2f, y, color, size);
    }

    public void drawCenteredText(MatrixStack stack, ITextComponent text, float x, float y, float size, float alpha) {
        drawText(stack, text, x - getWidth(text, size) / 2f, y, size, alpha);
    }

    public void drawCenteredTextWithOutline(MatrixStack stack, String text, float x, float y, int color, float size) {
        drawTextWithOutline(stack, text, x - getWidth(text, size) / 2f, y, color, size, 0.05f);
    }

    public void drawCenteredTextEmpty(MatrixStack stack, String text, float x, float y, int color, float size) {
        drawEmpty(stack, text, x - getWidth(text, size) / 2f, y, size, color, 0);
    }

    public void drawCenteredTextEmptyOutline(MatrixStack stack, String text, float x, float y, int color, float size) {
        drawEmptyWithOutline(stack, text, x - getWidth(text, size) / 2f, y, size, color, 0);
    }

    public void drawCenteredText(MatrixStack stack, String text, float x, float y, int color, float size,
                                 float thickness) {
        drawText(stack, text, x - getWidth(text, size, thickness) / 2f, y, color, size, thickness);
    }

    public void drawText(MatrixStack stack, String text, float x, float y, int color, float size, float thickness) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        FontData.AtlasData atlas = this.font.getAtlas();
        FONT.useProgram();
        FONT.setupUniformi("Sampler", 0);
        FONT.setupUniformf("EdgeStrength", 0.5f);
        FONT.setupUniformf("TextureSize", atlas.width(), atlas.height());
        FONT.setupUniformf("Range", atlas.range());
        FONT.setupUniformf("Thickness", thickness);
        FONT.setupUniformf("color", ColorService.getRGBAf(color));
        FONT.setupUniformi("Outline", 0); // boolean
        FONT.setupUniformf("OutlineThickness", 0f);

        FONT.setupUniformf("OutlineColor", 1f, 1f, 1f, 1f);

        this.font.bind();
        GlStateManager.enableBlend();
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, POSITION_COLOR_TEX);
        this.font.applyGlyphs(stack.getLast().getMatrix(), Tessellator.getInstance().getBuffer(), size, text, thickness,
                x, y + font.getMetrics().baselineHeight() * size, 0, 255, 255, 255, 255);
        Tessellator.getInstance().draw();

        this.font.unbind();
        FONT.unloadProgram();
    }

    public void drawTextWithOutline(MatrixStack stack, String text, float x, float y, int color, float size,
                                    float thickness) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        FontData.AtlasData atlas = this.font.getAtlas();
        FONT.useProgram();
        FONT.setupUniformi("Sampler", 0);
        FONT.setupUniformf("EdgeStrength", 0.5f);
        FONT.setupUniformf("TextureSize", atlas.width(), atlas.height());
        FONT.setupUniformf("Range", atlas.range());
        FONT.setupUniformf("Thickness", thickness);
        FONT.setupUniformf("color", ColorService.getRGBAf(color));
        FONT.setupUniformi("Outline", 1); // boolean
        FONT.setupUniformf("OutlineThickness", 0.2f);

        FONT.setupUniformf("OutlineColor", 0f, 0f, 0f, 1f);

        this.font.bind();
        GlStateManager.enableBlend();
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, POSITION_COLOR_TEX);
        this.font.applyGlyphs(stack.getLast().getMatrix(), Tessellator.getInstance().getBuffer(), size, text, thickness,
                x, y + font.getMetrics().baselineHeight() * size, 0, 255, 255, 255, 255);
        Tessellator.getInstance().draw();

        this.font.unbind();
        FONT.unloadProgram();
    }

    public void init(float thickness, float smoothness) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        FontData.AtlasData atlas = this.font.getAtlas();
        FONT.useProgram();
        FONT.setupUniformi("Sampler", 0);
        FONT.setupUniformf("EdgeStrength", smoothness);
        FONT.setupUniformf("TextureSize", atlas.width(), atlas.height());
        FONT.setupUniformf("Range", atlas.range());
        FONT.setupUniformf("Thickness", thickness);
        FONT.setupUniformi("Outline", 0); // boolean
        FONT.setupUniformf("OutlineThickness", 0f);

        FONT.setupUniformf("OutlineColor", 1f, 1f, 1f, 1f);

        this.font.bind();
        GlStateManager.enableBlend();
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, POSITION_COLOR_TEX);

    }

    public void drawEmpty(MatrixStack stack, String text, float x, float y, float size, int color, float thickness) {
        FONT.setupUniformf("color", ColorService.getRGBAf(color));
        this.font.applyGlyphs(stack.getLast().getMatrix(), Tessellator.getInstance().getBuffer(), size, text, thickness,
                x, y + font.getMetrics().baselineHeight() * size, 0, 255, 255, 255, 255);
    }

    public void drawEmptyWithOutline(MatrixStack stack, String text, float x, float y, float size, int color,
                                     float thickness) {
        FONT.setupUniformi("Outline", 1); // boolean
        FONT.setupUniformf("OutlineThickness", 0.2f);

        FONT.setupUniformf("OutlineColor", 0f, 0f, 0f, 1f);
        FONT.setupUniformf("color", ColorService.getRGBAf(color));
        this.font.applyGlyphs(stack.getLast().getMatrix(), Tessellator.getInstance().getBuffer(), size, text, thickness,
                x, y + font.getMetrics().baselineHeight() * size, 0, 255, 255, 255, 255);
    }

    public void end() {
        Tessellator.getInstance().draw();
        this.font.unbind();
        FONT.unloadProgram();
    }

    public float getWidth(String text, float size) {
        return font.getWidth(text, size);
    }

    public float getWidth(String text, float size, float thickness) {
        return font.getWidth(text, size, thickness);
    }

    public float getHeight(float size) {
        return size;
    }

}
