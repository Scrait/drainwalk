package tech.drainwalk.api.impl.events.modules;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoPushEvent extends EventCancellable {

    private final PushType pushType;

    public enum PushType {
        Blocks, Entities, Water
    }
}
