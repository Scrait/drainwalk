package tech.drainwalk.services.players;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;

public class GifPlayer extends Player {

    public GifPlayer(String gifName, int frameCount, int fps) {
        super(gifName, frameCount, fps, "gifs/");
    }

    public void play(MatrixStack matrixStack, float x, float y, float width, float height, int color) {
        RenderService.drawImage(matrixStack, new DrainwalkResource(PATH + fileName + ".gif/" + fileName + "-" + currentFrame + ".png"), x, y, width, height, color);
        if (fpsStabilizer.delay((float) Minecraft.getDebugFPS() / fps, true)) {
            currentFrame += currentFrame >= frameCount ? -frameCount + 1 : 1;
        }
    }

    @Override
    public void play(MatrixStack matrixStack, float x, float y, float width, float height) {
        play(matrixStack, x, y, width, height, ColorService.COLOR_NONE);
    }

}