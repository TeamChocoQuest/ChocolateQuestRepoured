package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class EntityAIFireball extends AbstractCQREntityAI<AbstractEntityCQR> {
	
	private int cooldown = 100;

	public EntityAIFireball(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(cooldown > 0) {
			cooldown--;
			return false;
		}
		return entity != null && !entity.isDead && entity.hasAttackTarget() && cooldown <= 0 && hasFireball();
	}

	private boolean hasFireball() {
		ItemStack item = entity.getHeldItemOffhand();
		if(item.isEmpty() || item.getItem() != Items.FIRE_CHARGE) {
			return false;
		}
		return true;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		cooldown = 140;
		entity.getHeldItemOffhand().shrink(1);
		Vec3d v = entity.getAttackTarget().getPositionVector().subtract(entity.getPositionVector());
		EntityFireball fireball = entity.getRNG().nextDouble() > 0.7 ? new EntityLargeFireball(entity.world, entity, v.x, v.y, v.z) : new EntitySmallFireball(entity.world, entity, v.x, v.y, v.z);
		entity.world.spawnEntity(fireball);
	}

}
