package tech.drainwalk.api.impl.events.player;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.vector.Vector2f;

@Getter
@Setter
@AllArgsConstructor
public final class LookEvent implements Event {
    private Vector2f rotation;
}
