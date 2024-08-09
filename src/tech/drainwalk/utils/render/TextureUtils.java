package tech.drainwalk.utils.render;

import java.awt.image.BufferedImage;
import java.nio.*;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.optifine.Config;
import net.optifine.reflect.Reflector;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

public final class TextureUtils {
    private static final IntBuffer DATA_BUFFER = GLAllocation.createDirectByteBuffer(4194304 << 2).asIntBuffer();


    public static int loadTexture(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

        try {
            for (int pixel : pixels) {
                buffer.put((byte)((pixel >> 16) & 0xFF));
                buffer.put((byte)((pixel >> 8) & 0xFF));
                buffer.put((byte)(pixel & 0xFF));
                buffer.put((byte)((pixel >> 24) & 0xFF));
            }
            buffer.flip();
        } catch (BufferOverflowException | ReadOnlyBufferException ex) {return -1;}

        int textureID = GlStateManager.genTexture();
        GlStateManager.bindTexture(textureID);
        GlStateManager.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GlStateManager.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
        GlStateManager.bindTexture(0);

        return textureID;
    }

    public static int uploadTextureImageAllocate(int textureId, BufferedImage texture, boolean blur, boolean clamp)
    {
        allocateTexture(textureId, texture.getWidth(), texture.getHeight());
        return uploadTextureImageSub(textureId, texture, 0, 0, blur, clamp);
    }

    public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_)
    {
        GlStateManager.bindTexture(textureId);
        uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_)
    {
        int i = p_110993_0_.getWidth();
        int j = p_110993_0_.getHeight();
        int k = 4194304 / i;
        int[] aint = new int[4194304];
        setTextureBlurred(p_110993_3_);
        setTextureClamped(p_110993_4_);

        for (int l = 0; l < i * j; l += i * k)
        {
            int i1 = l / i;
            int j1 = Math.min(k, j - i1);
            int k1 = i * j1;
            p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
            copyToBuffer(aint, k1);
            texSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + i1, i, j1, 32993, 33639, DATA_BUFFER);
        }
    }

    public static void texSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, IntBuffer pixels)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        GL11.glTexSubImage2D(target, level, xOffset, yOffset, width, height, format, type, pixels);
    }

    public static void setTextureClamped(boolean p_110997_0_)
    {
        if (p_110997_0_)
        {
            GlStateManager.texParameter(3553, 10242, 33071);
            GlStateManager.texParameter(3553, 10243, 33071);
        }
        else
        {
            GlStateManager.texParameter(3553, 10242, 10497);
            GlStateManager.texParameter(3553, 10243, 10497);
        }
    }

    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_)
    {
        copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }

    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_)
    {
        int[] aint = p_110994_0_;

        DATA_BUFFER.clear();
        DATA_BUFFER.put(aint, p_110994_1_, p_110994_2_);
        DATA_BUFFER.position(0).limit(p_110994_2_);
    }

    private static void setTextureBlurred(boolean p_147951_0_)
    {
        setTextureBlurMipmap(p_147951_0_, false);
    }

    public static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_)
    {
        if (p_147954_0_)
        {
            GlStateManager.texParameter(3553, 10241, p_147954_1_ ? 9987 : 9729);
            GlStateManager.texParameter(3553, 10240, 9729);
        }
        else
        {
            int i = Config.getMipmapType();
            GlStateManager.texParameter(3553, 10241, p_147954_1_ ? i : 9728);
            GlStateManager.texParameter(3553, 10240, 9728);
        }
    }

    public static void allocateTexture(int textureId, int width, int height)
    {
        allocateTextureImpl(textureId, 0, width, height);
    }

    public static void allocateTextureImpl(int glTextureId, int mipmapLevels, int width, int height)
    {
        Object object = TextureUtil.class;

        synchronized (object)
        {
            GlStateManager.deleteTexture(glTextureId);
            GlStateManager.bindTexture(glTextureId);
        }

        if (mipmapLevels >= 0)
        {
            GlStateManager.texParameter(3553, 33085, mipmapLevels);
            GlStateManager.texParameter(3553, 33082, 0);
            GlStateManager.texParameter(3553, 33083, mipmapLevels);
            GlStateManager.texParameter(3553, 34049, 0.0F);
        }

        for (int i = 0; i <= mipmapLevels; ++i)
        {
            GlStateManager.texImage2D(3553, i, 6408, width >> i, height >> i, 0, 32993, 33639, (IntBuffer)null);
        }
    }
}