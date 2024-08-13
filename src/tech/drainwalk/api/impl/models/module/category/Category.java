package tech.drainwalk.api.impl.models.module.category;

import lombok.Getter;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.font.Icon;

@Getter
public enum Category {

    COMBAT("Combat", Icon.COMBAT),
    MOVEMENT("Movement", Icon.MOVEMENT),
    RENDER("Render", Icon.RENDER),
    OVERLAY("Overlay", Icon.OVERLAY),
    MISC("Misc", Icon.MISC),
    CONFIGS("Configs", Icon.CONFIG),
    SCRIPTS("Scripts", Icon.SCRIPT),
    THEMES("Themes", Icon.OPTIONS);

    private final Animation hoveredAnimation = new Animation();
    private final Animation animation = new Animation();
    private final String name;
    private final Icon icon;

    Category(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

}
