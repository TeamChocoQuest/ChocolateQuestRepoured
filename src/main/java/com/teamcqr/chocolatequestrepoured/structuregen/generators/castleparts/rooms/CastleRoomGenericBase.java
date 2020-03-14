package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.*;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects.RoomDecorChest;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects.RoomDecorNone;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        this.addPaintings(world);
        this.addMidDecoration(world, dungeon);
        this.addSpawners(world, dungeon, mobFactory);
        this.addChests(world, dungeon);
        this.fillEmptySpaceWithAir(world);
    }
}
