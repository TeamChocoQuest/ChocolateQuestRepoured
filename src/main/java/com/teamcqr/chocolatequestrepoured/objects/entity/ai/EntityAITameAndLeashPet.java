package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public class EntityAITameAndLeashPet extends AbstractCQREntityAI {

	EntityTameable pet = null;
	
	protected final int TAMEABLE_SEARCH_RANGE = 12; 
	protected final int DISTANCE_TO_TAME = 4;
	protected boolean tamedNLeashedPet = false;
	
	public EntityAITameAndLeashPet(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(!(entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == Items.LEAD || entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == Items.LEAD )) {
			return false;
		}
		
		List<Entity> tameablesInRange = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(
				entity.getPosition().subtract(new Vec3i(TAMEABLE_SEARCH_RANGE, TAMEABLE_SEARCH_RANGE /3, TAMEABLE_SEARCH_RANGE)),
				entity.getPosition().add(new Vec3i(TAMEABLE_SEARCH_RANGE, TAMEABLE_SEARCH_RANGE /3, TAMEABLE_SEARCH_RANGE))),
				TargetUtil.PREDICATE_PETS);
		return (!tameablesInRange.isEmpty());
	}
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void updateTask() {
		if(pet == null || pet.isDead) {
			tamedNLeashedPet = false;
		}
		if(!shouldContinueExecuting() || pet == null || entity == null ||pet.isDead ||entity.isDead) {
			return;
		}
		if(tamedNLeashedPet) {
			return;
		}
		if(pet != null && !pet.isDead) {
			if(pet.getDistance(entity) > DISTANCE_TO_TAME) {
				entity.getNavigator().tryMoveToEntityLiving(pet, 0.6D);
				pet.getNavigator().tryMoveToEntityLiving(entity, 0.65D);
			} else {
				pet.setTamed(true);
				pet.setOwnerId(entity.getPersistentID());
				pet.setLeashHolder(entity, true);
				tamedNLeashedPet = false;
			}
		} else {
			List<Entity> tameablesInRange = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(
					entity.getPosition().subtract(new Vec3i(TAMEABLE_SEARCH_RANGE, TAMEABLE_SEARCH_RANGE /3, TAMEABLE_SEARCH_RANGE)),
					entity.getPosition().add(new Vec3i(TAMEABLE_SEARCH_RANGE, TAMEABLE_SEARCH_RANGE /3, TAMEABLE_SEARCH_RANGE))),
					TargetUtil.PREDICATE_PETS);
			if(!tameablesInRange.isEmpty()) {
				List<Entity> tmp = new ArrayList<Entity>();
				tameablesInRange.forEach(new Consumer<Entity>() {

					@Override
					public void accept(Entity t) {
						if(!(t instanceof EntityTameable)) {
							return;
						}
						if(((EntityTameable)t).getOwner() != null) {
							return;
						}
						if(entity.getNavigator().getPathToEntityLiving(t) != null) {
							tmp.add(t);
						}
					}
				});
				tameablesInRange = tmp;
				if(!tameablesInRange.isEmpty()) {
					tameablesInRange.sort(new Comparator<Entity>() {

						@Override
						public int compare(Entity e1, Entity e2) {
							float distE1 = entity.getDistance(e1);
							float distE2 = entity.getDistance(e2);

							if(distE1 < distE2) {
								return -1;
							}
							if(distE1 > distE2) {
								return 1;
							}
							return 0;
						}
					});
					pet = (EntityTameable) tameablesInRange.get(0);
				}
			}
		}
	}
	
	@Override
	public void resetTask() {
		pet = null;
		tamedNLeashedPet = false;
	}

}
