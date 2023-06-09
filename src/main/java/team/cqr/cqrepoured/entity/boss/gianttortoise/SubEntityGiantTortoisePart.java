package team.cqr.cqrepoured.entity.boss.gianttortoise;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.getParent() == null || (this.getParent() != null && !this.getParent().isAlive())) {
			return InteractionResult.FAIL;
		}
		return this.getParent().interact(player, hand);
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}
	
}
