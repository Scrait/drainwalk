package tech.drainwalk.api.managers;

import by.radioegor146.nativeobfuscator.NotNative;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.Friend;

import java.util.ArrayList;

public class FriendManager extends ArrayList<Friend> implements IManager<Friend> {

    @Override
    public void register(Friend friend) {
        this.add(friend);
    }

    @Override
    public String toString() {
        if (this.isEmpty()) return "";
        final StringBuilder friends = new StringBuilder();
        for (Friend friend : this) {
            friends.append(friend.getName()).append(",").append(friend.getAddDate().toString()).append(":");
        }
        return friends.substring(friends.length() - 1);
    }

    public boolean existsByName(final String name) {
        // noinspection unchecked
        return this.stream()
                .filter(friend -> friend.getName().equalsIgnoreCase(name))
                .findAny().orElse(null) != null;
    }

    @NotNative
    public void removeByName(final String name) {
        // noinspection unchecked
        this.remove(this.stream()
                .filter(friend -> friend.getName().equalsIgnoreCase(name))
                .findAny().orElse(null));
    }
}
