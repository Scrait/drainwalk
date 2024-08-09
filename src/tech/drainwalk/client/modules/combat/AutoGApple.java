package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Items;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.UpdateEvent;

public class AutoGApple extends Module {

    private final FloatOption health = new FloatOption("Health", 10, 0, 20).addIncrementValue(0.1f);
    private boolean isActive;

    public AutoGApple() {
        super("AutoGApple", Category.COMBAT);
        addType(Type.SECONDARY);
        register(health);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE
                && mc.player.getHealth() <= health.getValue()) {
            isActive = true;
            mc.gameSettings.keyBindUseItem.setPressed(true);
        } else if (isActive) {
            mc.gameSettings.keyBindUseItem.setPressed(false);
            isActive = false;
        }
    }
}
