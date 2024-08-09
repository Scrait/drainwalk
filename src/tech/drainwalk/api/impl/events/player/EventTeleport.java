package tech.drainwalk.api.impl.events.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.network.play.client.CPlayerPacket;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class EventTeleport extends EventCancellable {

    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;

}