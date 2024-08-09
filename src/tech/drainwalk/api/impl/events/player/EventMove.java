package tech.drainwalk.api.impl.events.player;


import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.Getter;
import lombok.Setter;

public class EventMove extends EventCancellable {
    @Getter
    @Setter
    private double x, y, z;
    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}