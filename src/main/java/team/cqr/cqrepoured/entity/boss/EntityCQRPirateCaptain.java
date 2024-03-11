package team.cqr.cqrepoured.entity.boss;

import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.ILivingEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.util.AzureLibUtil;
import mod.azure.azurelib3.core.manager.AnimationData;
import mod.azure.azurelib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.Capes;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.IShoulderEntityProvider;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateFleeSpell;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateTeleportBehindEnemy;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateTurnInvisible;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;

public class EntityCQRPirateCaptain extends AbstractEntityCQRBoss implements IAnimatableCQR, IShoulderEntityProvider {

	private static final EntityDataAccessor<Boolean> IS_DISINTEGRATING = SynchedEntityData.<Boolean>defineId(EntityCQRPirateCaptain.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_REINTEGRATING = SynchedEntityData.<Boolean>defineId(EntityCQRPirateCaptain.class, EntityDataSerializers.BOOLEAN);

	public static int TURN_INVISIBLE_ANIMATION_TIME = 15;

	private boolean spawnedParrot = false;

	public EntityCQRPirateCaptain(Level world) {
		this(CQREntityTypes.PIRATE_CAPTAIN.get(), world);
	}

	public EntityCQRPirateCaptain(EntityType<? extends EntityCQRPirateCaptain> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.pirateCaptain.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance potioneffectIn) {
		if (!super.canBeAffected(potioneffectIn)) {
			return false;
		}
		if (potioneffectIn.getEffect().isBeneficial()) {
			return true;
		}
		return potioneffectIn.getEffect() == MobEffects.GLOWING;
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		this.spawnShoulderEntities();
		return super.doHurtTarget(entityIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		this.spellHandler.addSpell(1, new BossAIPirateSummonParrot(this, 0, 20, 20));
		this.spellHandler.addSpell(0, new BossAIPirateFleeSpell(this, 60, 30, 30));

		this.goalSelector.addGoal(2, new BossAIPirateTurnInvisible(this));
		this.goalSelector.addGoal(1, new BossAIPirateTeleportBehindEnemy(this));
	}

	@Override
	public void setSitting(boolean sitting) {
		super.setSitting(false);
	}

	@Override
	public boolean hasCape() {
		return true;
	}

	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_PIRATE;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(IS_DISINTEGRATING, false);
		this.entityData.define(IS_REINTEGRATING, false);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(difficulty);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER.get(), 1));

		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, new ItemStack(CQRItems.BULLET_FIRE.get(), 64));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING.get(), 2));
	}

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, MobSpawnType p_213386_3_, ILivingEntityData setDamageValue, CompoundTag p_213386_5_) {
		this.populateDefaultEquipmentSlots(difficulty);
		return super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, setDamageValue, p_213386_5_);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("spawnedParrot", this.spawnedParrot);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.spawnedParrot = compound.getBoolean("spawnedParrot");
	}

	public boolean hasSpawnedParrot() {
		return this.spawnedParrot;
	}

	public void setSpawnedParrot(boolean b) {
		this.spawnedParrot = b;
	}

	public void setIsDisintegrating(boolean value) {
		this.entityData.set(IS_DISINTEGRATING, value);
	}

	public void setIsReintegrating(boolean value) {
		this.entityData.set(IS_REINTEGRATING, value);
	}

	public boolean isDisintegrating() {
		return this.entityData.get(IS_DISINTEGRATING);
	}

	public boolean isReintegrating() {
		return this.entityData.get(IS_REINTEGRATING);
	}

	@Override
	public float getInvisibility() {
		return this.entityData.get(INVISIBILITY);
	}

	@Override
	protected int getInvisibilityTurningTime() {
		return EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	// Geckolib
	private AnimationFactory factory = AzureLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		this.registerControllers(this, data);
	}
	
	@Override
	public Optional<CompoundTag> getShoulderEntityFor(String boneName) {
		if (boneName.equalsIgnoreCase(CQRConstants.Entity.PirateCaptain.PARROT_BONE_NAME)) {
			return Optional.ofNullable(this.getLeftShoulderEntity());
		}
		return Optional.empty();
	}
}
