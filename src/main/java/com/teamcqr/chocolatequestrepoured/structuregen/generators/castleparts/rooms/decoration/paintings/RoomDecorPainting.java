package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.paintings;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;

public class RoomDecorPainting {
	private EnumMap<EntityPainting.EnumArt, ArrayList<Vec3i>> artFootprints = new EnumMap<>(EntityPainting.EnumArt.class);

	public RoomDecorPainting() {
		super();

		for (EntityPainting.EnumArt art : EntityPainting.EnumArt.values()) {
			artFootprints.put(art, getFootPrintFromArtType(art));
		}
	}

	public boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap) {
		return !getArtThatWouldFit(start, side, decoArea, decoMap).isEmpty();
	}

	public void buildRandom(World world, BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap) {
		ArrayList<EntityPainting.EnumArt> artList = getArtThatWouldFit(start, side, decoArea, decoMap);
		if (!artList.isEmpty()) {
			Collections.shuffle(artList);
			this.build(artList.get(0), world, start, side, decoMap);
		}
	}

	public void build(EntityPainting.EnumArt art, World world, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		ArrayList<Vec3i> rotated = this.alignFootprint(this.artFootprints.get(art), side);

		for (Vec3i placement : rotated) {
			BlockPos pos = start.add(placement);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			decoMap.add(pos);
		}
		this.createEntityDecoration(art, world, start, side);
	}

	protected void createEntityDecoration(EntityPainting.EnumArt art, World world, BlockPos pos, EnumFacing side) {
		EntityPainting painting = new EntityPainting(world);
		painting.art = art;
		painting.facingDirection = side.getOpposite();
		float rotation = side.getHorizontalAngle();
		// Need to add 0.5 to each position amount so it spawns in the middle of the tile
		painting.setPositionAndRotation((pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), rotation, 0f);
		world.spawnEntity(painting);
	}

	public ArrayList<EntityPainting.EnumArt> getArtThatWouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap) {
		ArrayList<EntityPainting.EnumArt> fitList = new ArrayList<>();

		for (EntityPainting.EnumArt art : EntityPainting.EnumArt.values()) {
			boolean fits = true;
			ArrayList<Vec3i> rotated = alignFootprint(artFootprints.get(art), side);

			for (Vec3i placement : rotated) {
				BlockPos pos = start.add(placement);
				if (!decoArea.contains(pos) || decoMap.contains(pos)) {
					fits = false;
					break;
				}
			}

			if (fits) {
				fitList.add(art);
			}
		}

		return fitList;
	}

	private ArrayList<Vec3i> getFootPrintFromArtType(EntityPainting.EnumArt artType) {
		final int pixelsPerBlock = 16;
		ArrayList<Vec3i> footprint = new ArrayList<>();

		int blockWidth = artType.sizeX / pixelsPerBlock;
		int blockHeight = artType.sizeY / pixelsPerBlock;
		for (int x = 0; x < blockWidth; x++) {
			for (int y = 0; y < blockHeight; y++) {
				footprint.add(new Vec3i(x, y, 0));
			}
		}

		return footprint;
	}

	protected ArrayList<Vec3i> alignFootprint(List<Vec3i> unrotated, EnumFacing side) {
		ArrayList<Vec3i> result = new ArrayList<>();

		unrotated.forEach(v -> result.add(DungeonGenUtils.rotateVec3i(v, side)));

		return result;
	}
}
