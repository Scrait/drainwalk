package tech.drainwalk.services.math;

import lombok.Getter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UniqueRandomNumberGenerator {

    private final Set<Float> previousNumbers;
    private final float minDifference;
    private final float startRange;
    private final float endRange;
    @Getter
    private float current = 1;
    private final int maxCapacity;

    public UniqueRandomNumberGenerator(float start, float end, float minDifference) {
        this.previousNumbers = new HashSet<>();
        this.minDifference = minDifference;
        this.startRange = start;
        this.endRange = end;
        this.maxCapacity = (int)((end - start) / minDifference);
    }

    public void generate() {
        Random random = new Random();

        if (previousNumbers.size() >= maxCapacity) {
            previousNumbers.clear();
        }

        float num;
        do {
            num = startRange + random.nextFloat() * (endRange - startRange);
        } while (!isNumberValid(num));

        // Update the set with the new number and its close neighbors
        for (float i = num - minDifference; i <= num + minDifference; i += 0.01f) { // Increment by a small step to cover the range
            previousNumbers.add(i);
        }

        current = num;
    }

    private boolean isNumberValid(float num) {
        return previousNumbers.stream().noneMatch(existingNum -> Math.abs(existingNum - num) < minDifference);
    }

}
