package tech.drainwalk.api.impl.events.player;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.ItemStack;

@AllArgsConstructor
@Getter
public class ItemTooltipEvent implements Event {

    private final ItemStack itemStack;

}
