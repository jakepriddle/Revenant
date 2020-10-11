package me.rina.racc.client.modules.movement;

// Revenant Module.
import me.rina.racc.client.RevenantModule;

// Minecraft.
import net.minecraft.client.entity.EntityPlayerSP;

public class RevenantReverseStep extends RevenantModule
{
    public RevenantReverseStep() {
        super("ReverseStep", "ReverseStep", "Pulls you down quicker", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (RevenantReverseStep.mc.player == null || RevenantReverseStep.mc.world == null || RevenantReverseStep.mc.player.isInWater() || RevenantReverseStep.mc.player.isInLava()) {
            return;
        }
        if (RevenantReverseStep.mc.player.onGround) {
            final EntityPlayerSP player = RevenantReverseStep.mc.player;
            --player.motionY;
        }
    }
}
