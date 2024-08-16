package tech.drainwalk.client.ui.dwmenu.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.client.ui.dwmenu.UIMain;

@Getter
public abstract class Component implements IInstanceAccess, ITheme, IFonts {

    @Setter
    protected float x, y, width, height;
    public boolean canDragging = true;
    protected final UIMain parent;

    public Component(float x, float y, float width, float height, UIMain parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
    public abstract void tick();
    public abstract void charTyped(char codePoint, int modifiers);
    public abstract void mouseClicked(double mouseX, double mouseY, int button);
    public abstract void mouseScrolled(double mouseX, double mouseY, double delta);

}
