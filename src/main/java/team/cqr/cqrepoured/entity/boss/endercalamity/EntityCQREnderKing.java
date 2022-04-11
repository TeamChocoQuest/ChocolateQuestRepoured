package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.ai.EntityAITeleportToTargetWhenStuck;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ServerProtectedRegionManager;

public class EntityCQREnderKing extends AbstractEntityCQRBoss {

	protected static final DataParameter<Boolean> WIDE = EntityDataManager.<Boolean>defineId(EntityCQREnderKing.class, DataSerializers.BOOLEAN);

	public EntityCQREnderKing(World world) {
		this(CQREntityTypes.ENDER_KING.get(), world);
	}
	
	public EntityCQREnderKing(EntityType<? extends EntityCQREnderKing> type, World worldIn) {
		super(type, worldIn);
		this.maxUpStep = 1.0F;
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
	}

	@Override
	protected void customServerAiStep() {
		if (this.isInWater() || (this.isInWaterOrRain() && this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty())) {
			this.hurt(DamageSource.DROWN, 1.0F);
		}
		super.customServerAiStep();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		this.goalSelector.addGoal(3, new EntityAITeleportToTargetWhenStuck<>(this));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		amount /= 2;
		if (source instanceof IndirectEntityDamageSource || source.isBypassArmor()) {
			for (int i = 0; i < 64; ++i) {
				if (this.teleportRandomly()) {
					if (source.isBypassArmor()) {
						return super.hurt(source, amount);
					}
					return false;
				}
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(WIDE, DungeonGenUtils.percentageRandom(0.05));
	}

	@Override
	public ITextComponent getDisplayName() {
		if (this.isWide()) {
			return new StringTextComponent("Wide Enderman");
		}
		return super.getDisplayName();
	}

	public boolean isWide() {
		return this.entityData.get(WIDE);
	}

	@Override
	protected boolean doesExplodeOnDeath() {
		return true;
	}

	@Override
	protected boolean usesEnderDragonDeath() {
		return false;
	}

	@Override
	public void die(DamageSource cause) {
		// DONE: SPawn the true boss, BEFORE super.onDeath (that one creates the living death event)
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(this.level);
		if (manager instanceof ServerProtectedRegionManager) {

			EntityCalamitySpawner cs = new EntityCalamitySpawner(this.level);
			BlockPos pos = this.hasHomePositionCQR() ? this.getHomePositionCQR() : this.blockPosition();
			cs.setPos(pos.getX(), pos.getY(), pos.getZ());
			cs.setFaction(this.getFaction().getName());

			this.level.addFreshEntity(cs);

			EntityUtil.addEntityToAllRegionsAt(pos, cs);
		}
		super.die(cause);
	}

	protected boolean teleportRandomly() {
		double d0 = this.getX() + (this.getRandom().nextDouble() - 0.5D) * 64.0D;
		double d1 = this.getY() + (this.getRandom().nextInt(64) - 32);
		double d2 = this.getZ() + (this.getRandom().nextDouble() - 0.5D) * 64.0D;
		return this.teleportEnderman(d0, d1, d2);
	}
	
	@Override
	public void teleport(double x, double y, double z) {
		if(!this.teleportEnderman(x, y, z)) {
			super.teleport(x, y, z);
		}
	}
	
	public boolean teleportEnderman(double pX, double pY, double pZ) {
		 BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pX, pY, pZ);

	      while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
	         blockpos$mutable.move(Direction.DOWN);
	      }

	      BlockState blockstate = this.level.getBlockState(blockpos$mutable);
	      boolean flag = blockstate.getMaterial().blocksMotion();
	      boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
	      if (flag && !flag1) {
	         net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
	         if (event.isCanceled()) return false;
	         boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
	         if (flag2 && !this.isSilent()) {
	            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
	            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
	         }

	         return flag2;
	      } else {
	         return false;
	      }
	}

	@Override
	public float getBaseHealth() {
		return 2F * CQRConfig.baseHealths.Enderman;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return /* this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : */ SoundEvents.ENDERMAN_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENDERMAN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENDERMAN_DEATH;
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}
	
	@Override
	public double getEyeY() {
		return this.getBbHeight() * 0.875F;
	}

	@Override
	public void aiStep() {
		if (this.level.isClientSide) {
			// Client
			for (int i = 0; i < 4; ++i) {
				this.level.addParticle(ParticleTypes.PORTAL, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), (this.getRandom().nextDouble() - 0.5D) * 2.0D, -this.getRandom().nextDouble(),
						(this.getRandom().nextDouble() - 0.5D) * 2.0D);
			}
		}
		super.aiStep();
	}
	
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, SpawnReason p_213386_3_, ILivingEntityData setDamageValue, CompoundNBT p_213386_5_) {
		this.populateDefaultEquipmentSlots(difficulty);
		
		return super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, setDamageValue, p_213386_5_);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(difficulty);

		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(CQRItems.GREAT_SWORD_DIAMOND.get()));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING.get(), 3));

		this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(CQRItems.KING_CROWN.get(), 1));

		// Give him some armor...
		CompoundNBT nbttagcompound = new CompoundNBT();
		CompoundNBT nbttagcompound1 = nbttagcompound.getCompound("display");

		if (!nbttagcompound.contains("display", 10)) {
			nbttagcompound.put("display", nbttagcompound1);
		}

		nbttagcompound1.putInt("color", 0x9000FF);
		ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE.get(), 1, nbttagcompound);
		((ItemArmorDyable) CQRItems.CHESTPLATE_DIAMOND_DYABLE.get()).setColor(chest, 0x9000FF);
		this.setItemSlot(EquipmentSlotType.CHEST, chest);
	}

	@Override
	protected float getSoundVolume() {
		return 2F * super.getSoundVolume();
	}

	@Override
	protected float getVoicePitch() {
		return 0.75F * super.getVoicePitch();
	}
	
	@Override
	public int getAmbientSoundInterval() {
		// Super: 80
		return 60;
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("wide_enderman", this.isWide());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(WIDE, compound.getBoolean("wide_enderman"));
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
