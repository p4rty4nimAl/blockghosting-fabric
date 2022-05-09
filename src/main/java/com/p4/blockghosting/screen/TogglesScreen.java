package com.p4.blockghosting.screen;

import com.p4.blockghosting.ConfigInit;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class TogglesScreen extends Screen {

    final Screen prevScreen;
    private MatrixStack matrices;
    public TogglesScreen(Screen prevScreen) {
        super(new LiteralText(""));
        this.prevScreen = prevScreen;
    }
    public void init() {
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 135,
                this.height - 40,
                270,
                20,
                new LiteralText("Back"),
                click -> this.client.setScreen(this.prevScreen))
        );
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 135,
                20,
                270,
                20,
                new LiteralText(" << Toggles >> "),
                click -> {}
        ));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 65,
                50,
                50,
                20,
                new LiteralText(ConfigInit.State.modBlockState ? "Enabled" : "Disabled"),
                click -> {
                    ConfigInit.State.modBlockState = !ConfigInit.State.modBlockState;
                    this.clearChildren();
                    this.init();
                }));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 65,
                75,
                50,
                20,
                new LiteralText(ConfigInit.State.modAreaState ? "Enabled" : "Disabled"),
                click -> {
                    ConfigInit.State.modAreaState = !ConfigInit.State.modAreaState;
                    this.clearChildren();
                    this.init();
                }));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 65,
                100,
                50,
                20,
                new LiteralText(ConfigInit.State.breakCane ? "Enabled" : "Disabled"),
                click -> {
                    ConfigInit.State.breakCane = !ConfigInit.State.breakCane;
                    this.clearChildren();
                    this.init();
                }));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 65,
                125,
                50,
                20,
                new LiteralText(ConfigInit.State.breakBamboo ? "Enabled" : "Disabled"),
                click -> {
                    ConfigInit.State.breakBamboo = !ConfigInit.State.breakBamboo;
                    this.clearChildren();
                    this.init();
                }));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 65,
                150,
                50,
                20,
                new LiteralText(ConfigInit.State.farmlandRender ? "Enabled" : "Disabled"),
                click -> {
                    ConfigInit.State.farmlandRender = !ConfigInit.State.farmlandRender;
                    this.clearChildren();
                    this.init();
                }));
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawStringWithShadow(matrices, this.textRenderer, "Block toggle:", this.width / 2 - 115, 60, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, "Area toggle:", this.width / 2 - 115, 85, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, "Break sugarcane toggle:", this.width / 2 - 115, 110, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, "Break bamboo toggle:", this.width / 2 - 115, 135, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, "Farmland rendering toggle:", this.width / 2 - 115, 160, 0xFFFFFF);

    }
}
