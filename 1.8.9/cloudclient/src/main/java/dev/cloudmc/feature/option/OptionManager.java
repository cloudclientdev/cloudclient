/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.option;

import dev.cloudmc.gui.modmenu.impl.sidebar.options.type.CellGrid;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.type.Keybinding;
import org.lwjgl.input.Keyboard;
import scala.tools.cmd.gen.AnyValReps;

import java.awt.*;
import java.util.ArrayList;

public class OptionManager {

    public ArrayList<Option> optionList;

    public OptionManager() {
        optionList = new ArrayList<>();
        init();
    }

    public void init() {
        addOption(new Option("Font Changer", "Arial", 0,
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        addOption(new Option("Color", new Color(255, 255, 255)));
        addOption(new Option("Rounded Corners", true));
        addOption(new Option("ModMenu Keybinding", Keyboard.KEY_RSHIFT));
    }

    /**
     * Adds an option to the options List
     *
     * @param option The option to be added
     */

    public void addOption(Option option) {
        optionList.add(option);
    }

    /**
     * @return Returns an Arraylist of all options
     */

    public ArrayList<Option> getOptions() {
        return optionList;
    }

    /**
     * Returns an option with a given name
     *
     * @param name The option name
     * @return The option
     */

    public Option getOptionByName(String name) {
        for (Option option : optionList) {
            if (option.getName().equalsIgnoreCase(name)) {
                return option;
            }
        }
        return null;
    }
}
