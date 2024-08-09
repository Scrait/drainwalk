package tech.drainwalk.api.impl.events.movement;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

public class EventEntityMove implements Event {
    private Entity ctx;
    private Vector3d from;

    public EventEntityMove(Entity ctx, Vector3d from) {
        this.ctx = ctx;
        this.from = from;
    }

    public Vector3d from() {
        return this.from;
    }

    public Entity ctx() {
        return this.ctx;
    }

}
