/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod;

import dev.cloudmc.feature.mod.impl.*;
import dev.cloudmc.feature.mod.impl.FreelookMod;

import java.util.ArrayList;

public class ModManager {

    public ArrayList<Mod> mods = new ArrayList<>();

    public ModManager() {
        init();
    }

    /**
     * Initializes all mods
     */

    public void init() {
        addMod(new ToggleSprintMod());
        addMod(new ToggleSneakMod());
        addMod(new FpsMod());
        addMod(new KeystrokesMod());
        addMod(new ArmorMod());
        addMod(new FullbrightMod());
        addMod(new SnaplookMod());
        addMod(new CoordinatesMod());
        addMod(new ServerAddressMod());
        addMod(new PingMod());
        addMod(new CpsMod());
        addMod(new PotionMod());
        addMod(new TimeMod());
        addMod(new SpeedIndicatorMod());
        addMod(new AnimationMod());
        addMod(new FreelookMod());
        addMod(new CrosshairMod());
        addMod(new MotionblurMod());
        addMod(new GuiBlurMod());
        addMod(new BlockOverlayMod());
        addMod(new BlockInfoMod());
        addMod(new ReachDisplayMod());
        addMod(new ZoomMod());
        addMod(new DayCounterMod());
        addMod(new NoHurtCamMod());
        addMod(new ScrollTooltipsMod());
        addMod(new ParticleMultiplierMod());
        addMod(new NickHiderMod());
        addMod(new ScoreboardMod());
        addMod(new BossbarMod());
        addMod(new DirectionMod());
    }

    /**
     * @return Returns an Arraylist of all mods
     */

    public ArrayList<Mod> getMods() {
        return mods;
    }

    /**
     * Returns a given mod using its name
     * @param name The name of the mod
     * @return The returned mod
     */

    public Mod getMod(String name) {
        for (Mod m : mods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }

        return null;
    }

    /**
     * Adds a mod to the list
     * @param mod The mod which should be added
     */

    public void addMod(Mod mod) {
        mods.add(mod);
    }
}
