package tech.drainwalk.client.modules.misc;

import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;

public class NoFriendDamage extends Module {
    public NoFriendDamage() {
        super("NoFriendDamage", Category.MISC);
        addType(Type.SECONDARY);
    }
}
