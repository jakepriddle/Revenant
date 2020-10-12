package me.rina.racc.client.mixins;

import me.rina.racc.Revenant;
import me.rina.racc.event.update.RevenantDamageBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class RevenantMixinPlayerControllerMP {
    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        RevenantDamageBlockEvent event = new RevenantDamageBlockEvent(posBlock, directionFacing);
        Revenant.getInstance().revEventManager.dispatch(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}
