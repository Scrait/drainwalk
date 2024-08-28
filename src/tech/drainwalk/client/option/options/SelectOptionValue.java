package tech.drainwalk.client.option.options;

import lombok.Getter;
import tech.drainwalk.services.animation.Animation;

@Getter
public class SelectOptionValue  {
    private final Animation animation = new Animation();
    private final Animation openAnimation = new Animation();

    private final String name;

    public SelectOptionValue(final String name) {
        this.name = name;
    }
}