package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.PlayerEntity;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.api.impl.events.UpdateEvent;

import java.util.*;

public class AntiBot extends Module {
    /*private final BooleanOption setting = new BooleanOption("Delifan", true);
    private final MultiOption setting3 = new MultiOption("Delikates", new MultiOptionValue("Deus", false), new MultiOptionValue("De1us", true));
    private final BooleanOption setting1 = new BooleanOption("Delifan1", true);*/

    private final BooleanOption remove = new BooleanOption("Remove", true);
    private final Set<Integer> bots = new HashSet<>();

    public AntiBot() {
        super("AntiBot", Category.COMBAT);
        addDescription("Antibot, blocks AntiCheat generated bot.");
//        register(remove);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (PlayerEntity bot : mc.world.getPlayers()) {
            if (bot != mc.player && bot.ticksExisted < 5) {
                if (bot.hurtTime > 0 && mc.player.getDistance(bot) <= 25 && mc.player.connection.getPlayerInfo(bot.getUniqueID()).getResponseTime() != 0) {
                    bots.add(bot.getEntityId());
//                    if (remove.getValue()) {
//                        mc.world.removeEntityFromWorld(bot.getEntityId());
//                    }
                }
            }
        }
    }

    public boolean isValid(PlayerEntity entity) {
        if (isEnabled() && bots.contains(entity.getEntityId()))
            return false;
        return true;
    }

    @Override
    public void onDisable() {
        bots.clear();
    }
}