package tech.drainwalk.client.modules.combat;

import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;

public class Blink extends Module {
    public Blink() {
        super("Blink", Category.COMBAT);
        addType(Type.SECONDARY);
    }
}
