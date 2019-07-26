package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICQREntity {
	
	public EFaction getFaction();
	
	public UUID getUUID();
	
	public boolean isBoss();
	public boolean isRideable();
	//This overrides the faction!!
	public boolean isFriendlyTowardsPlayer();
	public boolean hasFaction();
	
	public float getBaseHealth();
	
	public default float getBaseHealth(BlockPos dungeonPos) {
		float distance = Math.abs(dungeonPos.getX()) > Math.abs(dungeonPos.getZ()) ? Math.abs(dungeonPos.getX()) : Math.abs(dungeonPos.getZ());
		distance /= Reference.CONFIG_HELPER_INSTANCE.getHealthDistanceDivisor();
		distance /= 10;
		distance = distance < 1 ? distance++ : distance;
		
		return distance * this.getBaseHealth();
	}
	
	public void spawnAt(int x, int y, int z);
	public default void onKilled(Entity killer, Entity killed) {
		World world = killer.getEntityWorld();
		if(world != null && killer instanceof EntityPlayer && this.hasFaction()) {
			AxisAlignedBB aabb = new AxisAlignedBB(killed.getPosition().add(
						-Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius(), 
						-Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius(), 
						-Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius()
					), 
					killed.getPosition().add(
							Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius(),
							Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius(),
							Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius()
					)
				);
			for(Entity entity : world.getEntitiesInAABBexcluding(killed, aabb, null)) {
				if(entity instanceof ICQREntity) {
					ICQREntity CQRBaseEntity = (ICQREntity) entity;
					//TODO: Faction repu change!!
					if(this.getFaction().isEnemy(CQRBaseEntity.getFaction())) {
						//The player gains repu in CQBaseEntity's faction
						
					} else if(this.getFaction().isAlly(CQRBaseEntity.getFaction())) {
						//The player looses repu in the faction of CQBaseEntity's faction
						
					}
				}
			}
		}
	}
	
	public default void handleArmorBreaking(float currHP, float maxHP, EntityLiving entityDamaged) {
		if((currHP / maxHP) > 0 && (currHP / maxHP) % 0.2F == 0) {
			//TODO: Play breaking sound
		}
		//Armor breaking stuff
    	//Only 1/5 of health left -> Loose chestplate
    	if(currHP / maxHP <= 0.2F) {
    		//TODO: remove chest
    	} else 
    	//Only 2/5 of health left -> Loose pants
    	if(currHP / maxHP <= 0.4F) {
    		//TODO: remove pants
    	} else
    	//Only 3/5 of health left -> loose boobs
    	if(currHP / maxHP <= 0.6F) {
    		//TODO: remove boobs
    	} else
    	//Only 4/5 of health left -> loose helmet
    	if(currHP / maxHP <= 0.8F) {
    		//TODO: Remove helmet
    	}
	}

	
}
