package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.paintings;

import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import java.util.*;

public class RoomDecorPainting {
	private Map<PaintingEntity.EnumArt, List<Vector3i>> artFootprints = new EnumMap<>(PaintingEntity.EnumArt.class);

	public RoomDecorPainting() {
		super();

		for (PaintingEntity.EnumArt art : PaintingEntity.EnumArt.values()) {
			this.artFootprints.put(art, this.getFootPrintFromArtType(art));
		}
	}

	public boolean wouldFit(BlockPos start, Direction side, Set<BlockPos> decoArea, Set<BlockPos> decoMap) {
		return !this.getArtThatWouldFit(start, side, decoArea, decoMap).isEmpty();
	}

	public void buildRandom(World world, BlockPos start, BlockStateGenArray genArray, Direction side, Set<BlockPos> decoArea, Set<BlockPos> decoMap) {
		List<PaintingEntity.EnumArt> artList = this.getArtThatWouldFit(start, side, decoArea, decoMap);
		if (!artList.isEmpty()) {
			Collections.shuffle(artList);
			this.build(artList.get(0), world, start, genArray, side, decoMap);
		}
	}

	public void build(PaintingEntity.EnumArt art, World world, BlockPos start, BlockStateGenArray genArray, Direction side, Set<BlockPos> decoMap) {
		List<Vector3i> rotated = this.alignFootprint(this.artFootprints.get(art), side);

		for (Vector3i placement : rotated) {
			BlockPos pos = start.add(placement);
			decoMap.add(pos);
		}
		this.createEntityDecoration(art, world, start, genArray, side);
	}

	protected void createEntityDecoration(PaintingEntity.EnumArt art, World world, BlockPos pos, BlockStateGenArray genArray, Direction side) {
		PaintingEntity painting = new PaintingEntity(world);
		painting.art = art;
		painting.facingDirection = side.getOpposite();
		float rotation = side.getHorizontalAngle();
		// Need to add 0.5 to each position amount so it spawns in the middle of the tile
		painting.setPosition((pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5));
		painting.rotationYaw = rotation;
		genArray.addEntity(BlockPos.ORIGIN, painting);

	}

	public List<PaintingEntity.EnumArt> getArtThatWouldFit(BlockPos start, Direction side, Set<BlockPos> decoArea, Set<BlockPos> decoMap) {
		List<PaintingEntity.EnumArt> fitList = new ArrayList<>();

		for (PaintingEntity.EnumArt art : PaintingEntity.EnumArt.values()) {
			boolean fits = true;
			List<Vector3i> rotated = this.alignFootprint(this.artFootprints.get(art), side);

			for (Vector3i placement : rotated) {
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

	private List<Vector3i> getFootPrintFromArtType(PaintingEntity.EnumArt artType) {
		final int pixelsPerBlock = 16;
		List<Vector3i> footprint = new ArrayList<>();

		int blockWidth = artType.sizeX / pixelsPerBlock;
		int blockHeight = artType.sizeY / pixelsPerBlock;
		for (int x = 0; x < blockWidth; x++) {
			for (int y = 0; y < blockHeight; y++) {
				footprint.add(new Vector3i(x, y, 0));
			}
		}

		return footprint;
	}

	protected List<Vector3i> alignFootprint(List<Vector3i> unrotated, Direction side) {
		List<Vector3i> result = new ArrayList<>();

		unrotated.forEach(v -> result.add(DungeonGenUtils.rotateVec3i(v, side)));

		return result;
	}
}
