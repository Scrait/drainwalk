package tech.drainwalk.utils.shader.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import tech.drainwalk.utils.shader.AbstractShader;
import tech.drainwalk.utils.shader.Shader;
import tech.drainwalk.utils.shader.kernel.GaussianKernel;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GaussianBlurShader extends AbstractShader {

    private final Shader blurProgram = new Shader("shaders/blur.fsh", true);
    private Framebuffer inputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
    private Framebuffer outputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
    private GaussianKernel gaussianKernel = new GaussianKernel(0);

    @Override
    public void run(final float partialTicks, ConcurrentLinkedQueue<IRenderCall> runnable) {
        // Prevent rendering
        if (mc.getMainWindow().isClosed()) {
            return;
        }

        this.update();
        this.setActive(this.isActive() || !runnable.isEmpty());

        if (this.isActive()) {
            this.inputFramebuffer.bindFramebuffer(true);
            runnable.forEach(IRenderCall::execute);

            setRadius(8);

            // TODO: make radius and other things as a setting
            final float radius = getRadius();
            final float compression = 2.0F;

            this.outputFramebuffer.bindFramebuffer(true);
            this.blurProgram.useProgram();

            if (this.gaussianKernel.getSize() != radius) {
                this.gaussianKernel = new GaussianKernel((int) radius);
                this.gaussianKernel.compute();

                final FloatBuffer buffer = BufferUtils.createFloatBuffer((int) radius);
                buffer.put(this.gaussianKernel.getKernel());
                buffer.flip();

                blurProgram.setupUniform1f("u_radius", radius);
                blurProgram.setupUniformBF("u_kernel", buffer);
                blurProgram.setupUniform1i("u_diffuse_sampler", 0);
                blurProgram.setupUniform1i("u_other_sampler", 20);
            }

            blurProgram.setupUniform2f("u_texel_size", 1.0F / mc.getMainWindow().getWidth(), 1.0F / mc.getMainWindow().getHeight());
            blurProgram.setupUniform2f("u_direction", compression, 0.0F);

            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
            mc.getFramebuffer().bindFramebufferTexture();
            blurProgram.drawQuads();

            mc.getFramebuffer().bindFramebuffer(true);
            blurProgram.setupUniform2f("u_direction", 0.0F, compression);
            outputFramebuffer.bindFramebufferTexture();
            GL13.glActiveTexture(GL13.GL_TEXTURE20);
            inputFramebuffer.bindFramebufferTexture();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            blurProgram.drawQuads();
            GlStateManager.disableBlend();

            blurProgram.unloadProgram();
        }

    }

    @Override
    public void update() {
        this.setActive(false);

        if (mc.getMainWindow().getWidth() != inputFramebuffer.framebufferWidth || mc.getMainWindow().getFramebufferHeight() != inputFramebuffer.framebufferHeight) {
            inputFramebuffer.deleteFramebuffer();
            inputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);

            outputFramebuffer.deleteFramebuffer();
            outputFramebuffer = new Framebuffer(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getScaledHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
        } else {
            inputFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
            outputFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
        }
    }
}
