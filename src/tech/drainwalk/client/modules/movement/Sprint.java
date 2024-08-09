package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.api.impl.events.UpdateEvent;

public class Sprint extends Module {

    private final BooleanOption legit = new BooleanOption("Legit", true);

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        addType(Type.SECONDARY);
        register(legit);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (!legit.getValue()) {
            if ((mc.gameSettings.keyBindForward.isKeyDown()) && !(mc.player.isSneaking()) && !(mc.player.isHandActive())
                    && !(mc.player.collidedHorizontally) && !(mc.player.getFoodStats().getFoodLevel() <= 6f)) {
                mc.player.setSprinting(true);
            }
        } else {
            mc.gameSettings.keyBindSprint.setPressed(true);
        }

    }

    @Override
    public void onDisable() {
        if (!legit.getValue()) {
            mc.player.setSprinting(false);
        } else {
            mc.gameSettings.keyBindSprint.setPressed(false);
        }
        super.onDisable();
    }
}
