//package team.cqr.cqrepoured.world.structure.generation.generators;
//
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.ChunkGenerator;
//import team.cqr.cqrepoured.config.CQRConfig;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//import team.cqr.cqrepoured.world.structure.generation.generation.part.BlockDungeonPart;
//import team.cqr.cqrepoured.world.structure.generation.generation.part.EntityDungeonPart;
//import team.cqr.cqrepoured.world.structure.generation.generation.part.PlateauDungeonPart;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.CastleRoomSelector;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.CastleRoomSelector.SupportArea;
//import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
//import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * Copyright (c) 25.05.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
// */
//public class GeneratorRandomizedCastle extends LegacyDungeonGenerator<DungeonRandomizedCastle> {
//
//	private CastleRoomSelector roomHelper;
//	private BlockPos structurePos;
//
//	public GeneratorRandomizedCastle(ChunkGenerator chunkGenerator, BlockPos pos, DungeonRandomizedCastle dungeon, Random rand) {
//		super(chunkGenerator, pos, dungeon, rand);
//	}
//
//	@Override
//	public void preProcess() {
//		this.roomHelper = new CastleRoomSelector(this.dungeon, this.random);
//		this.roomHelper.randomizeCastle();
//
//		int minX = Integer.MAX_VALUE;
//		int minZ = Integer.MAX_VALUE;
//		int maxX = Integer.MIN_VALUE;
//		int maxZ = Integer.MIN_VALUE;
//		for (SupportArea area : this.roomHelper.getSupportAreas()) {
//			minX = Math.min(area.getNwCorner().getX(), minX);
//			minZ = Math.min(area.getNwCorner().getZ(), minZ);
//			maxX = Math.max(area.getNwCorner().getX() + area.getBlocksX(), maxX);
//			maxZ = Math.max(area.getNwCorner().getZ() + area.getBlocksZ(), maxZ);
//		}
//		this.structurePos = this.pos.add((minX - maxX) / 2, 0, (minZ - maxZ) / 2);
//
//		if (this.dungeon.doBuildSupportPlatform()) {
//			for (CastleRoomSelector.SupportArea area : this.roomHelper.getSupportAreas()) {
//				// CQRMain.logger.info("{} {} {}", area.getNwCorner(), area.getBlocksX(), area.getBlocksZ());
//				BlockPos p1 = this.structurePos.add(area.getNwCorner());
//				BlockPos p2 = p1.add(area.getBlocksX(), 0, area.getBlocksZ());
//				PlateauDungeonPart.Builder partBuilder = new PlateauDungeonPart.Builder(p1.getX(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), CQRConfig.general.supportHillWallSize);
//				partBuilder.setSupportHillBlock(this.dungeon.getSupportBlock());
//				partBuilder.setSupportHillTopBlock(this.dungeon.getSupportTopBlock());
//				this.dungeonBuilder.add(partBuilder);
//			}
//		}
//	}
//
//	@Override
//	public void buildStructure() {
//		BlockStateGenArray genArray = new BlockStateGenArray(this.random);
//		List<String> bossUuids = new ArrayList<>();
//		DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());
//		this.roomHelper.generate(this.world, genArray, this.dungeon, this.pos, bossUuids, mobType);
//
//		this.dungeonBuilder.add(new BlockDungeonPart.Builder().addAll(genArray.getMainMap().values()), this.structurePos);
//		this.dungeonBuilder.add(new BlockDungeonPart.Builder().addAll(genArray.getPostMap().values()), this.structurePos);
//		this.dungeonBuilder.add(new EntityDungeonPart.Builder().addAll(genArray.getEntityMap()), this.structurePos);
//	}
//
//	@Override
//	public void postProcess() {
//		// Does nothing here
//	}
//
//}
