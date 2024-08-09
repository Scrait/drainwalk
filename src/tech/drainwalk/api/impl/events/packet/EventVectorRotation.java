package tech.drainwalk.api.impl.events.packet;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

/**
 *
 * inject into Entity class, getVectorForRotation(f, f)V method.
 * Designed to change the rotation in the number methods, thereby burning the current
 * rotation (only client)
 *
 */
public class EventVectorRotation extends EventCancellable {

    private float yaw, pitch;

    public EventVectorRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;

        if (!isCancelled())
            setCancelled(true);
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;

        if (!isCancelled())
            setCancelled(true);
    }

}
