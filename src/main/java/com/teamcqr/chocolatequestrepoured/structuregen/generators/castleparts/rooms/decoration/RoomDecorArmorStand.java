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
        //Need to add 0.5 to each position amount so it spawns in the middle of the tile
        EntityArmorStand stand = new EntityArmorStand(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        world.spawnEntity(stand);
    }
}
