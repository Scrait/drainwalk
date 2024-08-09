package tech.drainwalk.utils.math;

import tech.drainwalk.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathematicUtils extends Utils {

    public static float interpolate(float prev, float to, float value) {
        return prev + (to - prev) * value;
    }

    public static double interpolate (double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static double round(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static int randomNumber(int min, int max) {
        return (int) (min + (double) (max - min) * Math.random());
    }

    public static float randomNumber(float min, float max) {
        return (float) (min + (double) (max - min) * Math.random());
    }

    public static float clamp(float value, float min, float max) {
        if (value <= min) {
            return min;
        }
        if (value >= max) {
            return max;
        }
        return value;
    }

    public static float lerp(double start, double end, double step) {
        return (float)(start + (end - start) * step);
    }

    public static int getRandomNumberBetween(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static float getRandomNumberBetween(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }

    public static double getRandomNumberBetween(double min, double max) {
        return (Math.random() * (max - min) + min);
    }
}
