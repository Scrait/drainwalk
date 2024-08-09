package tech.drainwalk.utils.time;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import tech.drainwalk.utils.Utils;

@Getter
public class Timer extends Utils {

    public long lastMs = 0L;

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public void setLastMs(long newValue) {
        lastMs = System.currentTimeMillis() + newValue;
    }

    public boolean delay(long nextDelay) {
        return System.currentTimeMillis() - this.lastMs >= nextDelay;
    }

    public boolean delay(float nextDelay, boolean reset) {
        if ((float)(System.currentTimeMillis() - this.lastMs) >= nextDelay) {
            if (reset) {
                this.reset();
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean isDelayComplete(double valueState) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= valueState;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public boolean hasTimeElapsed() {
        return lastMs < System.currentTimeMillis();
    }

}
