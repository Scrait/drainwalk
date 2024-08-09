package tech.drainwalk.client.service.services.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.utils.time.Timer;

@RequiredArgsConstructor
public class GifPlayer {
    
    private final String gifName;
    private final int frameCount;
    private final int fps;

    private int currentFrame = 1;
    private final Timer fpsStabilizer = new Timer();

    public void play(MatrixStack matrixStack, float x, float y, float width, float height) {
//        final StringBuilder currentFrameInName = new StringBuilder();
//        final int nolCount = 2 - String.valueOf(currentFrame).length();
//        currentFrameInName.append("0".repeat(Math.max(0, nolCount)));
//        currentFrameInName.append(currentFrame + "_delay-0.01s.gif");
//        //System.out.println(currentFrameInName);
//        RenderService.renderImage(matrixStack, new ResourceLocation("drainwalk/gifs/" + gifName + "/frame_" + currentFrameInName), x, y, width, height);
//        if (fpsStabilizer.delay(Minecraft.getDebugFPS() / fps, true)) {
//            currentFrame += currentFrame >= frameCount ? -frameCount : 1;
//        }
        final StringBuilder currentFrameInName = new StringBuilder();
        //final int nolCount = 5 - String.valueOf(currentFrame).length();
        //currentFrameInName.append("0".repeat(Math.max(0, nolCount)));
        currentFrameInName.append("jc").append(currentFrame).append(".gif");
        //System.out.println(currentFrameInName);
        RenderService.drawImage(matrixStack, new ResourceLocation("drainwalk/gifs/" + gifName + "/" + currentFrameInName), x, y, width, height);
        if (fpsStabilizer.delay(Minecraft.getDebugFPS() / fps, true)) {
            currentFrame += currentFrame >= frameCount ? -frameCount + 1 : 1;
        }
    }

}
