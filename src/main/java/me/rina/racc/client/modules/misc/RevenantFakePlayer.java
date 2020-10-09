package me.rina.racc.client.modules.misc;

import me.rina.racc.client.RevenantModule;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.ArrayList;
import com.mojang.authlib.GameProfile;

public class RevenantFakePlayer extends RevenantModule {
    public RevenantFakePlayer() {
        super("FakePlayer", "FakePlayer", "creates a motionless fake player", Category.MISC);
    }

    private EntityOtherPlayerMP _fakePlayer;

    @Override
    public void onEnable() {
        super.onEnable();
        _fakePlayer = null;

        if (mc.world == null)
        {
            this.toggle();
            return;
        }

        String s = "linustouchtips24";

        _fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), s));

        mc.world.addEntityToWorld(_fakePlayer.getEntityId(), _fakePlayer);
        _fakePlayer.attemptTeleport(mc.player.posX, mc.player.posY, mc.player.posZ);
    }

    @Override
    public void onDisable() {
        if(!(mc.world == null)) {
            mc.world.removeEntity(_fakePlayer);
        }
    }
}
