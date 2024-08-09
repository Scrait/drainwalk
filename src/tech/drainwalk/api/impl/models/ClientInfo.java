package tech.drainwalk.api.impl.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientInfo {

    private String CLIENT_NAME;
    private String BUILD;
    private String RELEASE_TYPE;
    private String VERSION;

}
