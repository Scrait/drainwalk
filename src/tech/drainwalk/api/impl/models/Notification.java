package tech.drainwalk.api.impl.models;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.services.animation.Animation;

@Getter
public class Notification {

    private final String text;
    private final String header;
    private final Type type;

    private final long startTime;

    @Setter
    private boolean direction = true;

    private final Animation animation = new Animation();

    public Notification(String header, String text, Type type) {
        this.text = text;
        this.header = header;
        this.type = type;
        startTime = System.currentTimeMillis();
    }

    public enum Type {
        WARNING,
        WRONG,
        CORRECT
    }

}
