package me.rina.racc.client.modules.player;

// Revenantg Module.
import me.rina.racc.client.RevenantModule;

// Revenant Setting.
import me.rina.racc.client.RevenantSetting;

// Minecraft.
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

public class RevenantFastUse extends RevenantModule
{
    public RevenantFastUse() {
        super("FastUse", "FastUse", "FastUse", Category.PLAYER);
    }

    private RevenantSetting exp = newSetting(new String[] {"XP", "FastUseXP", "XP"}, true);
    private RevenantSetting crystal = newSetting(new String[] {"Crystals", "FastUseCrystals", "Crystals"}, true);
    private RevenantSetting bow = newSetting(new String[] {"Fast Bow", "FastUseFastBow", "FastBow"}, true);
    private RevenantSetting blocks = newSetting(new String[] {"Blocks", "FastUseBlocks", "Blocks"}, true);
    private RevenantSetting other = newSetting(new String[] {"Other", "FastUseOther", "Other"}, true);

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            if (this.exp.getBoolean()) {
                mc.rightClickDelayTimer = 0;
            }
        } else if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
            if (this.crystal.getBoolean()) {
                mc.rightClickDelayTimer = 0;
            }
        } else if (Block.getBlockFromItem(mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock()) {
            if (this.blocks.getBoolean()) {
                mc.rightClickDelayTimer = 0;
            } else if (this.other.getBoolean() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
                mc.rightClickDelayTimer = 0;
            }
        } else if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
            if (this.bow.getBoolean()) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
            }
        }
    }
}
