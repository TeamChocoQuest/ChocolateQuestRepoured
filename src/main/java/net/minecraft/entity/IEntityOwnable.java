package net.minecraft.entity;

import java.util.UUID;

public interface IEntityOwnable {
	
	public Entity getOwner();
	public UUID getOwnerId();

}
