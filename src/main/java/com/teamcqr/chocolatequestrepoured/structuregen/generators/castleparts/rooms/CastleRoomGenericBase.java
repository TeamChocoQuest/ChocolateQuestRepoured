package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CastleRoomGenericBase extends CastleRoomDecoratedBase {
	public CastleRoomGenericBase(BlockPos startOffset, int sideLength, int height, int floor) {
		super(startOffset, sideLength, height, floor);
	}

	@Override
	public void generateRoom(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {

	}

	@Override
	public void decorate(World world, BlockStateGenArray genArray, CastleDungeon dungeon, CastleGearedMobFactory mobFactory) {
        this.addEdgeDecoration(world, genArray, dungeon);
        this.addWallDecoration(world, genArray, dungeon);
        this.addMidDecoration(world, genArray, dungeon);
        this.addSpawners(world, genArray, dungeon, mobFactory);
        this.addChests(world, genArray, dungeon);
        this.fillEmptySpaceWithAir(genArray);
    }
}
