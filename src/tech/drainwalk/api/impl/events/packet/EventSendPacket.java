package tech.drainwalk.api.impl.events.packet;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.IPacket;

public class EventSendPacket extends EventCancellable {

    private final IPacket<?> packet;

    public EventSendPacket(IPacket packet) {
        this.packet = packet;
    }

    public IPacket getPacket() {
        return this.packet;
    }
}
