/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.helpers.font;

public class FontHelper {

    private String font = "Arial Rounded MT Bold";

    public GlyphPageFontRenderer size15;
    public GlyphPageFontRenderer size20;
    public GlyphPageFontRenderer size30;
    public GlyphPageFontRenderer size40;

    public FontHelper(){
        init();
    }

    public void init(){
        size15 = GlyphPageFontRenderer.create(font, 15, false, false, false);
        size20 = GlyphPageFontRenderer.create(font, 20, false, false, false);
        size30 = GlyphPageFontRenderer.create(font, 30, false, false, false);
        size40 = GlyphPageFontRenderer.create(font, 40, false, false, false);
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }
}
