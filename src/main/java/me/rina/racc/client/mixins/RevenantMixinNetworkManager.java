package me.rina.racc.client.mixins;

// Minecraft.
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

// IO.
import io.netty.channel.ChannelHandlerContext;

// Mixin.
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

// Event.
import me.rina.racc.event.network.RevenantEventPacket;
import me.rina.racc.event.RevenantEvent;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 25/09/2020.
 *
 **/
@Mixin(value = NetworkManager.class, priority = 9998)
public abstract class RevenantMixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo callback) {
        RevenantEventPacket event = new RevenantEventPacket.Send(RevenantEvent.Stage.PRE, packet);

        Revenant.getInstance().revEventManager.dispatch(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void receivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        RevenantEventPacket event = new RevenantEventPacket.Receive(RevenantEvent.Stage.POST, packet);

        Revenant.getInstance().revEventManager.dispatch(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}