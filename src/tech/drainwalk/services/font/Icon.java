package tech.drainwalk.services.font;

import lombok.Getter;

@Getter
public enum Icon {

    COMBAT('a'),
    MOVEMENT('b'),
    RENDER('c'),
    OVERLAY('d'),
    MISC('e'),
    CONFIG('f'),
    SCRIPT('g'),
    OPTIONS('h'),
    PROFILE('i'),
    SUCCESS('g'),
    ERROR('k'),
    UNKNOWN('l'),
    WARNING('m'),
    PROFILE2('n'),
    LOGO('w');

    private final char symbol;

    Icon(char symbol) {
        this.symbol = symbol;
    }

}
