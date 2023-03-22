/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticleMultiplierMod extends Mod {

    public ParticleMultiplierMod() {
        super(
                "ParticleMultiplier",
                "Multiplies or adds Particles by a given amount.",
                Type.Visual
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Particle Amount", this, 15, 5));
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent e) {
        if (e.getTarget() instanceof EntityLivingBase) {
            boolean doCriticalDamage =
                    Cloud.INSTANCE.mc.player.fallDistance > 0.0F &&
                    !Cloud.INSTANCE.mc.player.onGround &&
                    !Cloud.INSTANCE.mc.player.isOnLadder() &&
                    !Cloud.INSTANCE.mc.player.isInWater() &&
                    !Cloud.INSTANCE.mc.player.isPotionActive(MobEffects.BLINDNESS) &&
                            Cloud.INSTANCE.mc.player.getRidingEntity() == null;

            for (int i = 0; i < getAmount(); i++) {
                Cloud.INSTANCE.mc.player.onEnchantmentCritical(e.getTarget());
                if (doCriticalDamage) {
                    Cloud.INSTANCE.mc.player.onCriticalHit(e.getTarget());
                }
            }

        }
    }

    private float getAmount() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Particle Amount").getCurrentNumber();
    }
}
