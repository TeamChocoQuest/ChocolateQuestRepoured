package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HookShotPullPacket implements IMessage {

    private boolean pulling;
    private double velocity;
    private Vec3d impactLocation;

    //Must have default constructor or else netty throws an exception
    public HookShotPullPacket () {

    }

    public HookShotPullPacket (boolean pulling, double velocity, Vec3d impactLocation) {
        this.pulling = pulling;
        this.velocity = velocity;
        this.impactLocation = new Vec3d(impactLocation.x, impactLocation.y, impactLocation.z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pulling = buf.readBoolean();
        velocity = buf.readDouble();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        impactLocation = new Vec3d(x, y, z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(pulling);
        buf.writeDouble(velocity);
        buf.writeDouble(impactLocation.x);
        buf.writeDouble(impactLocation.y);
        buf.writeDouble(impactLocation.z);
    }

    public boolean isPulling() {
        return pulling;
    }

    public double getVelocity() {
        return velocity;
    }

    public Vec3d getImpactLocation() {
        return impactLocation;
    }
}
