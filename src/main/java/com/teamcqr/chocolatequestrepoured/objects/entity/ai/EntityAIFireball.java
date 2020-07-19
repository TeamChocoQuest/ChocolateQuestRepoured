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
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity != null && !this.entity.isDead && this.entity.hasAttackTarget() && this.cooldown <= 0 && this.hasFireball();
	}

	private boolean hasFireball() {
		ItemStack item = this.entity.getHeldItemOffhand();
		if (item.isEmpty() || item.getItem() != Items.FIRE_CHARGE) {
			return false;
		}
		return true;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.cooldown = 140;
		this.entity.getHeldItemOffhand().shrink(1);
		Vec3d v = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector());
		EntityFireball fireball = this.entity.getRNG().nextDouble() > 0.7 ? new EntityLargeFireball(this.entity.world, this.entity, v.x, v.y, v.z) : new EntitySmallFireball(this.entity.world, this.entity, v.x, v.y, v.z);
		this.entity.world.spawnEntity(fireball);
	}

}
