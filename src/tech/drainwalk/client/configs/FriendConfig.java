package tech.drainwalk.client.configs;

import tech.drainwalk.api.impl.models.Config;

public class FriendConfig extends Config {
    public FriendConfig() {
        super("other/FriendCfg");
    }

    @Override
    protected void load() {
        assert readConfig().length() != 0;
//        final String[] friends = readConfig().split(":");
//        for (String s : friends) {
//            final String[] friend = s.split(",");
//            dw.getApiMain().getFriendManager().add(new Friend(friend[0], new Date(Date.parse(friend[1]))));
//        }
    }

    @Override
    protected void save() {
        writeConfig(dw.getApiMain().getFriendManager().toString());
    }
}
