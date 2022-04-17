package com.p4.blockghosting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;

public class ChatLib {
    private static final ChatHud chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();
    public static void chat(LiteralText text) {
        chatHud.addMessage(text);
    }
}
