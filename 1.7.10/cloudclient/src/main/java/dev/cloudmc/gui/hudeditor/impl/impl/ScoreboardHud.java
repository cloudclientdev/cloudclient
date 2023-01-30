/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.GLHelper;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardHud extends HudMod {

    public ScoreboardHud(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            ScoreObjective scoreobjective = Cloud.INSTANCE.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);

            if (scoreobjective != null) {
                if(isModern()) {
                    drawModernScoreboard(scoreobjective, isBackground(), isRemoveNumbers());
                } else {
                    drawLegacyScoreboard(scoreobjective, isBackground(), isRemoveNumbers());
                }
            } else {
                GLHelper.endScale();
                return;
            }

            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            ScoreObjective scoreobjective = Cloud.INSTANCE.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);

            if (scoreobjective != null) {
                if(isModern()) {
                    drawModernScoreboard(scoreobjective, isBackground(), isRemoveNumbers());
                } else {
                    drawLegacyScoreboard(scoreobjective, isBackground(), isRemoveNumbers());
                }
            }
        }
        GLHelper.endScale();
    }

    private void drawLegacyScoreboard(ScoreObjective objective, boolean background, boolean numbers) {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null &&
                !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());

        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int displayText = Cloud.INSTANCE.mc.fontRendererObj.getStringWidth(objective.getDisplayName()) + 2;

        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String text = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            displayText = Math.max(displayText, Cloud.INSTANCE.mc.fontRendererObj.getStringWidth(text));
        }

        int y = getY();
        int x = getX();

        int textHeight = Cloud.INSTANCE.mc.fontRendererObj.FONT_HEIGHT;

        int index = collection.size() - 1;
        for (Score score1 : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score1.getPlayerName());
            String mainText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score1.getPlayerName());
            String redNumbers = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int calculatedY = y + index * textHeight;

            if (index == 0) {
                String topText = objective.getDisplayName();
                if(background) {
                    Helper2D.drawRectangle(x, calculatedY, displayText + 4, textHeight, 1610612736);
                    Helper2D.drawRectangle(x, calculatedY + textHeight, displayText + 4, 1, 1342177280);
                }
                Cloud.INSTANCE.mc.fontRendererObj.drawString(topText, x + 2 + displayText / 2 - Cloud.INSTANCE.mc.fontRendererObj.getStringWidth(topText) / 2, calculatedY + 1, -1);
            }

            if(background) {
                Helper2D.drawRectangle(x, calculatedY + textHeight + 1, displayText + 4, textHeight, 1342177280);
            }
            Cloud.INSTANCE.mc.fontRendererObj.drawString(mainText, x + 2, calculatedY + textHeight + 1, -1);
            if (!numbers) {
                Cloud.INSTANCE.mc.fontRendererObj.drawString(redNumbers, x + 4 + displayText - Cloud.INSTANCE.mc.fontRendererObj.getStringWidth(redNumbers), calculatedY + textHeight + 1, -1);
            }

            index--;
        }

        setW(displayText + 4);
        setH((collection.size() + 1) * textHeight);
    }

    private void drawModernScoreboard(ScoreObjective objective, boolean background, boolean numbers) {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null &&
                !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());

        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int displayText = Cloud.INSTANCE.fontHelper.size20.getStringWidth(objective.getDisplayName()) + 2;

        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String text = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            displayText = Math.max(displayText, Cloud.INSTANCE.fontHelper.size20.getStringWidth(text)) + 1;
        }

        int y = getY();
        int x = getX();

        int textHeight = 9;

        int index = collection.size() - 1;
        for (Score score1 : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score1.getPlayerName());
            String mainText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score1.getPlayerName());
            String redNumbers = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int calculatedY = y + index * textHeight;

            if (index == 0) {
                String topText = objective.getDisplayName();
                if(background) {
                    Helper2D.drawRoundedRectangle(x, calculatedY, displayText + 4, textHeight, 2, 1610612736, 1);
                    Helper2D.drawRectangle(x, calculatedY + textHeight, displayText + 4, 1, 1342177280);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString(topText, x + 2 + displayText / 2f - Cloud.INSTANCE.fontHelper.size20.getStringWidth(topText) / 2f, calculatedY + 1, -1);
            }

            if(background) {
                if(index == collection.size() - 1) {
                    Helper2D.drawRoundedRectangle(x, calculatedY + textHeight + 1, displayText + 4, textHeight, 2, 1342177280, 2);
                } else {
                    Helper2D.drawRectangle(x, calculatedY + textHeight + 1, displayText + 4, textHeight, 1342177280);
                }
            }
            Cloud.INSTANCE.fontHelper.size20.drawString(mainText, x + 2, calculatedY + textHeight + 1, -1);
            if (!numbers) {
                Cloud.INSTANCE.fontHelper.size20.drawString(redNumbers, x + 4 + displayText - Cloud.INSTANCE.fontHelper.size20.getStringWidth(redNumbers) - 5, calculatedY + textHeight + 1, -1);
            }

            index--;
        }

        setW(displayText + 4);
        setH((collection.size() + 1) * textHeight);
    }

    private boolean isModern() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isRemoveNumbers() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Remove Red Numbers").isCheckToggled();
    }
}
