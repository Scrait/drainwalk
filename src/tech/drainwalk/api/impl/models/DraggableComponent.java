package tech.drainwalk.api.impl.models;

import com.darkmagician6.eventapi.EventTarget;
import lombok.Getter;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.interfaces.IFonts;
import tech.drainwalk.api.impl.interfaces.ITheme;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.DraggableOption;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.services.render.ColorService;

@Getter
public abstract class DraggableComponent extends Module implements ITheme, IFonts {

    private final DraggableOption draggableOption;
    private final BooleanOption blur = new BooleanOption("Blur", false);
    private final FloatOption alpha = new FloatOption("Alpha", 1, 0, 1).addIncrementValue(0.01f);
    private final FloatOption round = new FloatOption("Round", 6, 0, 10).addIncrementValue(0.1f);
    private boolean optionsEnabled = false;

    public DraggableComponent(String name, Vector2f value, float width, float height) {
        super(name, Category.OVERLAY);
        draggableOption = new DraggableOption(this.getName(), value, width, height);
    }

    @EventTarget
    public abstract void onUpdate(UpdateEvent event);

    @EventTarget
    public abstract void onRender2D(EventRender2D event);

    protected int[] getBackgroundColorsWithAlpha() {
        return new int[]{
                ColorService.getColorWithAlpha(backgroundFirstColor, alpha.getValue()),
                ColorService.getColorWithAlpha(backgroundSecondColor, alpha.getValue())
        };
    }

}
