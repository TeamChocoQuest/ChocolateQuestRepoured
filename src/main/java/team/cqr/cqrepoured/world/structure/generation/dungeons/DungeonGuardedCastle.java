package team.cqr.cqrepoured.world.structure.generation.dungeons;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.world.structure.generation.generators.GeneratorGuardedStructure;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonGuardedCastle extends DungeonBase {

	private File structureFolder;
	private File centerStructureFolder;

	private int minBuildings = 7;
	private int maxBuilding = 14;

	private int minDistance = 15;
	private int maxDistance = 30;

	public DungeonGuardedCastle(String name, Properties prop) {
		super(name, prop);

		this.structureFolder = PropertyFileHelper.getStructureFolderProperty(prop, "structurefolder", "village_buildings");

		this.centerStructureFolder = PropertyFileHelper.getStructureFolderProperty(prop, "centerstructurefolder", "village_centers");
		this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minbuildings", 6);
		this.maxBuilding = PropertyFileHelper.getIntProperty(prop, "maxbuildings", 10);

		this.minDistance = PropertyFileHelper.getIntProperty(prop, "mindistance", 20);
		this.maxDistance = PropertyFileHelper.getIntProperty(prop, "maxdistance", 40);
	}

	@Override
	public Collection<StructurePiece> runGenerator(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random) {
		Collection<StructurePiece> pieces = new ArrayList<>();
		pieces.add(new GeneratorGuardedStructure(chunkGenerator, pos, this, random).prepare());
		return pieces;
	}

	public int getMinDistance() {
		return this.minDistance;
	}

	public int getMaxDistance() {
		return this.maxDistance;
	}

	public int getMinBuildings() {
		return this.minBuildings;
	}

	public int getMaxBuilding() {
		return this.maxBuilding;
	}

	public File getStructureFolder() {
		return this.structureFolder;
	}

	public File getCenterStructureFolder() {
		return this.centerStructureFolder;
	}

	public boolean rotateDungeon() {
		// TODO
		return false;
	}

}
