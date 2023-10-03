package team.cqr.cqrepoured.entity.ai.item;

import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIFireball extends AbstractCQREntityAI<AbstractEntityCQR> {

	private int cooldown = 100;

	public EntityAIFireball(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity != null && !this.entity.isDeadOrDying() && this.entity.hasAttackTarget() && this.cooldown <= 0 && this.hasFireball();
	}

	private boolean hasFireball() {
		ItemStack item = this.entity.getMainHandItem();
		if (item.isEmpty() || item.getItem() != Items.FIRE_CHARGE) {
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		super.start();
		this.cooldown = 140;
		this.entity.getMainHandItem().shrink(1);
		Vec3 v = this.entity.getTarget().position().subtract(this.entity.position());
		AbstractHurtingProjectile fireball = this.entity.getRandom().nextDouble() > 0.7 ? new LargeFireball(this.entity.level(), this.entity, v.x, v.y, v.z, 2) : new SmallFireball(this.entity.level(), this.entity, v.x, v.y, v.z);
		this.entity.level().addFreshEntity(fireball);
	}

}
