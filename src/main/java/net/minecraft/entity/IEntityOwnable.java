package net.minecraft.entity;

import java.util.UUID;

import net.minecraft.world.entity.Entity;

public interface IEntityOwnable {
	
	public Entity getOwner();
	public UUID getOwnerId();

}
