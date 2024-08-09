package tech.drainwalk.utils.bot;

import com.github.chen0040.rl.learning.qlearn.QLearner;
import tech.drainwalk.utils.minecraft.ChatUtils;
import tech.drainwalk.utils.time.Timer;

public class QBot extends Bot {

    private final QLearner agent;
    private final Timer timer = new Timer();

    public QBot(QLearner agent) {
        this.agent = agent;
    }

    @Override
    protected int selectAction(int state) {
        double q0 = this.agent.getModel().getQ(state, 0);
        double q1 = this.agent.getModel().getQ(state, 1);
        double q2 = this.agent.getModel().getQ(state, 2);
        ChatUtils.addChatMessage("Q0 -> " + q0 + ", Q1 -> " + q1);
        if(q0 >= q1) return 0;
        return 1;
    }

    @Override
    protected int selectAction2(int state) {
        double q0 = this.agent.getModel().getQ(state, 2);
        double q1 = this.agent.getModel().getQ(state, 3);
        ChatUtils.addChatMessage("Q2 -> " + q1 + ", Q3 -> " + q1);
        if(q0 > q1) return 0;
        if(q0 == q1) return 2;
        return 1;
    }

    @Override
    public void updateStrategy(int yawDelta) {
        for(int i = moves.size()-1; i >=0; --i){
            Move move = moves.get(i);
            double r = move.reward;
//            if (i == moves.size() - 1 || i == moves.size() - 2) {
                r = -Math.abs(yawDelta);
                if (timer.delay(1000, true) && r < -80) {
                    reset();
                    ChatUtils.addChatMessage("Update Strategy Yaw");
                }
//            }
            ChatUtils.addChatMessage("Reward -> " + r);
            agent.update(move.oldState, move.action, move.newState, r);
        }
    }

    @Override
    public void updateStrategy2() {
        for(int i = moves.size()-1; i >=0; --i){
            Move move = moves.get(i);
            double r = move.reward;
            if (i == moves.size() - 1 || i == moves.size() - 2) {
                r = -1000;
            }
            agent.update(move.oldState, move.action, move.newState, r);
        }
        reset2();

    }
}
