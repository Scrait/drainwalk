package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.render.EventRender2D;

public class Shaders extends Module {

    public Shaders() {
        super("Shaders", Category.OVERLAY);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onRender2D(EventRender2D.Pre event) {
        renderShaders(event.getPartialTicks());
        renderRects(event.getPartialTicks());
    }

}
