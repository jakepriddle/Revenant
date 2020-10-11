package me.rina.racc.client.modules.player;

// Revenant Module.
import me.rina.racc.client.RevenantModule;

// Revenant Setting.
import me.rina.racc.client.RevenantSetting;

// Minecraft Forge.
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;

// Java.
import java.util.List;
import java.util.ArrayList;

public class RevenantFOV extends RevenantModule
{
    private RevenantSetting fov = newSetting(new String[]{"FOVSlider", "FOVFOVSlider", "FovSlider"}, 120, 0, 180);
    private RevenantSetting modes = newSetting(new String[]{"Mode", "FOVMode", "Mode"}, FOVMode.ViewModelChanger);

    public enum FOVMode{
        ViewModelChanger,
        FOVChanger,
    }

    public float defaultFov;

    public RevenantFOV(){
        super("FOV", "FOV", "FOV", Category.PLAYER);
    }

    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier e) {
        switch ((FOVMode) modes.getEnum())
        {
            case FOVChanger: {
                e.setFOV((float) this.fov.getInteger());
                break;
            }
            default: {
                return;
            }
        }
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.defaultFov = RevenantFOV.mc.gameSettings.fovSetting;
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        RevenantFOV.mc.gameSettings.fovSetting = this.defaultFov;
    }
}
