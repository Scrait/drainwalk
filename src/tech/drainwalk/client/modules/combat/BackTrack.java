package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CKeepAlivePacket;
import net.minecraft.util.math.vector.Vector3d;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventSendPacket;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.utils.render.BackTrackUtils;

import java.util.ArrayList;
import java.util.List;

public class BackTrack extends Module {

    private long id;
    private short tsid;
    private int twid;
    public static final List<BackTrackUtils> backEntityCoords = new ArrayList<>();
    public final FloatOption range = new FloatOption("Range", 5.0f, 1.0f, 15.0f);

    public BackTrack() {
        super("BackTrack", Category.COMBAT);
        addType(Type.SECONDARY);
        register(range);
    }

    @Override
    public void onEnable() {
//        if (Drainwalk.moduleManager.esp.isEnabled()) {
//            Drainwalk.moduleManager.esp.toggle();
//            Drainwalk.moduleManager.esp.toggle();
//        }
    }

    @EventTarget
    public void onEvent(final UpdateEvent event) {

        backEntityCoords.forEach(BackTrackUtils::tick);
        for (BackTrackUtils backEntityCoord : backEntityCoords) {
            backEntityCoord.positions.forEach(BackTrackUtils.Point::update);
            backEntityCoord.positions.removeIf(point -> point.ticks > range.getValue());
        }
        for (PlayerEntity entity : mc.world.getPlayers()) {
            if (entity.getShouldBeDead() || entity.isInvisible() || !entity.isAlive() || entity.getHealth() <= 0)
                continue;
            if (entity.isInvisible() || !entity.isAlive() || entity.getHealth() <= 0) {
                backEntityCoords.clear();
            }
            if (backEntityCoords.stream().map(backEntityCoord -> backEntityCoord.entity).noneMatch(entity1 -> entity1 == entity)) {
                backEntityCoords.add(new BackTrackUtils(entity));
            }
        }

    }

    @EventTarget
    public void onRender(final EventRender3D e) {
//        ChatUtils.addChatMessage("sdsadad");
//        RenderUtils.drawRect(0,0,50,50,-1);
//        BackTrack.backEntityCoords.forEach(BackTrackUtils::renderPositions);
    }

    @EventTarget
    public void onPacket(final EventSendPacket e) {
        if (e.getPacket() instanceof CConfirmTransactionPacket) {
            if (this.tsid == ((CConfirmTransactionPacket) e.getPacket()).getUid() && this.twid == ((CConfirmTransactionPacket) e.getPacket()).getWindowId()) {
                return;
            }
            e.setCancelled(true);
            (new Thread(() -> {
                try {
                    Thread.sleep((long) (range.getValue() * 100));
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }

                this.tsid = ((CConfirmTransactionPacket) e.getPacket()).getUid();
                this.twid = ((CConfirmTransactionPacket) e.getPacket()).getWindowId();
                if (mc.player != null) {
                    if (mc.player.connection != null) {
                        mc.player.connection.sendPacket(e.getPacket());
                    }
                }
            })).start();
        }
        if (e.getPacket() instanceof CKeepAlivePacket) {
            if (this.id == ((CKeepAlivePacket) e.getPacket()).getKey()) {
                return;
            }

            e.setCancelled(true);
            (new Thread(() -> {
                try {
                    Thread.sleep((long) (range.getValue() * 100));
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }

                this.id = ((CKeepAlivePacket) e.getPacket()).getKey();
                assert mc.player != null;
                mc.player.connection.sendPacket(e.getPacket());
            })).start();
        }
    }

    public Vector3d nearestPosition(final BackTrackUtils entityCoord) {
        if (entityCoord.positions.size() > 0) {
            return entityCoord.positions.stream().min((o1, o2) -> (int) (o1.vec3d.distanceTo(mc.player) - o2.vec3d.distanceTo(mc.player))).get().vec3d;
        } else {
            return entityCoord.entity.getPositionVec();
        }
    }

    public BackTrackUtils getEntity(final Entity e) {
        for (BackTrackUtils backEntityCoord : backEntityCoords) {
            if (backEntityCoord.entity == e) {
                return backEntityCoord;
            }
        }
        return null;
    }
}
