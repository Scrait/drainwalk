package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.api.impl.events.UpdateEvent;

public class NoDelayModule extends Module {
    private final BooleanOption noJumpDelay = new BooleanOption("Jump", true);
    private final BooleanOption noClickDelay = new BooleanOption("Click", true);

    public NoDelayModule() {
        super("NoDelay", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(UpdateEvent eventUpdate) {
        mc.player.setJumpTicks(0);
    }

    @Override
    public void onDisable() {
        mc.player.setJumpTicks(10);
    }
}
