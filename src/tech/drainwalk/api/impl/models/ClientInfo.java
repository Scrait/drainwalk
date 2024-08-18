package tech.drainwalk.api.impl.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientInfo {

    private final String CLIENT_NAME;
    private final String CLIENT_TYPE;
    private final String BUILD;
    private final String RELEASE_TYPE;
    private final String VERSION;

}
