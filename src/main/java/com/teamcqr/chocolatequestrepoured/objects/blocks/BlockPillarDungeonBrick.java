package com.teamcqr.chocolatequestrepoured.objects.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockPillarDungeonBrick extends BlockRotatedPillar {

	public BlockPillarDungeonBrick() {
		super(Material.ROCK);

		this.setSoundType(SoundType.STONE);
		this.setBlockUnbreakable();
		this.setHardness(Float.MAX_VALUE);
	}

}