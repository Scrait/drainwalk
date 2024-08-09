package tech.drainwalk.services.math;

import lombok.experimental.UtilityClass;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

@UtilityClass
public class GCDService implements IInstanceAccess {

    public float getFixedRotation(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

    public float getGCDValue() {
        return (float) (getGCD() * 0.15);
    }

    public float getGCD() {
        float f1;

        double var11 = mc.gameSettings.mouseSensitivity / 0.15F / 8.0D;
        double var9 = Math.cbrt(var11);

        return (f1 = (float) ((var9 - 0.2f) / 0.6f * 0.6 + 0.2)) * f1 * f1 * 8;
    }

    public float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }

}