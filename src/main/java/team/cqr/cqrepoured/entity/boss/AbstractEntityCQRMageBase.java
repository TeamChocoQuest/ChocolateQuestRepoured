package team.cqr.cqrepoured.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.init.CQRItems;

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

		if (!this.level.isClientSide && (this.getHealth() / this.getMaxHealth()) < 0.83F) {
			this.revealIdentity();
		}
	}

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, MobSpawnType p_213386_3_, ILivingEntityData setDamageValue, CompoundTag p_213386_5_) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CQRItems.STAFF_VAMPIRIC.get(), 1));
		return super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, setDamageValue, p_213386_5_);
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

	protected static final TextComponent HIDDEN_NAME = new TextComponent("???");
	
	@Override
	public TextComponent getDisplayName() {
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
