package team.cqr.cqrepoured.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

public abstract class AbstractEntityCQRMageBase extends AbstractEntityCQRBoss {

	private static final EntityDataAccessor<Boolean> IDENTITY_HIDDEN = SynchedEntityData.<Boolean>defineId(AbstractEntityCQRMageBase.class, EntityDataSerializers.BOOLEAN);

	protected AbstractEntityCQRMageBase(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(IDENTITY_HIDDEN, true);
	}

	public void revealIdentity() {
		this.entityData.set(IDENTITY_HIDDEN, false);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	public boolean isIdentityHidden() {
		return this.entityData.get(IDENTITY_HIDDEN);
	}
	
	@Override
	protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
		super.actuallyHurt(damageSrc, damageAmount);

		if (!this.level().isClientSide() && (this.getHealth() / this.getMaxHealth()) < 0.83F) {
			this.revealIdentity();
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("identityHidden", this.isIdentityHidden());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("identityHidden") && !compound.getBoolean("identityHidden")) {
			this.revealIdentity();
		}
	}

	protected static final Component HIDDEN_NAME = Component.literal("???");
	
	@Override
	public Component getDisplayName() {
		if (this.isIdentityHidden()) {
			return HIDDEN_NAME;
		}
		return super.getDisplayName();
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}

}
