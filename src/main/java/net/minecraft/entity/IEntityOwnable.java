package net.minecraft.entity;

import net.minecraft.world.entity.Entity;

import java.util.UUID;

public interface IEntityOwnable {
	
	public Entity getOwner();
	public UUID getOwnerId();

}
