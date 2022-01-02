package team.cqr.cqrepoured.entity.ai.item;

import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
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
	public void start() {
		super.start();
		this.cooldown = 140;
		this.entity.getHeldItemOffhand().shrink(1);
		Vector3d v = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector());
		DamagingProjectileEntity fireball = this.entity.getRNG().nextDouble() > 0.7 ? new FireballEntity(this.entity.world, this.entity, v.x, v.y, v.z) : new SmallFireballEntity(this.entity.world, this.entity, v.x, v.y, v.z);
		this.entity.world.spawnEntity(fireball);
	}

}
