package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SUpdateTimePacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;

public class WorldFeatures extends Module {
    private final SelectOption mode = new SelectOption("Ambiance Mode", 0,
            new SelectOptionValue("Day"),
            new SelectOptionValue("Night"),
            new SelectOptionValue("Morning"),
            new SelectOptionValue("Sunset"));

    public WorldFeatures() {
        super("WorldFeatures", Category.RENDER);
        addType(Type.SECONDARY);
        register(mode);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SUpdateTimePacket) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mode.getValueByIndex(0)) {
            mc.world.setDayTime(5000);
        } else if (mode.getValueByIndex(1)) {
            mc.world.setDayTime(17000);
        } else if (mode.getValueByIndex(2)) {
            mc.world.setDayTime(0);
        } else if (mode.getValueByIndex(3)) {
            mc.world.setDayTime(13000);
        }
    }
}
