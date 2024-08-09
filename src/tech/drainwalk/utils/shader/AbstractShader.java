package tech.drainwalk.utils.shader;

import com.mojang.blaze3d.systems.IRenderCall;
import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
public abstract class AbstractShader implements IInstanceAccess {
    private boolean active;
    private float radius;

    public abstract void run(float partialTicks, ConcurrentLinkedQueue<IRenderCall> runnable);

    public abstract void update();
}
