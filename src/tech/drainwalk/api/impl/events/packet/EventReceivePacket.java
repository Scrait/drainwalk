package tech.drainwalk.api.impl.events.packet;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.IPacket;

public class EventReceivePacket extends EventCancellable {

    private IPacket<?> packet;

    public EventReceivePacket(IPacket<?> packet) {
        this.packet = packet;
    }

    public IPacket<?> getPacket() {
        return this.packet;
    }

    public void setPacket(IPacket<?> packet) {
        this.packet = packet;
    }
}
