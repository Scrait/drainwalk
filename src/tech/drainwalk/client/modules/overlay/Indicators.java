package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.draggables.Watermark;
import tech.drainwalk.api.impl.events.render.EventRender2D;

public class Indicators extends Module implements IFonts {

    public Indicators() {
        super("Indicators", Category.OVERLAY);
        this.addKey(GLFW.GLFW_KEY_V);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        dw.getDraggableManager().findByClass(Watermark.class).onRender2D(event);
    }

}
