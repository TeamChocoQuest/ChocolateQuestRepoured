package team.cqr.cqrepoured.entity.boss.gianttortoise;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.entity.MultiPartEntityPartSizable;

public class SubEntityGiantTortoisePart extends MultiPartEntityPartSizable<EntityCQRGiantTortoise> {

	private boolean isHead;

	public SubEntityGiantTortoisePart(EntityCQRGiantTortoise parent, String partName, float width, float height, boolean isHead) {
		super(parent, partName, width, height);

		this.isImmuneToFire = true;

		this.setSize(width, height);

		// setInvisible(true);
	}

	public EntityCQRGiantTortoise getParent() {
		return (EntityCQRGiantTortoise) this.parent;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isHead) {
			amount *= 1.5F;
		}
		return this.getParent().attackEntityFromPart(this, source, amount);
	}

	@Override
	public void setFire(int seconds) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.tick();

		++this.ticksExisted;
	}

	// As this is a part it does not make any noises
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
	}

	@Override
	public void setRotation(float yaw, float pitch) {
		super.setRotation(yaw, pitch);
	}

	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		if (this.getParent().isDead) {
			return false;
		}
		return this.getParent().processInitialInteract(player, hand);
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}

}
