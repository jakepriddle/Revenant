package me.rina.racc.client.modules.client;

// Minecraft.
import me.rina.racc.event.Handler;
import net.minecraft.network.play.client.CPacketCloseWindow;

// Pomelo.
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

// Setting.
import me.rina.racc.client.RevenantSetting;

// Module.
import me.rina.racc.client.RevenantModule;

// Event.
import me.rina.racc.event.network.RevenantEventPacket;

// Util.
import me.rina.racc.util.math.RevenantMathPlayerUtil;
import me.rina.racc.util.math.RevenantMathTimerUtil;
import me.rina.racc.util.client.RevenantChatUtil;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina.
 * @since 25/09/2020.
 *
 **/
public class RevenantTestModule extends RevenantModule {
    private RevenantSetting slider = newSetting(new String[] {"Slider", "ModuleTestSlider", "Test no."}, 2, 0, 10);
    private RevenantSetting mode   = newSetting(new String[] {"Mode", "ModuleTestMode", "Test no??."}, Mode.RELOAD);
    private RevenantSetting xcare  = newSetting(new String[] {"XCarry", "ModuleTestXCarry", "bro xcry."}, false);

    private RevenantMathTimerUtil clock = new RevenantMathTimerUtil(-1);

    public enum Mode {
        RELOAD,
        UNLOADED
    }

    public RevenantTestModule() {
        super("Test Module", "TestModule", "Dev module for test.", Category.CLIENT);
    }

    @Handler
    public void listenPacketReceive(RevenantEventPacket.Send event) {
        if (event.getPacket() instanceof CPacketCloseWindow && xcare.getBoolean()) {
            event.cancel();
        }
    }

    @Override
    public void onUpdate() {
        if (mode.getEnum() == Mode.RELOAD) {
            if (clock.isPassedSI(slider.getInteger())) {
                double[] lastPos = RevenantMathPlayerUtil.getPosition();

                RevenantChatUtil.sendClientMessage(Revenant.CHAT + "Me " + mc.player.getName() + " am in " + lastPos[0] + "x " + lastPos[1] + "y " + lastPos[2] + "z;");

                clock.resetTimer();
            }
        }
    }
}