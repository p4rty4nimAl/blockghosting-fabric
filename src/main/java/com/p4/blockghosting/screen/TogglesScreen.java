package com.p4.blockghosting.screen;

import com.p4.blockghosting.ConfigInit;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TogglesScreen extends Screen {

    final Screen prevScreen;
    public TogglesScreen(Screen prevScreen) {
        super(Text.of(""));
        this.prevScreen = prevScreen;
    }
    public void init() {
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 135,
                this.height - 40,
                270,
                20,
                Text.translatable("blockghosting.screen.back"),
                click -> this.client.setScreen(this.prevScreen))
        );
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 135,
                20,
                270,
                20,
                Text.translatable("blockghosting.toggle.header"),
                click -> {}
        ));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 65,
                50,
                50,
                20,
                Text.translatable(ConfigInit.State.modBlockState ? "blockghosting.toggle.enabled" : "blockghosting.toggle.disabled"),
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
                Text.translatable(ConfigInit.State.modAreaState ? "blockghosting.toggle.enabled" : "blockghosting.toggle.disabled"),
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
                Text.translatable(ConfigInit.State.breakCane ? "blockghosting.toggle.enabled" : "blockghosting.toggle.disabled"),
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
                Text.translatable(ConfigInit.State.breakBamboo ? "blockghosting.toggle.enabled" : "blockghosting.toggle.disabled"),
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
                Text.translatable(ConfigInit.State.farmlandRender ? "blockghosting.toggle.enabled" : "blockghosting.toggle.disabled"),
                click -> {
                    ConfigInit.State.farmlandRender = !ConfigInit.State.farmlandRender;
                    this.clearChildren();
                    this.init();
                }));
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("blockghosting.toggle.block").getString(), this.width / 2 - 115, 60, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("blockghosting.toggle.area").getString(), this.width / 2 - 115, 85, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("blockghosting.toggle.sugarcane").getString(), this.width / 2 - 115, 110, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("blockghosting.toggle.bamboo").getString(), this.width / 2 - 115, 135, 0xFFFFFF);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("blockghosting.toggle.render.farmland").getString(), this.width / 2 - 115, 160, 0xFFFFFF);

    }
}
