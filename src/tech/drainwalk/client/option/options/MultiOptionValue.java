package tech.drainwalk.client.option.options;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.services.animation.Animation;

@Getter
public class MultiOptionValue {
    private final Animation animation = new Animation();
    private final Animation hoveredAnimation = new Animation();
    private final String name;
    @Setter
    private boolean toggle;

    public MultiOptionValue(final String name, final boolean toggle) {
        this.name = name;
        this.toggle = toggle;
    }
}