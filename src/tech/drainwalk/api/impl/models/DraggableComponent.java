package tech.drainwalk.api.impl.models;

import com.darkmagician6.eventapi.EventTarget;
import lombok.Getter;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.DraggableOption;
import tech.drainwalk.api.impl.events.render.EventRender2D;

@Getter
public abstract class DraggableComponent extends Module implements ITheme, IFonts {

    private final DraggableOption draggableOption;

    public DraggableComponent(String name, Vector2f value, float width, float height) {
        super(name, Category.OVERLAY);
        draggableOption = new DraggableOption(this.getName(), value, width, height);
    }

    @EventTarget
    public abstract void onRender2D(EventRender2D event);

}
