package me.rina.racc.client.modules.combat;

import me.rina.racc.Revenant;
import me.rina.racc.client.RevenantModule;
import me.rina.racc.client.RevenantSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Hoosiers on 10/07/2020
 *
 * @LastUpdate on 10/08/2020
 */

public class RevenantAutoCrystal extends RevenantModule {

    private RevenantSetting crystalLogic = newSetting(new String[]{"Logic", "ACLogic", "Logic"}, CrystalLogic.BREAKPLACE);
    private RevenantSetting breakCrystal = newSetting(new String[]{"Break", "ACBreak", "Break"}, true);
    private RevenantSetting handLogic = newSetting(new String[]{"Hand", "ACHand", "Hand"}, HandLogic.MAINHAND);
    private RevenantSetting desyncBreak = newSetting(new String[]{"DesyncBreak", "ACDesynchBreak", "DesyncBreak"}, false);
    private RevenantSetting attackSpeed = newSetting(new String[]{"AttackSpeed", "ACAttackSpeed", "AttackSpeed"}, 12, 0, 20);
    private RevenantSetting breakRange = newSetting(new String[]{"BreakRange", "ACBreakRange", "BreakRange"}, 4.4, 0.0, 10.0);
    private RevenantSetting wallsRange = newSetting(new String[]{"WallsRange", "ACWallsRange", "WallsRange"}, 3.5, 0.0, 5.0);
    private RevenantSetting antiWeakness = newSetting(new String[]{"AntiWeakness", "ACAntiWeakness", "AntiWeakness"}, true);
    private RevenantSetting placeLogic = newSetting(new String[]{"PlaceLogic", "ACPlaceLogic", "PlaceLogic"}, PlaceLogic.DEFAULT);
    private RevenantSetting placeCrystal = newSetting(new String[]{"Place", "ACPlace", "Place"}, true);
    private RevenantSetting placeRange = newSetting(new String[]{"PlaceRange", "ACPlaceRange", "PlaceRange"}, 4.4, 0.0, 10.0);
    private RevenantSetting placeDelay = newSetting(new String[]{"PlaceDelay", "ACPlaceDelay", "PlaceDelay"}, 1, 0, 10);
    private RevenantSetting enemyRange = newSetting(new String[]{"EnemyRange", "ACEnemyRange", "EnemyRange"}, 6.0, 0.0, 20.0);
    private RevenantSetting antiSuicide = newSetting(new String[]{"AntiSuicide", "ACAntiSuicide", "AntiSuicide"}, true);
    private RevenantSetting antiSuicideHealth = newSetting(new String[]{"SuicideHealth", "ACSuicideHealth", "SuicideHealth"}, 10, 0, 36);
    private RevenantSetting rotate = newSetting(new String[]{"Rotate", "ACRotate", "Rotate"}, true);
    private RevenantSetting raytrace = newSetting(new String[]{"Raytrace", "ACRaytrace", "Raytrace"}, false);

    public enum CrystalLogic{
        BREAKPLACE,
        PLACEBREAK
    }

    public enum HandLogic{
        MAINHAND,
        OFFHAND
    }

    public enum PlaceLogic{
        DEFAULT,
        THIRTEEN
    }

    public RevenantAutoCrystal(){
        super("AutoCrystal", "AutoCrystal", "Breaks and places end crystals", Category.COMBAT);
    }

    private boolean isBreaking = false;
    private boolean isPlacing = false;
    private boolean isActive = false;
    private boolean isWeaknessSwitched = false;
    private long breakSystemTime;
    private int placeDelayInt = 0;

    public void onUpdate(){
        isActive = true;

        if (mc.player == null || mc.player.isDead){
            setDisabled();
            return;
        }

        //anti suicide
        if (antiSuicide.getBoolean() && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= antiSuicideHealth.getInteger()){
            return;
        }

        //crystal logic (break/place)
        switch ((CrystalLogic) crystalLogic.getEnum()){
            case BREAKPLACE: {
                breakCrystal();
                placeCrystal();
            }
            case PLACEBREAK: {
                placeCrystal();
                breakCrystal();
            }
        }

        isActive = false;
    }

    private void breakCrystal(){
        if (breakCrystal.getBoolean()) {
            mc.world.loadedEntityList.stream()
                    .filter(crystal -> crystal instanceof EntityEnderCrystal)
                    .filter(crystal -> crystalCheck(crystal))
                    .sorted(Comparator.comparing(crystal -> crystal.getDistance(mc.player))) //might change this
                    .forEach(crystal -> {
                        if (crystal == null) {
                            return;
                        }

                        //anti weakness
                        int oldSlot = -1;
                        int newSlot = -1;
                        if (antiWeakness.getBoolean() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                            if (!isWeaknessSwitched) {
                                oldSlot = mc.player.inventory.currentItem;
                                isWeaknessSwitched = true;
                            }
                            for (int i = 0; i < 9; i++) {
                                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                                if (stack == ItemStack.EMPTY) {
                                    continue;
                                }
                                if ((stack.getItem() instanceof ItemSword)) {
                                    newSlot = i;
                                    break;
                                }
                                if ((stack.getItem() instanceof ItemTool) && !(stack.getItem() instanceof ItemPickaxe)) {
                                    newSlot = i;
                                    break;
                                }
                            }
                            if (newSlot != -1) {
                                mc.player.inventory.currentItem = newSlot;
                            }
                        }
                        if (antiWeakness.getBoolean() && isWeaknessSwitched && !mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                            mc.player.inventory.currentItem = oldSlot;
                            isWeaknessSwitched = false;
                        }

                        //attack speed & break
                        if (System.nanoTime() / 1000000L - breakSystemTime >= 420 - (attackSpeed.getInteger() * 20)) {
                            isBreaking = true;

                            if (rotate.getBoolean()) {
                                //todo: rotate
                            }

                            mc.playerController.attackEntity(mc.player, crystal);
                            switch ((HandLogic) handLogic.getEnum()) {
                                case MAINHAND: {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                }
                                case OFFHAND: {
                                    mc.player.swingArm(EnumHand.OFF_HAND);
                                }
                            }
                            if (desyncBreak.getBoolean()) {
                                crystal.setDead();
                                mc.world.removeAllEntities();
                                mc.world.getLoadedEntityList();
                            }

                            isBreaking = false;
                            breakSystemTime = System.nanoTime() / 1000000L;
                        }
                    });
        }
    }

