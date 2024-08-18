package tech.drainwalk.client.ui.dwmenu.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.client.ui.dwmenu.UIMain;

@Getter
@AllArgsConstructor
public abstract class Component implements IInstanceAccess, ITheme, IFonts {

    protected final float x, y, width, height;
    protected final UIMain parent;

    public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
    public abstract void tick();
    public abstract void charTyped(char codePoint, int modifiers);
    public abstract void mouseClicked(double mouseX, double mouseY, int button);
    public abstract void mouseScrolled(double mouseX, double mouseY, double delta);

}
