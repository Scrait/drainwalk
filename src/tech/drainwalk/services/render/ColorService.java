package tech.drainwalk.services.render;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.services.math.MathService;

import java.awt.*;
import java.nio.ByteBuffer;

@UtilityClass
public class ColorService {

    public int COLOR_NONE = ColorService.rgbaFloat(1, 1, 1, 1);

    public float[] getRGBAf(int color) {
        return new float[]{(color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f,
                (color >> 24 & 0xFF) / 255.0f};
    }

    public int getColorWithAlpha(int color, float alpha) {
        float[] rgb = getRGBAf(color);
        return rgba((int) (rgb[0] * 255.0f), (int) (rgb[1] * 255.0f), (int) (rgb[2] * 255.0f), (int) (alpha * 255.0f));
    }

    public int interpolateColor(int color1, int color2, double offset) {
        float[] rgba1 = getRGBAf(color1);
        float[] rgba2 = getRGBAf(color2);
        double r = rgba1[0] + (rgba2[0] - rgba1[0]) * offset;
        double g = rgba1[1] + (rgba2[1] - rgba1[1]) * offset;
        double b = rgba1[2] + (rgba2[2] - rgba1[2]) * offset;
        double a = rgba1[3] + (rgba2[3] - rgba1[3]) * offset;
        return rgba((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public int interpolateColorsBackAndForth(int speed, int index, int start, int end) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return interpolateColor(start, end, angle / 360f);
    }

    public int rgba(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public int rgb(int r, int g, int b) {
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public int rgbaFloat(float r, float g, float b, float a) {
        return (int) (MathService.clamp(a,0,1) * 255) << 24 | (int) (MathService.clamp(r,0,1) * 255) << 16 | (int) (MathService.clamp(g,0,1) * 255) << 8 | (int) (MathService.clamp(b,0,1) * 255);
    }

    public int rgbFloat(float r, float g, float b) {
        return (255) << 24 | (int) (MathService.clamp(r,0,1) * 255) << 16 | (int) (MathService.clamp(g,0,1) * 255) << 8 | (int) (MathService.clamp(b,0,1) * 255);
    }

    public String RGBtoHEXString(int color) {
        return Integer.toHexString(color).substring(2);
    }

    public int getColorFromPixel(int x, int y) {
        ByteBuffer rgb = BufferUtils.createByteBuffer(3);
        GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);
        return rgb((rgb.get(0) & 255), (rgb.get(1) & 255), (rgb.get(2) & 255));
    }
    public int HUEtoRGB(int value) {
        float hue = (value / 360f);
        return Color.HSBtoRGB(hue, 1, 1);
    }

    public int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b);
    }

    public void glHexColor(int hex, int alpha) {
        float red = (float)(hex >> 16 & 255) / 255.0F;
        float green = (float)(hex >> 8 & 255) / 255.0F;
        float blue = (float)(hex & 255) / 255.0F;
        RenderSystem.color4f(red, green, blue, (float) alpha / 255);
    }

    public void glHexColor(int hex, float alpha) {
        float red = (float)(hex >> 16 & 255) / 255.0F;
        float green = (float)(hex >> 8 & 255) / 255.0F;
        float blue = (float)(hex & 255) / 255.0F;
        RenderSystem.color4f(red, green, blue, alpha);
    }

    public void glHexColor(int color) {
        glHexColor(color, (color >> 24 & 255) / 255.0F);
    }

    public float getSkyRainbow(float speed, int index) {
        int n = 0;
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        float hue = (float)angle / 360.0f;
        angle = (int)((double)angle % 360.0);
        return Color.HSBtoRGB((double)((float)((double)n / 360.0)) < 0.5 ? -((float)((double)angle / 360.0)) : (float)((double)angle / 360.0), 1.0f, 1.0f);
    }

    public int astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, saturation, 1F);
    }

    public int hexToRgb(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Недопустимый формат HEX: " + hex);
        }

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return rgb(r, g, b);
    }

}
