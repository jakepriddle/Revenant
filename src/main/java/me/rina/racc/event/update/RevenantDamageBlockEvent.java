package me.rina.racc.event.update;

// Revenant Event.
import me.rina.racc.event.RevenantEvent;

// Minecraft.
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class RevenantDamageBlockEvent extends RevenantEvent
{
    private BlockPos pos;
    private EnumFacing face;

    public RevenantDamageBlockEvent(BlockPos pos, EnumFacing face){
        this.pos = pos;
        this.face = face;
    }

    public BlockPos getPos(){
        return pos;
    }

    public void setPos(BlockPos pos){
        this.pos = pos;
    }

    public EnumFacing getFace(){
        return face;
    }

    public void setFace(EnumFacing face){
        this.face = face;
    }
}
