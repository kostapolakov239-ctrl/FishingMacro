package com.fishingmacro.mixin;

import com.fishingmacro.FishingMacro;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ScreenEventMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	private void onClientTick(CallbackInfo ci) {
		// This is where we could add additional tick logic if needed
	}
}
