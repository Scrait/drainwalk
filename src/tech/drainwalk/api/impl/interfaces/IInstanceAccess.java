package tech.drainwalk.api.impl.interfaces;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.Minecraft;
import tech.drainwalk.Drainwalk;
import tech.drainwalk.utils.shader.Shaders;
import tech.drainwalk.utils.shader.base.Profiler;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is an interface we can implement if we require access to the game
 * instance or our client instance if we require them in anywhere.
 *
 * @author Scrait
 * @since 17/08/2023
 */
public interface IInstanceAccess {

    Minecraft mc = Minecraft.getInstance();
    Drainwalk dw = Drainwalk.getInstance();

    ConcurrentLinkedQueue<IRenderCall> NORMAL_BLUR_RUNNABLES = Queues.newConcurrentLinkedQueue();
    ConcurrentLinkedQueue<IRenderCall> NORMAL_WHITE_RECT_RUNNABLES = Queues.newConcurrentLinkedQueue();
    ConcurrentLinkedQueue<IRenderCall> NORMAL_BLACK_RECT_RUNNABLES = Queues.newConcurrentLinkedQueue();
    ConcurrentLinkedQueue<IRenderCall> NORMAL_POST_BLOOM_RUNNABLES = Queues.newConcurrentLinkedQueue();

    Profiler blurProfiler = new Profiler();
    Profiler bloomProfiler = new Profiler();
    Profiler whiteRectProfiler = new Profiler();
    Profiler blackRectProfiler = new Profiler();


    default void renderShaders(float partialTicks) {
        if (!NORMAL_BLUR_RUNNABLES.isEmpty()) {
            blurProfiler.start();
            Shaders.GAUSSIAN_BLUR_SHADER.run(partialTicks, IInstanceAccess.NORMAL_BLUR_RUNNABLES);
            blurProfiler.stop();
            blurProfiler.reset();
        }
        if (!NORMAL_POST_BLOOM_RUNNABLES.isEmpty()) {
            bloomProfiler.start();
            Shaders.POST_BLOOM_SHADER.run(partialTicks, IInstanceAccess.NORMAL_POST_BLOOM_RUNNABLES);
            bloomProfiler.stop();
            bloomProfiler.reset();
        }
        clearShadersRunnables();
    }

    default void renderRects(float partialTicks) {
        if (!NORMAL_BLACK_RECT_RUNNABLES.isEmpty()) {
            blackRectProfiler.start();
            Shaders.BLACK_RECT_SHADER.run(partialTicks, IInstanceAccess.NORMAL_BLACK_RECT_RUNNABLES);
            blackRectProfiler.stop();
            blackRectProfiler.reset();
        }
        if (!NORMAL_WHITE_RECT_RUNNABLES.isEmpty()) {
            whiteRectProfiler.start();
            Shaders.WHITE_RECT_SHADER.run(partialTicks, IInstanceAccess.NORMAL_WHITE_RECT_RUNNABLES);
            whiteRectProfiler.stop();
            whiteRectProfiler.reset();
        }
        clearRectsRunnables();
    }

    private void clearShadersRunnables() {
        NORMAL_BLUR_RUNNABLES.clear();
        NORMAL_POST_BLOOM_RUNNABLES.clear();
    }

    default void clearRectsRunnables() {
        NORMAL_BLACK_RECT_RUNNABLES.clear();
        NORMAL_WHITE_RECT_RUNNABLES.clear();
    }
}
