package tech.drainwalk.api.impl.events;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TickEvent implements Event {

    private final Phase phase;

    public enum Phase {
        START,
        MID,
        END;
    }

    @Getter
    public static class Render extends TickEvent
    {

        private final float renderTickTime;

        public Render(Phase phase, float renderTickTime) {
            super(phase);
            this.renderTickTime = renderTickTime;
        }
    }

}
