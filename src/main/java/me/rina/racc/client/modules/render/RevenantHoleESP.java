package me.rina.racc.client.modules.render;

import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import me.rina.racc.event.network.RevenantEventPacket;
import me.rina.racc.event.render.RevenantEventRender3D;
import me.rina.racc.util.entity.RevenantEntityInterpolizerUtil;
import me.rina.racc.util.render.RevenantTessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static me.rina.racc.client.modules.render.RevenantHoleESP.Mode.FlatWireFrame;
import static me.rina.racc.client.modules.render.RevenantHoleESP.Mode.WireFrame;


public class RevenantHoleESP extends RevenantModule {
    public RevenantHoleESP() {
        super("HoleESP", "HoleESP", "Renders safe holes to stand in while crystalling", Category.RENDER);
    }

    private RevenantSetting range = newSetting(new String[] {"Range", "Range", "Range"}, 7, 0, 15);
    private RevenantSetting obbyRed = newSetting(new String[] {"Obsidian Red", "Obsidian Red", "Color red."}, 255, 0, 255);
    private RevenantSetting obbyGreen = newSetting(new String[] {"Obsidian Green", "Obsidian Green", "Color green."}, 0, 0, 255);
    private RevenantSetting obbyBlue = newSetting(new String[] {"Obsidian Blue", "Obsidian Blue", "Color blue."}, 255, 0, 255);
    private RevenantSetting bedrockRed = newSetting(new String[] {"Bedrock Red", "Bedrock Red", "Color red."}, 255, 0, 255);
    private RevenantSetting bedrockGreen = newSetting(new String[] {"Bedrock Green", "Bedrock Green", "Color green."}, 0, 0, 255);
    private RevenantSetting bedrockBlue = newSetting(new String[] {"Bedrock Blue", "Bedrock Blue", "Color blue."}, 255, 0, 255);
    private RevenantSetting mode   = newSetting(new String[] {"Mode", "ModuleMode", "Mode"}, Mode.FlatWireFrame);

    private BlockPos render;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    @Listener
    public void listener(RevenantEventPacket event) {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer) packet).yaw = (float) yaw;
            ((CPacketPlayer) packet).pitch = (float) pitch;
        }
    }

    @Override
    public void onUpdate() {
        BlockPos blockPos;
        List<BlockPos> bRockHoles = this.findBRockHoles();
        List<BlockPos> obbyHoles = this.findObbyHoles();
        BlockPos shouldRender = null;
        Iterator<BlockPos> iterator = bRockHoles.iterator();
        while (iterator.hasNext()) {
            shouldRender = blockPos = iterator.next();
        }
        iterator = obbyHoles.iterator();
        while (iterator.hasNext()) {
            shouldRender = blockPos = iterator.next();
        }
        this.render = shouldRender;
    }

    @Override
    public void onRender3D(RevenantEventRender3D event) {
        if (this.render != null) {
            for (BlockPos holepos : this.findObbyHoles()) {
                if (mode.getEnum() == WireFrame) {
                    RevenantTessellator.prepare(7);
                    RevenantTessellator.drawBoundingBoxBlockPos(holepos, 1.5f, new Color(obbyRed.getFloat(), obbyGreen.getFloat(), obbyBlue.getFloat()));
                    RevenantTessellator.release();
                } if (mode.getEnum() == FlatWireFrame) {
                    RevenantTessellator.prepare(7);
                    RevenantTessellator.drawBoundingBoxBottomBlockPos(holepos, 1.5f, obbyRed.getInteger(), obbyGreen.getInteger(), obbyBlue.getInteger(), 255);
                    RevenantTessellator.release();
                }
            }
            for (BlockPos holepos : this.findBRockHoles()) {
                if (mode.getEnum() == WireFrame) {
                    RevenantTessellator.prepare(7);
                    RevenantTessellator.drawBoundingBoxBlockPos(holepos, 1.5f, new Color(obbyRed.getFloat(), obbyGreen.getFloat(), obbyBlue.getFloat()));
                    RevenantTessellator.release();
                } if (mode.getEnum() == FlatWireFrame) {
                    RevenantTessellator.prepare(7);
                    RevenantTessellator.drawBoundingBoxBottomBlockPos(holepos, 1.5f, bedrockRed.getInteger(), bedrockGreen.getInteger(), bedrockBlue.getInteger(), 255);
                    RevenantTessellator.release();
                }
            }
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = RevenantEntityInterpolizerUtil.calculateLookAt(px, py, pz, me);
        RevenantHoleESP.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean IsObbyHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        return !(RevenantHoleESP.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || this.IsBRockHole(blockPos) || RevenantHoleESP.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR || RevenantHoleESP.mc.world.getBlockState(boost7).getBlock() != Blocks.AIR || RevenantHoleESP.mc.world.getBlockState(boost3).getBlock() != Blocks.OBSIDIAN && RevenantHoleESP.mc.world.getBlockState(boost3).getBlock() != Blocks.BEDROCK || RevenantHoleESP.mc.world.getBlockState(boost4).getBlock() != Blocks.OBSIDIAN && RevenantHoleESP.mc.world.getBlockState(boost4).getBlock() != Blocks.BEDROCK || RevenantHoleESP.mc.world.getBlockState(boost5).getBlock() != Blocks.OBSIDIAN && RevenantHoleESP.mc.world.getBlockState(boost5).getBlock() != Blocks.BEDROCK || RevenantHoleESP.mc.world.getBlockState(boost6).getBlock() != Blocks.OBSIDIAN && RevenantHoleESP.mc.world.getBlockState(boost6).getBlock() != Blocks.BEDROCK || RevenantHoleESP.mc.world.getBlockState(boost8).getBlock() != Blocks.AIR || RevenantHoleESP.mc.world.getBlockState(boost9).getBlock() != Blocks.OBSIDIAN && RevenantHoleESP.mc.world.getBlockState(boost9).getBlock() != Blocks.BEDROCK);
    }

    private boolean IsBRockHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.add(0, -1, 0);

        return RevenantHoleESP.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && RevenantHoleESP.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && RevenantHoleESP.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && RevenantHoleESP.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK && RevenantHoleESP.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK && RevenantHoleESP.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK && RevenantHoleESP.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK && RevenantHoleESP.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && RevenantHoleESP.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(RevenantHoleESP.mc.player.posX), Math.floor(RevenantHoleESP.mc.player.posY), Math.floor(RevenantHoleESP.mc.player.posZ));
    }

    private List<BlockPos> findObbyHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(RevenantHoleESP.getPlayerPos(), range.getInteger(), range.getInteger(), false, true, 0).stream().filter(this::IsObbyHole).collect(Collectors.toList()));
        return positions;
    }

    private List<BlockPos> findBRockHoles() {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(RevenantHoleESP.getPlayerPos(), range.getInteger(), range.getInteger(), false, true, 0).stream().filter(this::IsBRockHole).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                do {
                    float f = sphere ? (float)cy + r : (float)(cy + h);
                    if (!((float)y < f)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                } while (true);
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = RevenantHoleESP.mc.player.rotationYaw;
            pitch = RevenantHoleESP.mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onDisable() {
        this.render = null;
        RevenantHoleESP.resetRotation();
    }

    public enum Mode {
        FlatWireFrame,
        WireFrame;
    }
}

