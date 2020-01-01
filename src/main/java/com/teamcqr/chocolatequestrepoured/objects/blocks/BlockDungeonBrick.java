package com.teamcqr.chocolatequestrepoured.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDungeonBrick extends Block {

	public BlockDungeonBrick() {
		super(Material.ROCK);

		this.setSoundType(SoundType.STONE);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
	}

}