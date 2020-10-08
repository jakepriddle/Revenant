package me.rina.racc.util.entity;

// Minecraft.
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

// Java.
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Revenant.
import me.rina.racc.Revenant;

/**
 *
 * @author Rina!
 *
 * Created by Rina!
 * 17/08/2020.
 *
 **/
public class RevenantEntityInterpolizerUtil {
    public static Vec3d getLastTickPos(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static Vec3d getInterpolatedPos(Entity entity, double ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getLastTickPos(entity, ticks, ticks, ticks));
    }

    public static EntityPlayerSP getEntityPlayerSPOnChunkByName(String name) {
        for (Entity entity : Revenant.getMinecraft().world.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase && entity instanceof EntityPlayerSP)) {
                continue;
            }

            EntityPlayerSP entityPlayerSP = (EntityPlayerSP) entity;

            return entityPlayerSP;
        }

        return null;
    }

    public static double getInterpolated(double now, double then) {
        return then + (now - then) * Revenant.getMinecraft().getRenderPartialTicks();
    }

    public static double[] getInterpolated(Entity entity) {
        double x = getInterpolated(entity.posX, entity.lastTickPosX) - Revenant.getMinecraft().getRenderManager().renderPosX;
        double y = getInterpolated(entity.posY, entity.lastTickPosY) - Revenant.getMinecraft().getRenderManager().renderPosY;
        double z = getInterpolated(entity.posZ, entity.lastTickPosZ) - Revenant.getMinecraft().getRenderManager().renderPosZ;

        return new double[] {
                x, y, z
        };
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;

        yaw += 90f;

        return new double[]{yaw, pitch};
    }
}