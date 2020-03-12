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

public abstract class CastleRoomGeneric extends CastleRoom {
	protected static final int MAX_DECO_ATTEMPTS = 3;
	protected DecorationSelector decoSelector;
	private HashMap<BlockPos, EnumFacing> possibleChestLocs;

	public CastleRoomGeneric(BlockPos startPos, int sideLength, int height, int floor) {
		super(startPos, sideLength, height, floor);
		this.decoSelector = new DecorationSelector(this.random);
		this.possibleChestLocs = new HashMap<>();
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

	private void addChests(World world, CastleDungeon dungeon) {
		if (this.getChestIDs() != null && !this.possibleChestLocs.isEmpty()) {
			if (DungeonGenUtils.PercentageRandom(50, this.random)) {
				IRoomDecor chest = new RoomDecorChest();
				BlockPos pos = (BlockPos) this.possibleChestLocs.keySet().toArray()[this.random.nextInt(this.possibleChestLocs.size())];
				chest.build(world, this, dungeon, pos, this.possibleChestLocs.get(pos), this.usedDecoPositions);
			}
		}
	}

	private void addEdgeDecoration(World world, CastleDungeon dungeon) {
		if (this.decoSelector.edgeDecorRegistered()) {
			for (EnumFacing side : EnumFacing.HORIZONTALS) {
				if (this.hasWallOnSide(side) || this.adjacentRoomHasWall(side)) {
					ArrayList<BlockPos> edge = this.getDecorationEdge(side);
					for (BlockPos pos : edge) {
						if (this.usedDecoPositions.contains(pos)) {
							// This position is already decorated, so keep going
							continue;
						}

						int attempts = 0;

						while (attempts < MAX_DECO_ATTEMPTS) {
							IRoomDecor decor = this.decoSelector.randomEdgeDecor();
							if (decor.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) {
								decor.build(world, this, dungeon, pos, side, this.usedDecoPositions);

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
							this.usedDecoPositions.add(pos);
						}
					}
				}
			}
		}
	}

	private void addMidDecoration(World world, CastleDungeon dungeon) {
		if (this.decoSelector.midDecorRegistered()) {
			ArrayList<BlockPos> area = this.getDecorationLayer(0);
			for (BlockPos pos : area) {
				if (this.usedDecoPositions.contains(pos)) {
					// This position is already decorated, so keep going
					continue;
				}

				int attempts = 0;

				while (attempts < MAX_DECO_ATTEMPTS) {
					IRoomDecor decor = this.decoSelector.randomMidDecor();
					EnumFacing side = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
					if (decor.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) {
						decor.build(world, this, dungeon, pos, side, this.usedDecoPositions);

						break;
					}
					++attempts;
				}
				if (attempts >= MAX_DECO_ATTEMPTS) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					this.usedDecoPositions.add(pos);
				}
			}
		}
	}

	private void addPaintings(World world) {
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			if (this.hasWallOnSide(side) || this.adjacentRoomHasWall(side)) {
				ArrayList<BlockPos> edge = this.getPaintingEdge(side);
				for (BlockPos pos : edge) {
					if (this.usedDecoPositions.contains(pos)) {
						// This position is already decorated, so keep going
						continue;
					}

					if ((RoomDecorTypes.PAINTING.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) &&
							(DungeonGenUtils.PercentageRandom(15, this.random))) {
						RoomDecorTypes.PAINTING.buildRandom(world, pos, side, this.possibleDecoPositions, this.usedDecoPositions);
					}
				}
			}
		}
	}

	private void addSpawners(World world, CastleDungeon dungeon, CastleGearedMobFactory mobFactory) {
		ArrayList<BlockPos> spawnPositions = this.getDecorationLayer(0);
		spawnPositions.removeAll(this.usedDecoPositions);

		int spawnerCount = dungeon.randomizeRoomSpawnerCount();

		for (int i = 0; (i < spawnerCount && !spawnPositions.isEmpty()); i++) {
			BlockPos pos = spawnPositions.get(this.random.nextInt(spawnPositions.size()));
			
			Entity mobEntity = mobFactory.getGearedEntityByFloor(this.floor, world);

			SpawnerFactory.placeSpawner(new Entity[] { mobEntity }, false, null, world, pos);
			this.usedDecoPositions.add(pos);
			spawnPositions.remove(pos);
		}
	}

	private void fillEmptySpaceWithAir(World world) {
		HashSet<BlockPos> emptySpaces = new HashSet<>(this.possibleDecoPositions);
		emptySpaces.removeAll(this.usedDecoPositions);

		for (BlockPos emptyPos : emptySpaces) {
			world.setBlockState(emptyPos, Blocks.AIR.getDefaultState());
		}
	}
}
