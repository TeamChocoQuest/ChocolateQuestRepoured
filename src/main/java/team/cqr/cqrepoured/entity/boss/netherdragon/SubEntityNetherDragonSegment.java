package team.cqr.cqrepoured.entity.boss.netherdragon;

import javax.annotation.Nullable;


import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.Explosion.Mode;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.IDontRenderFire;

public class SubEntityNetherDragonSegment extends CQRPartEntity<EntityCQRNetherDragon> implements IBlacklistedFromStatues, IDontRenderFire, IAnimatable, IAnimationTickable {

	private EntityCQRNetherDragon dragon;
	private int partIndex = 0;
	private int realID = 0;
	private boolean isSkeletal = false;
	
	public static final EntitySize DEFAULT_SIZE = new EntitySize(1.25F, 1.25F, false);

	public SubEntityNetherDragonSegment(EntityCQRNetherDragon dragon, int partID, boolean skeletal) {
		super(dragon);

		this.dragon = dragon;
		this.partIndex = dragon.INITIAL_SEGMENT_COUNT - partID;
		this.realID = partID;

		// String partName, float width, float height
		this.setInvisible(false);
	}
	
	@Override
	public boolean isPickable() {
		return true;
	}

	public void onRemovedFromBody() {
	}

	public boolean isSkeletal() {
		return this.isSkeletal || this.dragon == null || this.dragon.getSkeleProgress() >= this.realID;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.isExplosion() || source.isFire() || this.dragon == null) {
			return false;
		}

		return this.dragon.hurt(this, source, amount);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.dragon.getSegmentCount() < this.partIndex) {
			// this.world.removeEntityDangerously(this);
			this.remove();
		}
		if (this.level.isClientSide && (this.dragon == null || !this.dragon.isAlive())) {
			this.remove();
		}

	}

	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (this.dragon == null || !this.dragon.isAlive()) {
			return ActionResultType.FAIL;
		}
		return this.dragon.interact(player, hand);
	}

	@Nullable
	public EntityCQRNetherDragon getParent() {
		return this.dragon;
	}

	public void explode() {
		if (!this.level.isClientSide) {
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1, Mode.DESTROY);
		}
	}

	public void switchToSkeletalState() {
		if (!this.level.isClientSide) {
			this.isSkeletal = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1, Mode.DESTROY);
		}
	}

	public void die() {
		this.explode();
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putBoolean("skeletal", this.isSkeletal());
		compound.putInt("realID", this.realID);
		compound.putInt("partIndex", this.partIndex);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.realID = compound.getInt("realID");
		this.partIndex = compound.getInt("partIndex");
		this.isSkeletal = compound.getBoolean("skeletal");
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}

	public int getPartIndex() {
		return this.partIndex;
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	public void registerControllers(AnimationData data) {
		
	}

	// Geckolib
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public int tickTimer() {
		if(this.getParent() != null) {
			return this.getParent().tickCount;
		}
		return this.tickCount;
	}
	
	@Override
	public boolean hasCustomRenderer() {
		return true;
	}

}
