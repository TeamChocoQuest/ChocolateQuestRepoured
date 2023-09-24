package team.cqr.cqrepoured.entity.boss.netherdragon;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;

import de.dertoaster.multihitboxlib.entity.hitbox.SubPartConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level.ExplosionInteraction;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.multipart.AnimatablePartEntity;

public class SubEntityNetherDragonSegment extends AnimatablePartEntity<EntityCQRNetherDragon> implements IBlacklistedFromStatues, IDontRenderFire {

	private int partIndex = 0;
	private int realID = 0;
	private boolean isSkeletal = false;
	
	public static final EntityDimensions DEFAULT_SIZE = new EntityDimensions(1.25F, 1.25F, false);

	public SubEntityNetherDragonSegment(EntityCQRNetherDragon dragon, int partID, boolean skeletal, final SubPartConfig spc) {
		super(dragon, spc);

		this.partIndex = dragon.INITIAL_SEGMENT_COUNT - partID;
		this.realID = partID;
		this.isSkeletal = skeletal;

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
		return this.isSkeletal || this.getParent() == null || this.getParent().getSkeleProgress() >= this.realID;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || this.getParent() == null) {
			return false;
		}

		return super.hurt(source, amount);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getParent().getSegmentCount() < this.partIndex) {
			// this.world.removeEntityDangerously(this);
			this.discard();
		}
		if (this.level().isClientSide() && (this.getParent() == null || !this.getParent().isAlive())) {
			this.discard();
		}

	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.getParent() == null || !this.getParent().isAlive()) {
			return InteractionResult.FAIL;
		}
		return this.getParent().interact(player, hand);
	}

	public void explode() {
		if (!this.level().isClientSide()) {
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1, ExplosionInteraction.MOB);
		}
	}

	public void switchToSkeletalState() {
		if (!this.level().isClientSide()) {
			this.isSkeletal = true;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1, ExplosionInteraction.MOB);
		}
	}

	public void die() {
		this.explode();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("skeletal", this.isSkeletal());
		compound.putInt("realID", this.realID);
		compound.putInt("partIndex", this.partIndex);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
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
	public boolean hasCustomRenderer() {
		return true;
	}

	@Override
	public void registerControllers(ControllerRegistrar arg0) {
		
	}

}
