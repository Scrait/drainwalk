package tech.drainwalk.api.impl.models.module.category;

import lombok.Getter;
import tech.drainwalk.services.animation.Animation;

public enum Category {
    COMBAT("Combat", 'a'),
    MOVEMENT("Movement", 'b'),
    RENDER("Render", 'c'),
    OVERLAY("Overlay", 'd'),
    MISC("Misc", 'e'),
    CONFIGS("Configs", 'f'),
    SCRIPTS("Scripts", 'g');//,
//    THEMES("Themes", 'h');
    @Getter
    private final Animation hoveredAnimation = new Animation();
    @Getter
    private final Animation animation = new Animation();
    @Getter
    private final String name;
    @Getter
    private final char icon;
    Category(String name, char icon) {
        this.name = name;
        this.icon = icon;
    }
}