    private boolean crystalCheck(Entity crystal) {
        if (!(crystal instanceof EntityEnderCrystal)){
            return false;
        }
        //distance
        if (crystal.getDistance(mc.player) > breakRange.getDouble()){
            return false;
        }
        //walls range
        if (!mc.player.canEntityBeSeen(crystal) && crystal.getDistance(mc.player) > wallsRange.getDouble()){
            return false;
        }

        return true;
    }

    private void placeCrystal(){
        if (placeCrystal.getBoolean()){
            mc.world.playerEntities.stream()
                    .filter(entityPlayer -> targetCheck(entityPlayer))
                    .sorted(Comparator.comparing(entityPlayer -> (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount())))
                    .sorted(Comparator.comparing(entityPlayer -> entityPlayer.getDistance(mc.player)))
                    .forEach(entityPlayer -> {

                        BlockPos targetBlock = getBestBlock(entityPlayer);

                        if (targetBlock == null){
                            return;
                        }

                        boolean offhandCheck = false;
                        EnumFacing enumFacing = EnumFacing.NORTH; //todo, north is temp

                        //todo: raytrace, offhand check, and autoswitch

                        if (placeDelayInt <= placeDelay.getInteger()){
                            placeDelayInt = 0;
                            isPlacing = true;

                            if (raytrace.getBoolean()){
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, enumFacing, offhandCheck ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                            }
                            //allows for the CA to place above build height
                            else if (targetBlock.y == 255){
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, EnumFacing.DOWN, offhandCheck ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                            }
                            else {
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, EnumFacing.UP, offhandCheck ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                            }
                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        }
                        placeDelayInt++;
                        isPlacing = false;
            });
        }
    }

    private boolean targetCheck(EntityPlayer target){
        if (!(target instanceof EntityPlayer)){
            return false;
        }
        if (target == mc.player){
            return false;
        }
        if (target.isDead || target.isCreative()){
            return false;
        }
        //friend
        if (Revenant.getSocialUserManager().isFriend(target.getName())){
            return false;
        }
        //health
        if ((target.getHealth() + target.getAbsorptionAmount()) <= 0){
            return false;
        }
        //distance
        if (target.getDistance(mc.player) > enemyRange.getDouble()){
            return false;
        }

        return true;
    }

    private BlockPos getBestBlock(EntityPlayer target){
        BlockPos bestBlockPos = null;
        if (target == null){
            bestBlockPos = null;
        }

        BlockPos targetBlockPos = new BlockPos(target.posX, target.posY, target.posZ);

        List<BlockPos> possibleBlocks = getPossibleBlocks(targetBlockPos);

        for (BlockPos blockPos : possibleBlocks){
            //calculate damage for each
            //total list
            //sort to highest
            //set highest to bestBlockPos
        }

        //todo

        return bestBlockPos;
    }

    private List<BlockPos> getPossibleBlocks(BlockPos targetBlockPos) {
        List<BlockPos> allPos = new ArrayList<>();

        allPos.addAll(getSphere(targetBlockPos, placeRange.getDouble(), placeRange.getDouble(), false, true,0).stream()
                .filter(blockPos -> validPlacement(blockPos)).collect(Collectors.toList()));

        return allPos;
    }

    private List<BlockPos> getSphere(BlockPos inputPos, double radius, double height, boolean hollow, boolean sphere, int yPlus) {
        List<BlockPos> spherePos = new ArrayList<>();
        //todo
        return spherePos;
    }

    private boolean validPlacement(BlockPos blockPos){
        BlockPos upOne = new BlockPos(blockPos.x, blockPos.y + 1, blockPos.z);
        BlockPos upTwo = new BlockPos(blockPos.x, blockPos.y + 2, blockPos.z);
        if (mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK){
            if (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(upOne)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(upTwo)).isEmpty()) {
                switch ((PlaceLogic) placeLogic.getEnum()) {
                    case DEFAULT: {
                        if (mc.world.getBlockState(upOne).getBlock() == Blocks.AIR || mc.world.getBlockState(upOne).getBlock() == Blocks.WEB){
                            if (mc.world.getBlockState(upTwo).getBlock() == Blocks.AIR || mc.world.getBlockState(upTwo).getBlock() == Blocks.WEB){
                                return true;
                            }
                        }
                    }
                    case THIRTEEN: {
                        if (mc.world.getBlockState(upOne).getBlock() == Blocks.AIR || mc.world.getBlockState(upOne).getBlock() == Blocks.WEB){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}