package com.p4.blockghosting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class ChatLib {
    public static void chat(LiteralText text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }
}
