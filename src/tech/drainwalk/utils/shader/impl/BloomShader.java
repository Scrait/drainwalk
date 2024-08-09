package tech.drainwalk.utils.shader.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import tech.drainwalk.utils.shader.AbstractShader;
import tech.drainwalk.utils.shader.Shader;
import tech.drainwalk.utils.shader.kernel.GaussianKernel;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.mojang.blaze3d.systems.RenderSystem.glUniform1;

public class BloomShader extends AbstractShader {

    private final Shader bloom = new Shader("shaders/bloom.fsh", true);
    private final Framebuffer inputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
    private final Framebuffer outputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);

    public BloomShader() {
        setRadius(25);
    }

    @Override
    public void run(final float partialTicks, ConcurrentLinkedQueue<IRenderCall> runnable) {
        // Prevent rendering
        if (mc.getMainWindow().isClosed()) {
            return;
        }

        this.update();
        this.setActive(this.isActive() || !runnable.isEmpty());

        if (this.isActive()) {

            float exposure = 1.5f;

            setupBuffer(inputFramebuffer);
            setupBuffer(outputFramebuffer);
            inputFramebuffer.bindFramebuffer(true);
            runnable.forEach(IRenderCall::execute);
            inputFramebuffer.unbindFramebuffer();

            outputFramebuffer.bindFramebuffer(true);

            bloom.useProgram();
            bloom.setupUniform1f("radius", getRadius());
            bloom.setupUniform1f("exposure", exposure);
            bloom.setupUniform1i("textureIn", 0);
            bloom.setupUniform1i("textureToCheck", 20);
            bloom.setupUniform1i("avoidTexture", 1);
            final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
            for (int i = 0; i <= getRadius(); i++) {
                weightBuffer.put(calculateGaussianValue(i, getRadius() / 2));
            }

            weightBuffer.rewind();
            glUniform1(bloom.getUniform("weights"), weightBuffer);
            bloom.setupUniform2f("texelSize", 1.0F / (float) Minecraft.getInstance().getMainWindow().getWidth(), 1.0F / (float) Minecraft.getInstance().getMainWindow().getHeight());
            bloom.setupUniform2f("direction", 1f, 0.0F);

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
            GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

            inputFramebuffer.bindFramebufferTexture();
            Shader.drawQuads();

            mc.getFramebuffer().bindFramebuffer(false);
            GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

            bloom.setupUniform2f("direction", 0.0F, 1);

            outputFramebuffer.bindFramebufferTexture();
            GL30.glActiveTexture(GL30.GL_TEXTURE20);
            inputFramebuffer.bindFramebufferTexture();
            GL30.glActiveTexture(GL30.GL_TEXTURE0);
            Shader.drawQuads();

            bloom.unloadProgram();
            outputFramebuffer.unbindFramebuffer();
            GlStateManager.bindTexture(0);
            GlStateManager.disableBlend();
            mc.getFramebuffer().bindFramebuffer(false);
        }

    }

    private float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    private Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if(frameBuffer.framebufferWidth != mc.getMainWindow().getWidth() || frameBuffer.framebufferHeight != mc.getMainWindow().getHeight())
            frameBuffer.resize(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), Minecraft.IS_RUNNING_ON_MAC);
        else
            frameBuffer.framebufferClear(false);
        frameBuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);

        return frameBuffer;
    }

    @Override
    public void update() {
        this.setActive(false);

//        if (mc.getMainWindow().getWidth() != inputFramebuffer.framebufferWidth || mc.getMainWindow().getFramebufferHeight() != inputFramebuffer.framebufferHeight) {
//            inputFramebuffer.deleteFramebuffer();
//            inputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
//
//            outputFramebuffer.deleteFramebuffer();
//            outputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getScaledHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
//        } else {
//            inputFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
//            outputFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
//        }
//
//        inputFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
//        outputFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    }
}
