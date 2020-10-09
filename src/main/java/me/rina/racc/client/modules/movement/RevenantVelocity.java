package me.rina.racc.client.modules.movement;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.event.network.RevenantEventPacket;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * linus module
 **/

public class RevenantVelocity extends RevenantModule {
    public RevenantVelocity() {
        super("Velocity", "Velocity", "Modifies player velocity", Category.MOVEMENT);
    }

    @Listener
    public void listener(RevenantEventPacket.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId())
                event.cancel();
        }

        if (event.getPacket() instanceof SPacketExplosion) {
            event.cancel();
        }
    }

    @Override
    public String getHUDInfo(){
        return "H:0% V:0%";
    }
}

