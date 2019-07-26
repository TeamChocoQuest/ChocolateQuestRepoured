package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface ICQREntity {
	
	public EFaction getFaction();
	
	public UUID getUUID();
	
	public boolean isBoss();
	public boolean isRideable();
	//This overrides the faction!!
	public boolean isFriendlyTowardsPlayer();
	public boolean hasFaction();
	
	public double getBaseHealth();
	
	public default double getHealth(BlockPos dungeonPos) {
		double distance = Math.abs(dungeonPos.getX()) > Math.abs(dungeonPos.getZ()) ? Math.abs(dungeonPos.getX()) : Math.abs(dungeonPos.getZ());
		distance /= Reference.CONFIG_HELPER_INSTANCE.getHealthDistanceDivisor();
		distance /= 10;
		distance = distance < 1 ? distance++ : distance;
		
		return distance * this.getBaseHealth();
	}
	
	public void spawnAt(int x, int y, int z);
	public void onKilled(Entity killer);

	
}
