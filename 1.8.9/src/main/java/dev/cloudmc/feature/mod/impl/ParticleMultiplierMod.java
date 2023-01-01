/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticleMultiplierMod extends Mod {

    public ParticleMultiplierMod() {
        super(
                "ParticleMultiplier",
                "Multiplies or adds Particles by a given amount."
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Particle Amount", this, 15, 5));
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent e) {
        if (e.target instanceof EntityLivingBase) {
            boolean doCriticalDamage =
                    Cloud.INSTANCE.mc.thePlayer.fallDistance > 0.0F &&
                    !Cloud.INSTANCE.mc.thePlayer.onGround &&
                    !Cloud.INSTANCE.mc.thePlayer.isOnLadder() &&
                    !Cloud.INSTANCE.mc.thePlayer.isInWater() &&
                    !Cloud.INSTANCE.mc.thePlayer.isPotionActive(Potion.blindness) &&
                            Cloud.INSTANCE.mc.thePlayer.ridingEntity == null;

            for (int i = 0; i < getAmount(); i++) {
                Cloud.INSTANCE.mc.thePlayer.onEnchantmentCritical(e.target);
                if (doCriticalDamage) {
                    Cloud.INSTANCE.mc.thePlayer.onCriticalHit(e.target);
                }
            }

        }
    }

    private float getAmount() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Particle Amount").getCurrentNumber();
    }
}
