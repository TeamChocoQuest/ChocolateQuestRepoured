package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CastleRoomGenericBase extends CastleRoomDecoratedBase {
	public CastleRoomGenericBase(BlockPos startPos, int sideLength, int height, int floor) {
		super(startPos, sideLength, height, floor);
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {

	}

	@Override
	public void decorate(World world, CastleDungeon dungeon, CastleGearedMobFactory mobFactory) {
        this.addEdgeDecoration(world, dungeon);
        this.addWallDecoration(world, dungeon);
        this.addMidDecoration(world, dungeon);
        this.addSpawners(world, dungeon, mobFactory);
        this.addChests(world, dungeon);
        this.fillEmptySpaceWithAir(world);
    }
}
