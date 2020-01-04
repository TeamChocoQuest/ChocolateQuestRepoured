package com.teamcqr.chocolatequestrepoured.API.events;

import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Copyright (c) 11.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CQProtectedRegionEnterEvent extends Event {
	private ProtectedRegion region;
	private ChunkPos pos;
	private EntityPlayer player;
	private World world;

	public CQProtectedRegionEnterEvent(ProtectedRegion region, ChunkPos pos, EntityPlayer player) {
		this.region = region;
		this.pos = pos;
		this.player = player;
		this.world = player.world;
	}

	public ProtectedRegion getRegion() {
		return this.region;
	}

	public ChunkPos getPos() {
		return this.pos;
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public World getWorld() {
		return this.world;
	}
}
