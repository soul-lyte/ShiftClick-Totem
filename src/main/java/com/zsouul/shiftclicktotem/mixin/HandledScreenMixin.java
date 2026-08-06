package com.zsouul.shiftclicktotem.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

	@Shadow
	protected ScreenHandler handler;

	private HandledScreenMixin(Text title) {
		super(title);
	}

	@Inject(
		method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void shiftclicktotem$onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
		if (actionType != SlotActionType.QUICK_MOVE || slot == null || !slot.hasStack()) {
			return;
		}

		if (!slot.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
			return;
		}

		if (this.client == null || this.client.player == null || !this.client.player.getOffHandStack().isEmpty()) {
			return;
		}

		this.client.interactionManager.clickSlot(this.handler.syncId, slot.id, 40, SlotActionType.SWAP, this.client.player);
		ci.cancel();
	}
}
