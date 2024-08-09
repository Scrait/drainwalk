package tech.drainwalk.api.impl.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class Friend {
    private final String name;
    private final Date addDate;
}
