package tech.drainwalk.api.impl.events.movement;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

@Getter
@Setter
@AllArgsConstructor
public final class StrafeEvent extends EventCancellable {

    private float forward;
    private float strafe;
    private float friction;
    private float yaw;

    public void setSpeed(final double speed, final double motionMultiplier) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        Minecraft.getInstance().player.setMotionWithMultiplication(motionMultiplier, motionMultiplier);
    }

    public void setSpeed(final double speed) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        Minecraft.getInstance().player.setMotion(0, 0, Minecraft.getInstance().player.getMotion().y);
    }
}
