package team.cqr.cqrepoured.entity.boss.gianttortoise;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.MultiPartEntityPartSizable;

public class SubEntityGiantTortoisePart extends MultiPartEntityPartSizable<EntityCQRGiantTortoise> {

	private boolean isHead;

	public SubEntityGiantTortoisePart(EntityCQRGiantTortoise parent, String partName, float width, float height, boolean isHead) {
		super(parent, partName, width, height);

		// setInvisible(true);
	}
	
	@Override
	public boolean fireImmune() {
		return true;
	}

	public EntityCQRGiantTortoise getParent() {
		return (EntityCQRGiantTortoise) this.getParent();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isHead) {
			amount *= 1.5F;
		}
		return this.getParent().hurt(this, source, amount);
	}

	@Override
	public void setSecondsOnFire(int seconds) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	// As this is a part it does not make any noises
	
	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (this.getParent() == null || (this.getParent() != null && !this.getParent().isAlive())) {
			return ActionResultType.FAIL;
		}
		return this.getParent().interact(player, hand);
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}
	
	@Override
	protected Class<? extends CQRPartEntity<?>> getClassForRenderer() {
		return SubEntityGiantTortoisePart.class;
	}

}
