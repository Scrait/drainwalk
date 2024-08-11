package tech.drainwalk.utils.render;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.services.render.StencilService;
import tech.drainwalk.utils.Utils;
import tech.drainwalk.utils.shader.kernel.GaussianKernel;
import tech.drainwalk.utils.shader.Shader;

import java.nio.FloatBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.opengl.GL11.*;

public class GaussianBlur extends Utils {

    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
    private static final Shader blurShader = new Shader("drainwalk/shaders/gaussian.fsh", true);

    private static Framebuffer framebuffer = new Framebuffer(1, 1, false, mc.IS_RUNNING_ON_MAC);

    public static void setupUniforms(float dir1, float dir2, float radius) {
        blurShader.setupUniform1i("textureIn", 0);
        blurShader.setupUniform2f("texelSize", 1.0F / (float) mc.getMainWindow().getWidth(), 1.0F / (float) mc.getMainWindow().getHeight());
        blurShader.setupUniform2f("direction", dir1, dir2);
        blurShader.setupUniform1f("radius", radius);

        GaussianKernel gaussianKernel = new GaussianKernel((int) radius);
        gaussianKernel.compute();

        final FloatBuffer buffer = BufferUtils.createFloatBuffer((int) radius);
        buffer.put(gaussianKernel.getKernel());
        buffer.flip();
        RenderSystem.glUniform1(blurShader.getUniform("weights"), buffer);
    }

    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }

    public static void draw(int radius) {
        if (renderQueue.isEmpty())
            return;
        StencilService.initStencilToWrite();
        while(!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }
        StencilService.readStencilBuffer(1);
        renderBlur(radius);
        StencilService.uninitStencilBuffer();
    }

    public static void renderBlur(float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        framebuffer = createFrameBuffer(framebuffer);

        if (framebuffer != null) {
            framebuffer.framebufferClear(mc.IS_RUNNING_ON_MAC);
            framebuffer.bindFramebuffer(true);
            blurShader.useProgram();
            setupUniforms(1, 0, radius);

            glBindTexture(GL_TEXTURE_2D, mc.getFramebuffer().getFramebufferTexture());

            Shader.drawQuads();
            framebuffer.unbindFramebuffer();
            blurShader.unloadProgram();

            mc.getFramebuffer().bindFramebuffer(true);
            blurShader.useProgram();
            setupUniforms(0, 1, radius);

            glBindTexture(GL_TEXTURE_2D, framebuffer.getFramebufferTexture());
            Shader.drawQuads();
            blurShader.unloadProgram();
        }

        GL11.glColor4d(1, 1, 1, 1);
        GlStateManager.bindTexture(0);
    }

    private static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight()) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            try {
                return new Framebuffer(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(), true, mc.IS_RUNNING_ON_MAC);
            } catch (Exception ex) {
                return null;
            }
        }
        return framebuffer;
    }
}
