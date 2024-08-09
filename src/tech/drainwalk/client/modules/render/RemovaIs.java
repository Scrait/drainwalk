package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.api.impl.events.render.EventGameOverlay;

public class RemovaIs extends Module {

    public final MultiOption type =
            new MultiOption("Type",
                    new MultiOptionValue("Hurt", false),
                    new MultiOptionValue("Totem", false),
                    new MultiOptionValue("Fire", false));

    public RemovaIs() {
        super("RemovaIs", Category.RENDER);
        register(type);
    }

    @EventTarget
    public void onGameOverlay(EventGameOverlay event) {
        if (type.isSelected("Hurt")
                && event.getOverlayType() == EventGameOverlay.OverlayType.Hurt) {
            event.setCancelled(true);
        } else if (type.isSelected("Totem")
                && event.getOverlayType() == EventGameOverlay.OverlayType.TotemPop) {
            event.setCancelled(true);
        } else if (type.isSelected("Fire") && event.getOverlayType() == EventGameOverlay.OverlayType.Fire) {
            event.setCancelled(true);
        }
    }
}
