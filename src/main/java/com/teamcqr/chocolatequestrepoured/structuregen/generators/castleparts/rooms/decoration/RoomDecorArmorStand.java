package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class RoomDecorArmorStand extends RoomDecorEntity
{
    public RoomDecorArmorStand()
    {
        super();
        this.footprint.add(new Vec3i(0, 0, 0));
        this.footprint.add(new Vec3i(0, 1, 0));
    }

    @Override
    protected void spawnEntity(World world, BlockPos pos)
    {
        EntityArmorStand stand = new EntityArmorStand(world);
        stand.posX = pos.getX();
        stand.posY = pos.getY();
        stand.posZ = pos.getZ();

        world.spawnEntity(stand);
    }
}
