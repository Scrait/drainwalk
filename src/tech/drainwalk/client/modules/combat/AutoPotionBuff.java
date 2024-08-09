package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.utils.time.Timer;

public class AutoPotionBuff extends Module {
    private final Timer timerUtils = new Timer();
    private final MultiOption potions = new MultiOption("Types", new MultiOptionValue("Strength", false), new MultiOptionValue("Speed", false), new MultiOptionValue("Ogon", false), new MultiOptionValue("Health", false));
    private final FloatOption health = new FloatOption("Health", 4f, 0f, 20f).addIncrementValue(0.1f).addVisibleCondition(() -> potions.isSelected("Health"));
    private final BooleanOption onlyGround = new BooleanOption("Only on ground", true);

    public AutoPotionBuff() {
        super("AutoPotionBuff", Category.COMBAT);
        addType(Type.SECONDARY);
        register(
                potions,
                health,
                onlyGround
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (isThrowed() && timerUtils.isDelayComplete(500) && ((!onlyGround.getValue() && mc.player.fallDistance > 0) || mc.player.isOnGround())) {
            //float predPith = mc.player.rotationPitch;
            mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(),
                    mc.player.rotationYawHead, 90, mc.player.isOnGround()));
            /*mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ,
                    mc.player.rotationYaw, predPith, mc.player.onGround));*/
            if (!mc.player.isPotionActive(Effects.SPEED) && isPotionOnHotBar(Potions.SPEED)
                    && potions.isSelected("Speed")) {
                throwPotion(Potions.SPEED);
            }
            if (!mc.player.isPotionActive(Effects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)
                    && potions.isSelected("Strength")) {
                throwPotion(Potions.STRENGTH);
            }
            if (!mc.player.isPotionActive(Effects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)
                    && potions.isSelected("Ogon")) {
                throwPotion(Potions.FIRERES);
            }
            if (mc.player.getHealth() + mc.player.getAbsorptionAmount() < health.getValue() && potions.isSelected("Health")
                    && isPotionOnHotBar(Potions.HEAL)) {
                throwPotion(Potions.HEAL);
            }
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
            timerUtils.reset();
        }
    }

//    @EventTarget
//    public void onSync(EventEntitySync event) {
//        if (isThrowed() && ((!onlyGround.getValue() && mc.player.fallDistance > 0) || mc.player.isOnGround())) {
//            event.setSprint(false);
//            mc.player.setSprinting(false);
//        }
//    }

    private boolean isThrowed() {
        boolean throwed = (!mc.player.isPotionActive(Effects.SPEED) && isPotionOnHotBar(Potions.SPEED)
                && potions.isSelected("Speed"))
                || (!mc.player.isPotionActive(Effects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)
                && potions.isSelected("Strength"))
                || (!mc.player.isPotionActive(Effects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)
                && potions.isSelected("Ogon"))
                || mc.player.getHealth() + mc.player.getAbsorptionAmount() < health.getValue() && potions.isSelected("Health")
                && isPotionOnHotBar(Potions.HEAL);
        return throwed;
    }


    private void throwPotion(Potions potion) {
        int slot = getPotionSlot(potion);
        mc.player.connection.sendPacket(new CHeldItemChangePacket(slot));
        mc.playerController.tick();
        mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
        mc.playerController.tick();
    }

    private static int getPotionSlot(Potions potion) {
        for (int i = 0; i < 9; ++i) {
            if (isStackPotion(mc.player.inventory.getStackInSlot(i), potion)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isPotionOnHotBar(Potions potions) {
        return getPotionSlot(potions) != -1;
    }

    private static boolean isStackPotion(ItemStack stack, Potions potion) {
        if (stack == null)
            return false;

        Item item = stack.getItem();

        if (item == Items.SPLASH_POTION) {
            int id = 0;

            switch (potion) {
                case STRENGTH: {
                    id = 5;
                    break;
                }
                case SPEED: {
                    id = 1;
                    break;
                }
                case FIRERES: {
                    id = 12;
                    break;
                }
                case HEAL: {
                    id = 6;
                }
            }

            for (EffectInstance effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == Effect.get(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private enum Potions {
        STRENGTH, SPEED, FIRERES, HEAL
    }
}
