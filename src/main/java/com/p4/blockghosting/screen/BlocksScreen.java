package com.p4.blockghosting.screen;

import com.p4.blockghosting.screen.widget.ScrollMenu;
import com.p4.blockghosting.screen.widget.ScrollableButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;

public class BlocksScreen extends BaseBlockGhostingScreen {
    private final Screen prevScreen;
    private TextFieldWidget textField;
    private ScrollMenu listWidget;

    public BlocksScreen(Screen prevScreen) {
        super(prevScreen);
        this.prevScreen = prevScreen;
    }

    @Override
    public void addWidgets() {
        /*listSection = new ButtonListWidget(MinecraftClient.getInstance(), width,
                height,
                50,
                height - 50,
                25
        );
        this.addDrawableChild(listSection);*/
        listWidget = new ScrollMenu();

        this.addDrawableChild(new ButtonWidget(
                this.width / 2,
                10,
                0,
                0,
                new LiteralText("v"),
                click -> {}));
        this.addDrawableChild(textField = new TextFieldWidget(
                textRenderer,
                140,
                this.height - 40,
                this.width - 316,
                20,
                new LiteralText(""))
        ).setMaxLength(256);
        this.addDrawableChild(new ButtonWidget(
                this.width - 170,
                this.height - 40,
                30,
                20,
                new LiteralText("Add"), b -> {
                    //blockList.add(blockToAdd);
                    textField.setText("");
                }
        ));
        this.addDrawableChild(new ScrollableButton(
                this.width - 130,
                this.height - 40,
                90,
                20,
                new LiteralText("Remove Selected"),
                b -> {},//blockList.remove(listGui.selected)
                listWidget
        ));
    }
}