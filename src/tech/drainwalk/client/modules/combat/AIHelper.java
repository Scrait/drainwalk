package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.github.chen0040.rl.actionselection.EpsilonGreedyActionSelectionStrategy;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.utils.bot.Bot;
import tech.drainwalk.utils.bot.QBot;

import java.util.ArrayList;
import java.util.List;

public class AIHelper extends Module {

    private Bot bot;
    private QLearner learner;

    public AIHelper() {
        super("AIHelper", Category.COMBAT);
        learner = new QLearner(2, 2);
        learner.getModel().setAlpha(0.8);
        learner.getModel().setGamma(0.999);
        learner.setActionSelection(EpsilonGreedyActionSelectionStrategy.class.getCanonicalName());
    }

    @Override
    public void onEnable() {
        bot = new QBot(learner);
    }

    @EventTarget
    public void onSync(UpdateEvent event) {
        int act = bot.act();
        if (act == 1) {
            mc.player.rotationYaw += 5;
        } else if (act == 0) {
            mc.player.rotationYaw -= 5;
        }
//        ChatUtils.addChatMessage(learner.toJson());
//        File file = new File("C:/Users/scrai/Desktop/AI.drain");
//
//        try (PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8))
//        {
//            out.print(learner.toJson());
//            System.out.println("Successfully written data to the file");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (bot.act2() == 1) {
//            mc.player.rotationPitch += 1;
//        } else {
//            mc.player.rotationPitch -= 1;
//        }

//        int rewardPitch;
//        if (pitchDelta > -10 && pitchDelta < 20) {
//            rewardPitch = 1;
//        } else {
//            rewardPitch = 0;
//        }
//        ChatUtils.addChatMessage("RewardYaw -> " + rewardYaw);
//        ChatUtils.addChatMessage("RewardPitch -> " + rewardPitch);
//
//        qLearner.update(prevStateYaw, 0, yawDelta, rewardYaw);
//        //qLearner.update(prevState, 1, state, mc.player.getDistance(findTarget()));
//        ChatUtils.addChatMessage("QYaw -> " + (float)qLearner.getModel().getQ(yawDelta, 0));
//        mc.player.rotationYaw = (float)qLearner.getModel().getQ(yawDelta, 0);
//        prevStateYaw = yawDelta;

//        qLearner.update(prevStatePitch, 0, pitchDelta, rewardPitch);
//        //qLearner.update(prevState, 1, state, mc.player.getDistance(findTarget()));
//        ChatUtils.addChatMessage("QPitch -> " + (float)qLearner.getModel().getQ(pitchDelta, 0));
//        mc.player.rotationPitch = (float)qLearner.getModel().getQ(pitchDelta, 0);
//        prevStatePitch = pitchDelta;

//        try(FileWriter writer = new FileWriter("C:/Users/scrai/Desktop/AI.drain", false))
//        {
//            final String json = qLearner.toJson();
//            writer.write(json);
//            writer.flush();
//        }
//        catch(IOException ex){
//            System.out.println(ex.getMessage());
//        }
    }

//    private int selectAction(int state) {
//        double q0 = qLearner.getModel().getQ(state, 0);
//        double q1 = qLearner.getModel().getQ(state, 1);
//        ChatUtils.addChatMessage(String.valueOf(q1));
//        if(q0 >= q1) return 0;
//        return 1;
//    }

    private Entity findTarget() {
        final List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity && entity != mc.player) {
                targets.add((LivingEntity) entity);
            }
        }
        targets.sort((e1, e2) -> {
            final int dst1 = (int) (mc.player.getDistance(e1) * 1000);
            final int dst2 = (int) (mc.player.getDistance(e2) * 1000);
            return dst1 - dst2;
        });

        return targets.isEmpty() ? null : targets.get(0);
    }
}
