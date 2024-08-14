package tech.drainwalk.api.managers;

import by.radioegor146.nativeobfuscator.Native;
import lombok.Getter;
import tech.drainwalk.api.impl.interfaces.IFind;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.modules.combat.*;
import tech.drainwalk.client.modules.misc.*;
import tech.drainwalk.client.modules.movement.*;
import tech.drainwalk.client.modules.overlay.*;
import tech.drainwalk.client.modules.render.*;

import java.util.ArrayList;

/**
 * @author Scrait
 * @since 14/08/2023
 *
 * There we can register modules and get them
 *
 */
@Native
@Getter
public class ModuleManager extends ArrayList<Module> implements IManager<Module>, IFind<Module> {

    public ModuleManager() {
        init();
    }

    @Override
    public void init() {
        //COMBAT
            register(new AntiBot());
            register(new Aura());
            register(new AutoPotionBuff());
            register(new Velocity());
            register(new AutoTotem());
            register(new NoServerRotation());
            register(new SuperBow());
            register(new PacketAutoTotem());
            register(new AIHelper());
            register(new AutoGApple());
            register(new BackTrack());
        //MOVEMENT
            register(new Fly());
            register(new Timer());
            register(new Sprint());
            register(new NoSlow());
            register(new NoDelayModule());
            register(new Strafe());
            register(new AirJump());
            register(new Spider());
            register(new WaterSpeed());
            register(new ElytraFireworkFly());
            register(new Speed());
            register(new ClientSounds());
            register(new GuiWalk());
        //OVERLAY
//            register(new ModuleList());
            register(new Menu());
//            register(new Watermark());
//            register(new GuiWalk());
//            register(new TargetHUD());
//            register(new TriangleESP());
//            register(new Crosshair());
//            register(new KeyBinds());
//            register(new Notifications());
//            register(new Hotbar());
//            register(new Shaders());
            register(new Indicators());
        //RENDER
            register(new ESP());
            register(new FullBright());
            register(new ViewModel());
            register(new RemovaIs());
            register(new NameTags());
            register(new Particles());
            register(new JumpCircles());
            register(new ChinaHat());
            register(new Trails());
            register(new WorldFeatures());
        //MISC
            register(new Disabler());
            register(new AutoRespawn());
            register(new DiscordRPC());
            register(new DeathCoordinates());
            register(new MiddleClickPearl());
            register(new MiddleClickFriend());
            register(new NoFriendDamage());
            register(new NoPush());
            register(new ElytraFix());
            register(new FreeCam());
            register(new MiddleClickItem());
    }

    @Override
    public void register(Module module) {
        this.add(module);
    }

    @Override
    public <T extends Module> T findByName(final String name) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    @Override
    public <T extends Module> T findByClass(final Class<T> clazz) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getClass() == clazz)
                .findAny().orElse(null);
    }

}
