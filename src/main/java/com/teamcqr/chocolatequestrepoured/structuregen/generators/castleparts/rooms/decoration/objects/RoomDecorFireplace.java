package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;

public class RoomDecorFireplace extends RoomDecorBlocksBase {
	public RoomDecorFireplace() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 0, 0, Blocks.NETHERRACK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(2, 0, 0, Blocks.BRICK_BLOCK.getDefaultState()));

		this.schematic.add(new DecoBlockBase(0, 0, 1, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 0, 1, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(2, 0, 1, Blocks.BRICK_BLOCK.getDefaultState()));

		this.schematic.add(new DecoBlockBase(0, 1, 0, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 1, 0, Blocks.FIRE.getDefaultState()));
		this.schematic.add(new DecoBlockBase(2, 1, 0, Blocks.BRICK_BLOCK.getDefaultState()));

		this.schematic.add(new DecoBlockBase(0, 2, 0, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 2, 0, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(2, 2, 0, Blocks.BRICK_BLOCK.getDefaultState()));

		this.schematic.add(new DecoBlockBase(1, 3, 0, Blocks.BRICK_BLOCK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 4, 0, Blocks.BRICK_BLOCK.getDefaultState()));
	}
}
