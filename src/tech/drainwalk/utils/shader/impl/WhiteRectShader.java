package tech.drainwalk.utils.shader.impl;

import com.mojang.blaze3d.systems.IRenderCall;
import tech.drainwalk.utils.render.RenderUtils;
import tech.drainwalk.services.render.StencilService;
import tech.drainwalk.utils.shader.AbstractShader;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WhiteRectShader extends AbstractShader {

    @Override
    public void run(final float partialTicks, ConcurrentLinkedQueue<IRenderCall> runnable) {
        // Prevent rendering
        if (mc.getMainWindow().isClosed()) {
            return;
        }

        this.setActive(this.isActive() || !runnable.isEmpty());

        if (this.isActive()) {
            StencilService.initStencilToWrite();
            //this.inputFramebuffer.bindFramebuffer(true);
            runnable.forEach(IRenderCall::execute);
            StencilService.readStencilBuffer(1);

            //this.outputFramebuffer.bindFramebuffer(true);
            RenderUtils.drawRect(0, 0, mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), -1);
            StencilService.uninitStencilBuffer();
        }
    }

    @Override
    public void update() {

    }
}
