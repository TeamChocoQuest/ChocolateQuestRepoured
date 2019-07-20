package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;

import net.minecraft.entity.Entity;

public interface ICQREntity {
	
	public EFaction getFaction();
	
	public UUID getUUID();
	
	public boolean isBoss();
	public boolean isRideable();
	//This overrides the faction!!
	public boolean isFriendlyTowardsPlayer();
	public boolean hasFaction();
	
	public void spawnAt(int x, int y, int z);
	public void onKilled(Entity killer);

}
