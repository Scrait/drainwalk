package tech.drainwalk.client.modules.overlay;

import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.ui.UIMain;

public class Menu extends Module {

    public Menu() {
        super("Menu", Category.OVERLAY);
        this.addKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new UIMain());
        setEnabled(false);
    }

}
