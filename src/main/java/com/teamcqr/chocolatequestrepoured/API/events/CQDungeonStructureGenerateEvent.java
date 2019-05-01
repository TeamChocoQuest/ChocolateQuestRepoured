package com.teamcqr.chocolatequestrepoured.API.events;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;


/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CQDungeonStructureGenerateEvent extends Event {

	private DungeonBase generatedDungeon;
	private BlockPos dunPosition;
	private BlockPos dunSize;
	
	public CQDungeonStructureGenerateEvent(DungeonBase dungeon, BlockPos position, BlockPos size) {
		this.generatedDungeon = dungeon;
		this.dunPosition = position;
		this.dunSize = size;
	}
	
	public DungeonBase getDungeon() {
		return this.generatedDungeon;
	}
	public BlockPos getPos() {
		return this.dunPosition;
	}
	public BlockPos getSize() {
		return this.dunSize;
	}
	public UUID getDungeonID() {
		return this.generatedDungeon.getDungeonID();
	}

}
