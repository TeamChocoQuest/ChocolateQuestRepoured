package com.teamcqr.chocolatequestrepoured.API.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	@Nullable
	private BlockPos shieldCorePosition = null;
	private ArrayList<String> bossUUIDs = new ArrayList<>();
	private World world;

	public CQDungeonStructureGenerateEvent(DungeonBase dungeon, BlockPos position, BlockPos size, World world, List<String> uuids) {
		this.generatedDungeon = dungeon;
		this.dunPosition = position;
		this.dunSize = size;
		this.world = world;
		bossUUIDs.addAll(uuids);
	}
	
	public ArrayList<String> getBossIDs() {
		return bossUUIDs;
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

	@Nullable
	public BlockPos getShieldCorePosition() {
		return this.shieldCorePosition;
	}

	public void setShieldCorePosition(BlockPos pos) {
		if (this.shieldCorePosition != null) {
			if (!this.shieldCorePosition.equals(pos)) {
				this.shieldCorePosition = pos;
			}
		} else {
			this.shieldCorePosition = pos;
		}
	}

	public World getWorld() {
		return this.world;
	}
}
