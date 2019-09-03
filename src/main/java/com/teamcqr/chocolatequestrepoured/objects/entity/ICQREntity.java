package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICQREntity {
	
	public EFaction getFaction();
	
	public UUID getUUID();
	
	public int getRemainingHealingPotions();
	
	public boolean isBoss();
	public boolean isRideable();
	//This overrides the faction!!
	public boolean isFriendlyTowardsPlayer();
	public boolean hasFaction();
	
	public float getBaseHealth();

	public EntityLivingBase getLeader();
	public void setLeader(EntityLivingBase leader);

	public BlockPos getHome();
	public void setHome(BlockPos home);
	
	public default float getBaseHealthForLocation(BlockPos dungeonPos, float health) {
		if (dungeonPos != null) {
			float x = (float) dungeonPos.getX();
			float z = (float) dungeonPos.getZ();
			float distance = (float) Math.sqrt(x * x + z * z);
			
			health *= 1.0F + distance / Reference.CONFIG_HELPER_INSTANCE.getHealthDistanceDivisor();
		}

    /*
		//System.out.println("Pos: " + dungeonPos.toString());
		float distance = Math.abs(dungeonPos.getX()) > Math.abs(dungeonPos.getZ()) ? Math.abs(dungeonPos.getX()) : Math.abs(dungeonPos.getZ());
		if(distance <= 0.0f) {
			return defBaseHealth;
		}
		//System.out.println("Distance: " + distance);
		distance /= Reference.CONFIG_HELPER_INSTANCE.getHealthDistanceDivisor();
		//System.out.println("Distance: " + distance);
		distance /= 10;
		//System.out.println("Distance: " + distance);
		distance = distance < 1 ? distance+=1.0f : distance;
		
		//System.out.println("Distance: " + distance);
		//System.out.println("HP: " + (distance * defBaseHealth));
		
		return distance * defBaseHealth;
    */
    return health;

	}
	
	public void onSpawnFromCQRSpawnerInDungeon(int x, int y, int z);
	
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
				if(entity instanceof ICQREntity && (entity instanceof EntityLiving && (((EntityLiving) entity).getEntitySenses().canSee(killed) || ((EntityLiving) entity).getEntitySenses().canSee(killer)))) {
					ICQREntity CQRBaseEntity = (ICQREntity) entity;
					//TODO: Faction repu change!!
					if(((ICQREntity)killed).getFaction().isEnemy(CQRBaseEntity.getFaction())) {
						//The player gains repu in CQBaseEntity's faction
						
					} else if(((ICQREntity)killed).getFaction().isAlly(CQRBaseEntity.getFaction())) {
						//The player looses repu in the faction of CQBaseEntity's faction
						
					}
				}
			}
		}
	}
	
	public default void handleArmorBreaking(float currHP, float maxHP, EntityLiving entityDamaged) {
		//Armor breaking stuff
		boolean armorBroke = false;
    	//Only 1/5 of health left -> Loose chestplate
		float hpPrcntg = currHP / maxHP;
		//System.out.println("Hp %: " + hpPrcntg);
    	if(hpPrcntg <= 0.2F && entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && !entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(Items.AIR)) {
    		//DONE: remove chest
    		entityDamaged.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.AIR, 1));
    		
    		armorBroke = true;
    	}
    	//Only 2/5 of health left -> Loose pants
    	if(hpPrcntg <= 0.4F && entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.LEGS) != null && !entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().equals(Items.AIR)) {
    		//DONE: remove pants
    		entityDamaged.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.AIR, 1));
    		
    		armorBroke = true;
    	}
    	//Only 3/5 of health left -> loose boobs
    	if(hpPrcntg <= 0.6F && entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.FEET) != null && !entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().equals(Items.AIR)) {
    		//DONE: remove boobs
    		entityDamaged.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.AIR, 1));
    		
    		armorBroke = true;
    	}
    	//Only 4/5 of health left -> loose helmet
    	if(hpPrcntg <= 0.8F && entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null && !entityDamaged.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(Items.AIR)) {
    		//DONE: Remove helmet
    		entityDamaged.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.AIR, 1));
    		
    		armorBroke = true;
    	}
    	
    	if(armorBroke && entityDamaged.getEntityWorld() != null/* && !entityDamaged.getEntityWorld().isRemote*/) {
    		entityDamaged.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.75F, 0.8F);
    		//System.out.println("Sound played!");
    	}
	}
	
}
