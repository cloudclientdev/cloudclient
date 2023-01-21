/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AnimationMod extends Mod {

    @SubscribeEvent
    public void onAnimation(TickEvent.ClientTickEvent e) {
        if (Cloud.INSTANCE.mc.theWorld == null || Cloud.INSTANCE.mc.thePlayer == null) return;
        if (e.phase == TickEvent.Phase.START) return;
        ItemStack heldItem = Cloud.INSTANCE.mc.thePlayer.getHeldItem();
        if (Cloud.INSTANCE.modManager.getMod("Animation").isToggled() && heldItem != null) {
            if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled() && heldItem.getItemUseAction() == EnumAction.BLOCK) {
                attemptSwing();
            } else if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Eat/Drink Animation").isCheckToggled() && heldItem.getItemUseAction() == EnumAction.DRINK) {
                attemptSwing();
            } else if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Bow Animation").isCheckToggled() && heldItem.getItemUseAction() == EnumAction.BOW) {
                attemptSwing();
            }
        }
    }

    /**
     * Swings the player's arm if you're holding the attack and use item keys at the same time and looking at a block.
     */
    private void attemptSwing() {
        if (Cloud.INSTANCE.mc.thePlayer.getItemInUseCount() > 0) {
            final boolean mouseDown = Cloud.INSTANCE.mc.gameSettings.keyBindAttack.isKeyDown() &&
                    Cloud.INSTANCE.mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && Cloud.INSTANCE.mc.objectMouseOver != null && Cloud.INSTANCE.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                forceSwingArm();
            }
        }
    }

    /**
     * Forces the player to swing their arm.
     */
    private void forceSwingArm() {
        EntityPlayerSP player = Cloud.INSTANCE.mc.thePlayer;
        int swingEnd = player.isPotionActive(Potion.digSpeed) ?
                (6 - (1 + player.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (player.isPotionActive(Potion.digSlowdown) ?
                (6 + (1 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!player.isSwingInProgress || player.swingProgressInt >= swingEnd / 2 || player.swingProgressInt < 0) {
            player.swingProgressInt = -1;
            player.isSwingInProgress = true;
        }
    }
    
    public AnimationMod() {
        super(
                "Animation",
                "1.7 Animations in 1.8."
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Block Animation", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Eat/Drink Animation", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Bow Animation", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Fishing Rod", this, true));


    }
}
