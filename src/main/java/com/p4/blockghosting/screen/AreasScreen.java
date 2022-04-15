package com.p4.blockghosting.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class AreasScreen extends BaseBlockGhostingScreen {
    public AreasScreen(Screen prevScreen) {
        super(prevScreen);
    }
    @Override
    public void addWidgets() {
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 90,
                10,
                0,
                0,
                new LiteralText("v"),
                click -> {}));
    }
}
