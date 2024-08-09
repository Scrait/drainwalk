package tech.drainwalk.services.players;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.RequiredArgsConstructor;
import tech.drainwalk.utils.time.Timer;

@RequiredArgsConstructor
public abstract class Player {

    protected final String fileName;
    protected final int frameCount;
    protected final int fps;
    protected final String PATH;

    protected int currentFrame = 0;
    protected final Timer fpsStabilizer = new Timer();

    public abstract void play(MatrixStack matrixStack, float x, float y, float width, float height);

}
