package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.api.impl.events.modules.NoPushEvent;

public class NoPush extends Module {

    public final MultiOption from = new MultiOption("From", new MultiOptionValue("Water", false), new MultiOptionValue("Entities", false),  new MultiOptionValue("Blocks", false));

    public NoPush() {
        super("NoPush", Category.MISC);
        register(from);
    }

    @EventTarget
    public void onPush(NoPushEvent event) {
        if (event.getPushType() == NoPushEvent.PushType.Water && from.isSelected("Water")) {
            event.setCancelled(true);
        } else if (event.getPushType() == NoPushEvent.PushType.Entities && from.isSelected("Entities")) {
            event.setCancelled(true);
        } else if (event.getPushType() == NoPushEvent.PushType.Blocks && from.isSelected("Blocks")) {
            event.setCancelled(true);
        }
    }

}
