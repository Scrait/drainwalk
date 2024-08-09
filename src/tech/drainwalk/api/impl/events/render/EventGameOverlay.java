package tech.drainwalk.api.impl.events.render;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventGameOverlay extends EventCancellable implements Event  {
    private final OverlayType overlayType;

    public EventGameOverlay(OverlayType overlayType) {
        this.overlayType = overlayType;
    }

    public OverlayType getOverlayType() {
        return overlayType;
    }

    public enum OverlayType {
        Hurt, PumpkinOverlay, TotemPop, CameraBounds, Fire, Light, BossBar, Fog, WaterFog, LavaFog
    }
}
