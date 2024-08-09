package tech.drainwalk.client.gui.modernui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.client.gui.modernui.MenuMain;

@Getter
public abstract class Component implements IInstanceAccess {

    @Setter
    protected float x, y, width, height;
    public static boolean canDragging = true;
    protected final MenuMain parent;

    public Component(float x, float y, float width, float height, MenuMain parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
    public abstract void tick();
    public abstract boolean charTyped(char codePoint, int modifiers);
    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);
    public abstract boolean mouseScrolled(double mouseX, double mouseY, double delta);

}
