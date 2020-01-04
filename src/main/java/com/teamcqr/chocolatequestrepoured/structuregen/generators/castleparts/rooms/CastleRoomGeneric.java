package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.DecorationSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorChest;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorNone;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CastleRoomGeneric extends CastleRoom {
	protected static final int MAX_DECO_ATTEMPTS = 3;
	protected DecorationSelector decoSelector;
	private HashMap<BlockPos, EnumFacing> possibleChestLocs;

	public CastleRoomGeneric(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.decoSelector = new DecorationSelector(this.random);
		this.possibleChestLocs = new HashMap<>();
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		this.setupDecoration(world);
	}

	@Override
	public void decorate(World world, CastleDungeon dungeon) {
		this.addEdgeDecoration(world, dungeon);
		this.addSpawners(world, dungeon);
		this.addChests(world, dungeon);
		this.fillEmptySpaceWithAir(world, dungeon);
	}

	private void addChests(World world, CastleDungeon dungeon) {
		if (this.getChestIDs() != null && !this.possibleChestLocs.isEmpty()) {
			if (DungeonGenUtils.PercentageRandom(50, this.random)) {
				IRoomDecor chest = new RoomDecorChest();
				BlockPos pos = (BlockPos) this.possibleChestLocs.keySet().toArray()[this.random.nextInt(this.possibleChestLocs.size())];
				chest.build(world, this, dungeon, pos, this.possibleChestLocs.get(pos), this.decoMap);
			}
		}
	}

	private void addEdgeDecoration(World world, CastleDungeon dungeon) {
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			if (this.hasWallOnSide(side) || this.adjacentRoomHasWall(side)) {
				ArrayList<BlockPos> edge = this.getDecorationEdge(side);
				for (BlockPos pos : edge) {
					if (this.decoMap.contains(pos)) {
						// This position is already decorated, so keep going
						continue;
					}

					int attempts = 0;

					while (attempts < MAX_DECO_ATTEMPTS) {
						IRoomDecor decor = this.decoSelector.randomEdgeDecor();
						if (decor.wouldFit(pos, side, this.decoArea, this.decoMap)) {
							decor.build(world, this, dungeon, pos, side, this.decoMap);

							// If we added air here then this is a candidate spot for a chest
							if (decor instanceof RoomDecorNone) {
								this.possibleChestLocs.put(pos, side);
							}
							break;
						}
						++attempts;
					}
					if (attempts >= MAX_DECO_ATTEMPTS) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						this.decoMap.add(pos);
					}
				}
			}
		}
	}

	private void addSpawners(World world, CastleDungeon dungeon) {
		ArrayList<BlockPos> spawnPositions = this.getDecorationFirstLayer();
		spawnPositions.removeAll(this.decoMap);

		int spawnerCount = this.getSpawnerCount();

		for (int i = 0; (i < spawnerCount && !spawnPositions.isEmpty()); i++) {
			BlockPos pos = spawnPositions.get(this.random.nextInt(spawnPositions.size()));

			ResourceLocation resLoc;
			if (dungeon.getDungeonMob() == EDungeonMobType.DEFAULT) {
				resLoc = EDungeonMobType.getMobTypeDependingOnDistance(pos.getX(), pos.getZ()).getEntityResourceLocation();
			} else {
				resLoc = dungeon.getDungeonMob().getEntityResourceLocation();
			}
			Entity mobEntity = EntityList.createEntityByIDFromName(resLoc, world);

			SpawnerFactory.placeSpawner(new Entity[] { mobEntity }, false, null, world, pos);
			this.decoMap.add(pos);
			spawnPositions.remove(pos);
		}
	}

	private void fillEmptySpaceWithAir(World world, CastleDungeon dungeon) {
		HashSet<BlockPos> emptySpaces = new HashSet<>(this.decoArea);
		emptySpaces.removeAll(this.decoMap);

		for (BlockPos emptyPos : emptySpaces) {
			world.setBlockState(emptyPos, Blocks.AIR.getDefaultState());
		}
	}
}
