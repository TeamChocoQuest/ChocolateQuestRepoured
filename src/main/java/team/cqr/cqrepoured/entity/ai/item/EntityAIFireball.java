package team.cqr.cqrepoured.entity.ai.item;

import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
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
		DamagingProjectileEntity fireball = this.entity.getRandom().nextDouble() > 0.7 ? new FireballEntity(this.entity.level, this.entity, v.x, v.y, v.z) : new SmallFireballEntity(this.entity.level, this.entity, v.x, v.y, v.z);
		this.entity.level.addFreshEntity(fireball);
	}

}
