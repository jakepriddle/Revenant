package me.rina.racc.client.modules.player;

// Revenant Module.
import me.rina.racc.client.RevenantModule;

// Minecraft.
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;

public class RevenantMultiTask extends RevenantModule
{
    public RevenantMultiTask(){
        super("MultiTask", "MultiTask", "MultiTask", Category.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (RevenantMultiTask.mc.gameSettings.keyBindUseItem.isKeyDown() && RevenantMultiTask.mc.player.getActiveHand() == EnumHand.MAIN_HAND) {
            if (!(RevenantMultiTask.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock) && RevenantMultiTask.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (RevenantMultiTask.mc.player.getHeldItemOffhand().getItem() instanceof ItemFood && RevenantMultiTask.mc.gameSettings.keyBindUseItem.isKeyDown() && RevenantMultiTask.mc.gameSettings.keyBindAttack.isKeyDown()) {
                    RevenantMultiTask.mc.player.setActiveHand(EnumHand.OFF_HAND);
                    final RayTraceResult r = RevenantMultiTask.mc.player.rayTrace(6.0, RevenantMultiTask.mc.getRenderPartialTicks());
                    RevenantMultiTask.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else {
                final RayTraceResult r = RevenantMultiTask.mc.player.rayTrace(6.0, RevenantMultiTask.mc.getRenderPartialTicks());
                RevenantMultiTask.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(r.getBlockPos(), r.sideHit, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
            }
        }
    }
}
