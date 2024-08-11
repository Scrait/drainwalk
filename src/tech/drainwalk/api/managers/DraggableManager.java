package tech.drainwalk.api.managers;

import tech.drainwalk.api.impl.interfaces.IFind;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.client.draggables.Keybinds;
import tech.drainwalk.client.draggables.Potions;
import tech.drainwalk.client.draggables.TargetInfo;
import tech.drainwalk.client.draggables.Watermark;

import java.util.ArrayList;

public class DraggableManager extends ArrayList<DraggableComponent> implements IManager<DraggableComponent>, IFind<DraggableComponent> {

    public DraggableManager() {
        init();
    }

    @Override
    public void init() {
        register(new Watermark());
        register(new Keybinds());
        register(new Potions());
        register(new TargetInfo());
    }

    @Override
    public void register(DraggableComponent draggableComponent) {
        this.add(draggableComponent);
    }

    @Override
    public <T extends DraggableComponent> T findByName(final String name) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    @Override
    public <T extends DraggableComponent> T findByClass(final Class<T> clazz) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getClass() == clazz)
                .findAny().orElse(null);
    }

}
