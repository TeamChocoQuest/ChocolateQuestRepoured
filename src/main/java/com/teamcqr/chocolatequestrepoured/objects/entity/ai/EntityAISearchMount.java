package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class EntityAISearchMount extends AbstractCQREntityAI {

	protected static final int MOUNT_SEARCH_RANGE = 15;
	protected static final float MAX_DISTANCE_WHEN_TO_MOUNT = 2F;
	protected static final boolean FORCE_MOUNTING = true; 
	protected static final double SPEED_WALK_TO_MOUNT = 1.5D;
	
	protected EntityAnimal entityToMount = null;
	
	public EntityAISearchMount(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entityToMount != null && !this.entityToMount.isDead) {
			if(this.entityToMount.getRidingEntity() != null && this.entityToMount.getRidingEntity().equals(this.entity)) {
				return false;
			}
		}
		
		if(!this.entity.isRiding()) {
			BlockPos pos1 = this.entity.getPosition().add(MOUNT_SEARCH_RANGE /2, MOUNT_SEARCH_RANGE /4, MOUNT_SEARCH_RANGE /2);
			BlockPos pos2 = this.entity.getPosition().subtract(new BlockPos(MOUNT_SEARCH_RANGE /2, MOUNT_SEARCH_RANGE /4, MOUNT_SEARCH_RANGE /2));
			List<Entity> suitableMounts = this.entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(pos1, pos2), TargetUtil.PREDICATE_MOUNTS);
			if(!suitableMounts.isEmpty()) {
				for(Entity ent : suitableMounts) {
					EntityAnimal animal = (EntityAnimal)ent;
					return (!animal.isBeingRidden());
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		if(!this.entity.isRiding() && entityToMount == null) {
			findNewTargetMount();
		}
	}
	
	private void findNewTargetMount() {
		BlockPos pos1 = this.entity.getPosition().add(MOUNT_SEARCH_RANGE /2, MOUNT_SEARCH_RANGE /4, MOUNT_SEARCH_RANGE /2);
		BlockPos pos2 = this.entity.getPosition().subtract(new BlockPos(MOUNT_SEARCH_RANGE /2, MOUNT_SEARCH_RANGE /4, MOUNT_SEARCH_RANGE /2));
		List<Entity> suitableMounts = this.entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(pos1, pos2), TargetUtil.PREDICATE_MOUNTS);
		List<Entity> listTmp = new ArrayList<>();
		suitableMounts.forEach(new Consumer<Entity>() {

			@Override
			public void accept(Entity t) {
				if(entity.canSeeEntity((EntityLivingBase) t) && !t.isBeingRidden() && !t.isDead) {
					listTmp.add(t);
				}
			}
		});
		suitableMounts = listTmp;
		suitableMounts.sort(new Comparator<Entity>() {

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
		this.entityToMount = (EntityAnimal) suitableMounts.get(0);
	}

	@Override
	public void updateTask() {
		if(shouldContinueExecuting()) {
			if(this.entityToMount != null && !this.entityToMount.isDead && !this.entityToMount.isBeingRidden()) {
				if(this.entity.getDistance(this.entityToMount) <= MAX_DISTANCE_WHEN_TO_MOUNT) {
					this.entity.startRiding(this.entityToMount, FORCE_MOUNTING);
				} else {
					this.entity.getNavigator().tryMoveToEntityLiving(this.entityToMount, SPEED_WALK_TO_MOUNT);
				}
			} else {
				findNewTargetMount();
			}
		} else {
			resetTask();
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		
		this.entityToMount = null;
	}

}
