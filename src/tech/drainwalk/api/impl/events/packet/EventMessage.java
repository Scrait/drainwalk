package tech.drainwalk.api.impl.events.packet;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventMessage extends EventCancellable {

    private final String message;

}
