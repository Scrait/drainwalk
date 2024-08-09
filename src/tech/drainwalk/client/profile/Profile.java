package tech.drainwalk.client.profile;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class Profile {
    private final String username;
    private final int uid;
    private final String subscriptionTill;
    private final String role;

    public Profile(String username, int uid, String subscriptionTill, String role) {
        this.username = username;
        this.uid = uid;
        this.subscriptionTill = subscriptionTill;
        this.role = role;
    }

    public String getAvatar() {
        return "Avatar";
    }
    public String getUsername() {
        return this.username;
    }
    public int getUid() {
        return this.uid;
    }
    public String getSubscriptionTill() {
        return this.subscriptionTill;
    }
    public String getRole() {
        return this.role;
    }
}
