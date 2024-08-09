package tech.drainwalk.api.impl.models.module.category;

import lombok.Getter;

public enum Type {
    MAIN("Main"), SECONDARY("Secondary");
    @Getter
    private final String name;

    Type(String name) {
        this.name = name;
    }
}
