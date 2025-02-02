package com.aizistral.nochatreports.mixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen {

	@Inject(method = "startConnecting", at = @At("HEAD"))
	private static void onStartConnecting(Screen screen, Minecraft minecraft, ServerAddress serverAddress,
			@Nullable ServerData serverData, CallbackInfo info) {
		ServerSafetyState.updateCurrent(ServerSafetyLevel.UNKNOWN); // just to be 100% sure
		ServerSafetyState.setLastConnectedServer(serverAddress);

		if (NoReportsConfig.isDebugLogEnabled()) {
			NoChatReports.LOGGER.info("Connecting to: {}, will expose public key: {}",
					serverAddress.getHost() + ":" + serverAddress.getPort(),
					ServerSafetyState.allowsUnsafeServer());
		}
	}

}
