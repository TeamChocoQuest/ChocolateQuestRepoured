package team.cqr.cqrepoured.world.structure.generation.generators;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonGuardedCastle;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.part.CoverDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.part.PlateauDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class GeneratorGuardedStructure extends AbstractDungeonGenerator<DungeonGuardedCastle> {

	// DONE? remake the part where the dungeons are chosen and the support hills are being built, it does not work how it
	// should atm...

	private Map<BlockPos, CQStructure> toGenerate = new HashMap<>();
	private Map<BlockPos, Mirror> mirrorMap = new HashMap<>();
	private Map<BlockPos, Rotation> rotationMap = new HashMap<>();

	public GeneratorGuardedStructure(World world, BlockPos pos, DungeonGuardedCastle dungeon, Random rand) {
		super(world, pos, dungeon, rand);
	}

	private void processStructure(CQStructure structure, BlockPos position) {
		Mirror mirror = Mirror.NONE;
		Rotation rotation = Rotation.NONE;
		if (this.dungeon.rotateDungeon()) {
			mirror = Mirror.values()[this.random.nextInt(Mirror.values().length)];
			rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
		}
		if (this.dungeon.doBuildSupportPlatform()) {
			BlockPos structurePos = Offset.CENTER.apply(position, structure, mirror, rotation);
			BlockPos startPos = structurePos.up(this.dungeon.getUnderGroundOffset()).down();
			BlockPos endPos = startPos.add(DungeonPlacement.transform(structure.getSize().getX() - 1, 0, structure.getSize().getZ() - 1, mirror, rotation));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			PlateauDungeonPart.Builder partBuilder = new PlateauDungeonPart.Builder(pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), CQRConfig.general.supportHillWallSize);
			partBuilder.setSupportHillBlock(this.dungeon.getSupportBlock());
			partBuilder.setSupportHillTopBlock(this.dungeon.getSupportTopBlock());
			this.dungeonBuilder.add(partBuilder);
		}
		this.toGenerate.put(position, structure);
		this.mirrorMap.put(position, mirror);
		this.rotationMap.put(position, rotation);
	}

	@Override
	public void preProcess() {
		int buildings = DungeonGenUtils.randomBetween(this.dungeon.getMinBuildings(), this.dungeon.getMaxBuilding(), this.random);
		Double degrees = 360.0 / buildings;

		File structure = this.dungeon.getStructureFileFromDirectory(this.dungeon.getCenterStructureFolder(), this.random);
		BlockPos position = this.pos;
		for (int i = 0; i < buildings; i++) {
			this.processStructure(this.loadStructureFromFile(structure), position);
			structure = this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFolder(), this.random);
			Vector3i v = new Vector3i(DungeonGenUtils.randomBetween(this.dungeon.getMinDistance(), this.dungeon.getMaxDistance(), this.random), 0, 0);
			v = VectorUtil.rotateVectorAroundY(v, degrees * i);
			position = this.pos.add(v);
			position = new BlockPos(position.getX(), this.dungeon.getYForPos(this.getWorld(), position.getX(), position.getZ(), this.random), position.getZ());
		}
		this.processStructure(this.loadStructureFromFile(structure), position);

	}

	@Override
	public void buildStructure() {
		for (Map.Entry<BlockPos, CQStructure> entry : this.toGenerate.entrySet()) {
			CQStructure structure = entry.getValue();
			BlockPos structurePos = entry.getKey();

			structure.addAll(this.dungeonBuilder, structurePos, Offset.CENTER, this.mirrorMap.get(structurePos), this.rotationMap.get(structurePos));
		}
	}

	@Override
	public void postProcess() {
		if (this.dungeon.isCoverBlockEnabled()) {
			for (Map.Entry<BlockPos, CQStructure> entry : this.toGenerate.entrySet()) {
				CQStructure structure = entry.getValue();
				BlockPos structurePos = entry.getKey();
				Mirror mirror = this.mirrorMap.get(structurePos);
				Rotation rotation = this.rotationMap.get(structurePos);

				BlockPos startPos = structurePos;
				BlockPos endPos = startPos.add(DungeonPlacement.transform(structure.getSize().getX() - 1, 0, structure.getSize().getZ() - 1, mirror, rotation));
				BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
				BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
				this.dungeonBuilder.add(new CoverDungeonPart.Builder(pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ(), this.dungeon.getCoverBlock()));
			}
		}
	}

}
