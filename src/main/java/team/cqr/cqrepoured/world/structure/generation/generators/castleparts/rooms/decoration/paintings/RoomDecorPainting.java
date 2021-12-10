package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.paintings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class RoomDecorPainting {
	private Map<EntityPainting.EnumArt, List<Vec3i>> artFootprints = new EnumMap<>(EntityPainting.EnumArt.class);

	public RoomDecorPainting() {
		super();

		for (EntityPainting.EnumArt art : EntityPainting.EnumArt.values()) {
			this.artFootprints.put(art, this.getFootPrintFromArtType(art));
		}
	}

	public boolean wouldFit(BlockPos start, EnumFacing side, Set<BlockPos> decoArea, Set<BlockPos> decoMap) {
		return !this.getArtThatWouldFit(start, side, decoArea, decoMap).isEmpty();
	}

	public void buildRandom(World world, BlockPos start, BlockStateGenArray genArray, EnumFacing side, Set<BlockPos> decoArea, Set<BlockPos> decoMap) {
		List<EntityPainting.EnumArt> artList = this.getArtThatWouldFit(start, side, decoArea, decoMap);
		if (!artList.isEmpty()) {
			Collections.shuffle(artList);
			this.build(artList.get(0), world, start, genArray, side, decoMap);
		}
	}

	public void build(EntityPainting.EnumArt art, World world, BlockPos start, BlockStateGenArray genArray, EnumFacing side, Set<BlockPos> decoMap) {
		List<Vec3i> rotated = this.alignFootprint(this.artFootprints.get(art), side);

		for (Vec3i placement : rotated) {
			BlockPos pos = start.add(placement);
			decoMap.add(pos);
		}
		this.createEntityDecoration(art, world, start, genArray, side);
	}

	protected void createEntityDecoration(EntityPainting.EnumArt art, World world, BlockPos pos, BlockStateGenArray genArray, EnumFacing side) {
		EntityPainting painting = new EntityPainting(world);
		painting.art = art;
		painting.facingDirection = side.getOpposite();
		float rotation = side.getHorizontalAngle();
		// Need to add 0.5 to each position amount so it spawns in the middle of the tile
		painting.setPosition((pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5));
		painting.rotationYaw = rotation;
		genArray.addEntity(BlockPos.ORIGIN, painting);

	}

	public List<EntityPainting.EnumArt> getArtThatWouldFit(BlockPos start, EnumFacing side, Set<BlockPos> decoArea, Set<BlockPos> decoMap) {
		List<EntityPainting.EnumArt> fitList = new ArrayList<>();

		for (EntityPainting.EnumArt art : EntityPainting.EnumArt.values()) {
			boolean fits = true;
			List<Vec3i> rotated = this.alignFootprint(this.artFootprints.get(art), side);

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

	private List<Vec3i> getFootPrintFromArtType(EntityPainting.EnumArt artType) {
		final int pixelsPerBlock = 16;
		List<Vec3i> footprint = new ArrayList<>();

		int blockWidth = artType.sizeX / pixelsPerBlock;
		int blockHeight = artType.sizeY / pixelsPerBlock;
		for (int x = 0; x < blockWidth; x++) {
			for (int y = 0; y < blockHeight; y++) {
				footprint.add(new Vec3i(x, y, 0));
			}
		}

		return footprint;
	}

	protected List<Vec3i> alignFootprint(List<Vec3i> unrotated, EnumFacing side) {
		List<Vec3i> result = new ArrayList<>();

		unrotated.forEach(v -> result.add(DungeonGenUtils.rotateVec3i(v, side)));

		return result;
	}
}
