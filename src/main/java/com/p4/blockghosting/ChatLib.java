package com.p4.blockghosting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

public class ChatLib {
    private static final ChatHud chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();
    public static void chat(Text text) {
        chatHud.addMessage(text);
    }
}
