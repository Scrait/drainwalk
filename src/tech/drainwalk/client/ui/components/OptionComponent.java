package tech.drainwalk.client.ui.components;

import com.mojang.blaze3d.systems.IRenderCall;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.services.animation.Animation;

@Getter
@RequiredArgsConstructor
public abstract class OptionComponent<T> implements IRenderCall, IInstanceAccess {

    protected final T option;
    protected final Animation hoveredAnimation = new Animation();
    protected final Animation visibleAnimation = new Animation();

}
