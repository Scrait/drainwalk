package tech.drainwalk.utils.movement;

public class Move {
    public int oldState;
    public int action;
    public int newState;
    public double reward;

    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
    }
}