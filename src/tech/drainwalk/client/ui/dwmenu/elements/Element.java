package tech.drainwalk.client.ui.dwmenu.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.services.animation.Animation;

@Getter
@RequiredArgsConstructor
public abstract class Element<T> implements IInstanceAccess, ITheme {

    @Setter
    protected float x, y;
    protected final float width, height;
    protected final T option;
    protected final Animation withModuleEnabledAnimation = new Animation();
    protected final Animation openAnimation = new Animation();
    protected final Animation activeAnimation = new Animation();

    public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
    public abstract void tick();
    public abstract void charTyped(char codePoint, int modifiers);
    public abstract void mouseClicked(double mouseX, double mouseY, int button);
    public abstract void mouseScrolled(double mouseX, double mouseY, double delta);

}
