package com.p4.blockghosting.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BaseBlockGhostingScreen extends Screen {

    private final Screen prevScreen;
    private MatrixStack matrices;
    public BaseBlockGhostingScreen(Screen prevScreen) {
        super(new LiteralText(""));
        this.prevScreen = prevScreen;
    }

    public void addWidgets() {
    }

    public void init() {
        this.addWidgets();
        this.addDrawableChild(new ButtonWidget(
                40,
                this.height - 40,
                90,
                20,
                new LiteralText("Back"),
                click -> this.client.setScreen(this.prevScreen))
        );
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 135,
                20,
                90,
                20,
                new LiteralText("Areas"),
                click -> this.client.setScreen(new AreasScreen(this.prevScreen)))
        );
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 45,
                20,
                90,
                20,
                new LiteralText("Blocks"),
                click -> this.client.setScreen(new BlocksScreen(this.prevScreen)))
        );
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 45,
                20,
                90,
                20,
                new LiteralText("Toggles"),
                click -> this.client.setScreen(new TogglesScreen(this.prevScreen)))
        );
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
