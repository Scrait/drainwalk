package tech.drainwalk.client.ui.dwmenu.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.services.animation.Animation;

@Getter
public abstract class Element<T> implements IInstanceAccess, ITheme, IFonts {

    @Setter
    protected float x, y;
    @Setter
    protected float width, height;
    protected final T option;
    protected final Animation withModuleEnabledAnimation = new Animation();
    protected final Animation openAnimation = new Animation();
    protected final Animation activeAnimation = new Animation();

    public Element(float width, float height, T option) {
        this.width = width;
        this.height = height;
        this.option = option;
    }

    public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
    public abstract void tick();
    public abstract void charTyped(char codePoint, int modifiers);
    public abstract void mouseClicked(double mouseX, double mouseY, int button);
    public abstract void mouseScrolled(double mouseX, double mouseY, double delta);
    public abstract void mouseReleased(double mouseX, double mouseY, int button);

}
