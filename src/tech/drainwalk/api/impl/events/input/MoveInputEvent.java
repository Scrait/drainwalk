package tech.drainwalk.api.impl.events.input;

import com.darkmagician6.eventapi.events.Event;
import lombok.Getter;
import lombok.Setter;

public class MoveInputEvent implements Event {
    @Getter
    @Setter
    private float forward, strafe;

    public MoveInputEvent(float forward, float strafe) {
        this.forward = forward;
        this.strafe = strafe;
    }
}
