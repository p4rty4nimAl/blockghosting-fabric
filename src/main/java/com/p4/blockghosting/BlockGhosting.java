package com.p4.blockghosting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BlockGhosting implements ClientModInitializer {

	public static final Vec3i ySubtraction = new Vec3i(0, 1, 0);
	public static final Logger LOGGER = LoggerFactory.getLogger("blockghosting");
	public static boolean invalidateCache = false;

	@Override
	public void onInitializeClient() {
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			invalidateCache = true;
			return ActionResult.PASS;
		});
		UseBlockCallback.EVENT.register((player, world, hand, pos) -> {
			invalidateCache = true;
			return ActionResult.PASS;
		});
		try {
			if (!ConfigInit.init()) {
				LOGGER.warn("Initialization failed!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
