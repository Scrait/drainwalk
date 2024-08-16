package tech.drainwalk.client.modules.overlay;

import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;

public class Menu extends Module {

    public Menu() {
        super("Menu", Category.OVERLAY);
        this.setCurrentKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void toggle() {
        if (!this.isEnabled() && mc.currentScreen != dw.getUiMain()) {
            mc.displayGuiScreen(dw.getUiMain());
        }
    }

}
