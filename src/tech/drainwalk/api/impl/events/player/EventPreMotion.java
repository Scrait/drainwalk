package tech.drainwalk.api.impl.events.player;

import com.darkmagician6.eventapi.events.Event;

public class EventPreMotion implements Event {

    private float yaw, pitch;
    private double posX, posY, posZ;
    private boolean onGround;

    public EventPreMotion(float yaw, float pitch, double posX, double posY, double posZ, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.onGround = onGround;
    }

    public void setRotation(float yaw, float pitch, boolean clientRotation) {
        if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90)
            return;

        this.yaw = yaw;
        this.pitch = pitch;

        if (clientRotation) {
            setYaw(yaw);
            setPitch(pitch);
        }
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
