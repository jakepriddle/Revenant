package me.rina.racc.client.modules.combat;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.util.client.RevenantChatUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class RevenantBurrow extends RevenantModule {
    public RevenantBurrow() {
        super("Burrow", "Burrow", "Burrows you in a obi block", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        if (!mc.player.onGround) {
            RevenantChatUtil.sendClientMessage("Burrow: You are not on ground, disabling!");
        }

        int obiSlot = findObsidianSlot();

        if (obiSlot == -1) {
            RevenantChatUtil.sendClientMessage("Burrow: No Obsidian in Hotbar, disabling!");
            setDisabled();
        }

        BlockPos toPutBlock = new BlockPos(mc.player.posX, mc.player.posY-1, mc.player.posZ);

        mc.player.inventory.currentItem = obiSlot - 36;

        mc.player.posX += 4;
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY+4, mc.player.posZ, true));

        mc.player.connection.sendPacket(
                new CPacketPlayerTryUseItemOnBlock(
                        toPutBlock,
                        EnumFacing.UP,
                        EnumHand.MAIN_HAND,
                        (float) mc.player.posX,
                        (float) mc.player.posY,
                        (float) mc.player.posZ
                )
        );
        mc.player.swingArm(EnumHand.MAIN_HAND);

        RevenantChatUtil.sendClientMessage("Burrow: You should now be inside an obsidian block, disabling!");
        setDisabled();
    }

    private int findObsidianSlot() {
        for (int i = 0; i < 9; i++) {
            if (!(mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)) continue;
            if (((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock() instanceof BlockObsidian) return i < 9 ? i + 36 : i;
        }

        return -1;
    }
}
