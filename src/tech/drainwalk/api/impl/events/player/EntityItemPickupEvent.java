package tech.drainwalk.api.impl.events.player;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

@AllArgsConstructor
@Getter
public class EntityItemPickupEvent implements Event {

    private final PlayerEntity player;
    private final ItemEntity item;

}
