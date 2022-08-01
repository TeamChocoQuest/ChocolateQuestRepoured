package team.cqr.cqrepoured.entity.boss;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.Capes;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateFleeSpell;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateTeleportBehindEnemy;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateTurnInvisible;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;

public class EntityCQRPirateCaptain extends AbstractEntityCQRBoss implements IAnimatableCQR {

	private static final DataParameter<Boolean> IS_DISINTEGRATING = EntityDataManager.<Boolean>defineId(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_REINTEGRATING = EntityDataManager.<Boolean>defineId(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);

	public static int TURN_INVISIBLE_ANIMATION_TIME = 15;

	private boolean spawnedParrot = false;

	public EntityCQRPirateCaptain(World world) {
		this(CQREntityTypes.PIRATE_CAPTAIN.get(), world);
	}

	public EntityCQRPirateCaptain(EntityType<? extends EntityCQRPirateCaptain> type, World worldIn) {
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
	public boolean canBeAffected(EffectInstance potioneffectIn) {
		if (!super.canBeAffected(potioneffectIn)) {
			return false;
		}
		if (potioneffectIn.getEffect().isBeneficial()) {
			return true;
		}
		return potioneffectIn.getEffect() == Effects.GLOWING;
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
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER.get(), 1));

		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, new ItemStack(CQRItems.BULLET_FIRE.get(), 64));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING.get(), 2));
	}

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, SpawnReason p_213386_3_, ILivingEntityData setDamageValue, CompoundNBT p_213386_5_) {
		this.populateDefaultEquipmentSlots(difficulty);
		return super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, setDamageValue, p_213386_5_);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("spawnedParrot", this.spawnedParrot);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
}
