package team.cqr.cqrepoured.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.block.WebBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.Capes;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.ai.boss.walkerking.BossAIWalkerLightningCircles;
import team.cqr.cqrepoured.entity.ai.boss.walkerking.BossAIWalkerLightningSpiral;
import team.cqr.cqrepoured.entity.ai.boss.walkerking.BossAIWalkerTornadoAttack;
import team.cqr.cqrepoured.entity.ai.boss.walkerking.EntityAIWalkerIllusions;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIAntiAirSpellWalker;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.entity.misc.EntityIceSpike;
import team.cqr.cqrepoured.entity.misc.EntityWalkerKingIllusion;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRWalkerKing extends AbstractEntityCQRBoss {

	private int lightningTick = 0;
	private int borderLightning = 20;
	private boolean active = false;
	private int activationCooldown = 80;
	private int dragonAttackCooldown = 0;
	private int lavaCounterAttackCooldown = 0;

	public EntityCQRWalkerKing(World worldIn) {
		super(worldIn);

		this.experienceValue = 200;
	}

	@Override
	public void enableBossBar() {
		super.enableBossBar();

		if (this.bossInfoServer != null) {
			this.bossInfoServer.setColor(Color.PURPLE);
			this.bossInfoServer.setCreateWorldFog(CQRConfig.bosses.enableWalkerKingFog);
			this.bossInfoServer.setDarkenScreen(CQRConfig.bosses.enableWalkerKingFog);
			this.bossInfoServer.setPlayBossMusic(true);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.spellHandler.addSpell(0, new EntityAIAntiAirSpellWalker(this));
		this.spellHandler.addSpell(1, new EntityAIWalkerIllusions(this, 600, 40));
		this.goalSelector.addGoal(15, new BossAIWalkerTornadoAttack(this));
		this.goalSelector.addGoal(16, new BossAIWalkerLightningCircles(this));
		this.goalSelector.addGoal(17, new BossAIWalkerLightningSpiral(this));
	}

	@Override
	public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, ILivingEntityData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void onLivingUpdate() {
		if (this.dragonAttackCooldown > 0) {
			this.dragonAttackCooldown--;
		}
		if (this.fallDistance > 12) {
			BlockPos teleportPos = null;
			boolean teleport = this.getTarget() != null || this.getHomePositionCQR() != null;
			if (this.getTarget() != null && !this.level.isClientSide) {
				Vector3d v = this.getTarget().getLookAngle();
				v = v.normalize();
				v = v.subtract(0, v.y, 0);
				v = v.scale(3);
				teleportPos = new BlockPos(this.getTarget().position().subtract(v));
				if (this.level.isBlockFullCube(teleportPos) || this.level.isBlockFullCube(teleportPos.relative(Direction.UP)) || this.level.isAirBlock(teleportPos.relative(Direction.DOWN))) {
					teleportPos = this.getTarget().blockPosition();
				}
			} else if (this.getHomePositionCQR() != null && !this.level.isClientSide) {
				teleportPos = this.getHomePositionCQR();
			}
			if (teleport) {
				// spawn cloud
				for (int ix = -1; ix <= 1; ix++) {
					for (int iz = -1; iz <= 1; iz++) {
						((ServerWorld) this.level).spawnParticle(ParticleTypes.SMOKE_LARGE, this.posX + ix, this.posY + 2, this.posZ + iz, 10, 0, 0, 0, 0.25, 0, 0, 0);
					}
				}
				this.level.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1, 1, true);
				this.attemptTeleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
			}
		}
		if (this.active && !this.level.isClientSide) {
			if (this.getTarget() == null) {
				this.activationCooldown--;
				if (this.activationCooldown < 0) {
					this.active = false;
					this.level.getWorldInfo().setThundering(false);
					this.activationCooldown = 80;
				}
			} else {
				this.level.getWorldInfo().setCleanWeatherTime(0);
				this.level.getWorldInfo().setRainTime(400);
				this.level.getWorldInfo().setThunderTime(200);
				this.level.getWorldInfo().setRaining(true);
				this.level.getWorldInfo().setThundering(true);
			}
			this.lightningTick++;
			if (this.lightningTick > this.borderLightning) {
				// strike lightning
				this.lightningTick = 0;
				this.borderLightning = 50;
				int x = -20 + this.getRandom().nextInt(41);
				int z = -20 + this.getRandom().nextInt(41);
				int y = -10 + this.getRandom().nextInt(21);

				EntityColoredLightningBolt entitybolt = new EntityColoredLightningBolt(this.world, this.posX + x, this.posY + y, this.posZ + z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
				this.level.addFreshEntity(entitybolt);
			}

			if (this.isInLava() && this.hasAttackTarget() && this.lavaCounterAttackCooldown <= 0) {
				this.teleportBehindEntity(this.getTarget());
				this.canAttack(this.getTarget());
				this.lavaCounterAttackCooldown = 20;
			}
			if (this.lavaCounterAttackCooldown > 0) {
				this.lavaCounterAttackCooldown--;
			}

			// Anti cobweb stuff
			if (this.isInWeb || this.level.getBlockState(this.blockPosition()).getBlock() instanceof WebBlock) {
				this.handleInWeb();
			}

		} else if (this.level.isRemote) {
			this.active = false;
		}
		super.onLivingUpdate();
	}

	private void handleInWeb() {
		if (this.hasAttackTarget()) {
			this.level.setBlockToAir(this.blockPosition());
			EntityWalkerKingIllusion illusion = new EntityWalkerKingIllusion(1200, this, this.getWorld());
			illusion.setPosition(this.posX, this.posY, this.posZ);
			this.level.addFreshEntity(illusion);

			this.teleportBehindEntity(this.getTarget());
			this.canAttack(this.getTarget());
		}
	}

	@Override
	public void onStruckByLightning(LightningBoltEntity lightningBolt) {
		this.heal(1F);
	}

	private void backStabAttacker(DamageSource source) {
		if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
			if (this.teleportBehindEntity(source.getEntity())) {
				this.canAttack((LivingEntity) source.getEntity());
			}
		}
	}

	private boolean teleportBehindEntity(Entity entity) {
		return this.teleportBehindEntity(entity, false);
	}

	private boolean teleportBehindEntity(Entity entity, boolean force) {
		Vector3d p = entity.position().subtract(entity.getLookAngle().scale(2 + (entity.width * 0.5)));
		if (this.getNavigation().isStableDestination(new BlockPos(p.x, p.y, p.z))) {
			for (int ix = -1; ix <= 1; ix++) {
				for (int iz = -1; iz <= 1; iz++) {
					((ServerWorld) this.level).spawnParticle(ParticleTypes.SMOKE_LARGE, this.posX + ix, this.posY + 2, this.posZ + iz, 10, 0, 0, 0, 0.25, 0, 0, 0);
				}
			}
			this.playSound(CQRSounds.WALKER_KING_LAUGH, 10.0F, 1.0F);
			this.level.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1, 1, true);
			if (force) {
				this.teleport(p.x, p.y, p.z);
				return true;
			}
			return this.attemptTeleport(p.x, p.y, p.z);
		}
		return false;
	}

	private void handleAttackedByDragon(Entity dragon) {
		if (CQRConfig.advanced.enableSpecialFeatures && dragon.getControllingPassenger() != null /* && (getRNG().nextInt(100) +1) > 95 */) {
			if (dragon instanceof MobEntity && dragon.getControllingPassenger() instanceof LivingEntity) {
				dragon.getControllingPassenger().unRide();
				// ((EntityLiving)dragon).setAttackTarget((EntityLivingBase) dragon.getControllingPassenger());
				/*
				 * if(dragon instanceof EntityTameable) { try { ((EntityTameable)dragon).setOwnerId(null); } catch(NullPointerException
				 * ex) {
				 * 
				 * } try { ((EntityTameable)dragon).setTamedBy(null); } catch(NullPointerException ex) {
				 * 
				 * } ((EntityTameable)dragon).setTamed(false); }
				 */
			}
		}

		// KILL IT!!!
		this.playSound(CQRSounds.WALKER_KING_LAUGH, 10.0F, 1.0F);
		int lightningCount = 6 + this.getRandom().nextInt(3);
		double angle = 360 / lightningCount;
		double dragonSize = dragon.width > dragon.height ? dragon.width : dragon.height;
		Vector3d v = new Vector3d(3 + (3 * dragonSize), 0, 0);
		for (int i = 0; i < lightningCount; i++) {
			Vector3d p = VectorUtil.rotateVectorAroundY(v, i * angle);
			int dY = -3 + this.getRandom().nextInt(7);
			EntityColoredLightningBolt clb = new EntityColoredLightningBolt(this.level, dragon.posX + p.x, dragon.posY + dY, dragon.posZ + p.z, false, false, 1F, 0.00F, 0.0F, 0.4F);
			this.level.spawnEntity(clb);
		}
		dragon.hurt(DamageSource.MAGIC, 10F);
	}

	private void handleActivation() {
		if (!this.level.isRemote && !this.level.getWorldInfo().isThundering()) {

			this.playSound(CQRSounds.WALKER_KING_LAUGH, 10.0F, 1.0F);

			this.active = true;
			this.activationCooldown = 80;
			this.level.getWorldInfo().setCleanWeatherTime(0);
			this.level.getWorldInfo().setRainTime(400);
			this.level.getWorldInfo().setThunderTime(200);
			this.level.getWorldInfo().setRaining(true);
			this.level.getWorldInfo().setThundering(true);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		if (source == DamageSource.WITHER) {
			this.heal(amount / 2);
			return true;
		}
		if (source == DamageSource.FALL) {
			return true;
		}

		if (source == DamageSource.IN_WALL && this.hasAttackTarget() && this.isServerWorld()) {
			EntityWalkerKingIllusion illusion = new EntityWalkerKingIllusion(1200, this, this.getWorld());
			illusion.setPosition(this.posX, this.posY, this.posZ);
			this.level.addFreshEntity(illusion);

			this.teleportBehindEntity(this.getTarget(), true);
			this.canAttack(this.getTarget());
			return false;
		}

		// Now handled by enchantment itself
		/*
		 * boolean spectralFlag = false;
		 * if (source.getTrueSource() instanceof EntityLivingBase) {
		 * if (EnchantmentHelper.getEnchantmentLevel(CQREnchantments.SPECTRAL, ((EntityLivingBase)
		 * source.getTrueSource()).getHeldItemMainhand()) > 0 ||
		 * EnchantmentHelper.getEnchantmentLevel(CQREnchantments.SPECTRAL, ((EntityLivingBase)
		 * source.getTrueSource()).getHeldItemOffhand()) > 0) {
		 * amount *= 2;
		 * spectralFlag = true;
		 * }
		 * }
		 */
		if (/* !spectralFlag && */((source.getDirectEntity() == null) || !(source.getDirectEntity() instanceof SpectralArrowEntity)) && !CQRConfig.bosses.armorForTheWalkerKing) {
			amount *= 0.5F;
		}

		if (source.getDirectEntity() != null) {
			if (source.getDirectEntity() instanceof SpectralArrowEntity) {
				amount *= 2;
				super.hurt(source, amount, sentFromPart);
				return true;
			}
			if ((source.getDirectEntity() instanceof ThrowableEntity || source.getDirectEntity() instanceof AbstractArrowEntity) && !this.level.isClientSide) {
				// STAB HIM IN THE BACK!!
				this.backStabAttacker(source);
				return false;
			}
		}

		this.handleActivation();

		if (source.getEntity() != null && !this.level.isClientSide) {
			ResourceLocation resLoc = EntityList.getKey(source.getEntity());
			if (resLoc != null) {
				// Start IceAndFire compatibility
				boolean flag = resLoc.getNamespace().equalsIgnoreCase("iceandfire") && CQRConfig.advanced.enableSpecialFeatures;
				if (flag) {
					amount /= 2;
				}
				// End IceAndFire compatibility

				Faction fac = FactionRegistry.instance(this).getFactionOf(source.getEntity());
				boolean dragonFactionFlag = fac != null && (fac.getName().equalsIgnoreCase("DRAGON") || fac.getName().equalsIgnoreCase("DRAGONS"));

				// If we are attacked by a dragon: KILL IT
				if (this.dragonAttackCooldown <= 0 && (dragonFactionFlag || resLoc.getPath().contains("dragon") || resLoc.getPath().contains("wyrm") || resLoc.getPath().contains("wyvern") || flag)) {
					this.dragonAttackCooldown = 80;
					this.handleAttackedByDragon(source.getEntity());
				}
			}
		}

		if (!this.level.isClientSide && source.getEntity() instanceof LivingEntity) {

			// How about killing the one who tries with the axe?
			// Maybe move this whole ability to the king shield itself??
			ItemStack shieldStack = this.getItemBySlot(EquipmentSlotType.OFFHAND);
			if (amount > 0F && this.canBlockDamageSource(source) && shieldStack != null && !shieldStack.isEmpty() && shieldStack.getItem() instanceof ShieldItem) {
				this.playSound(CQRSounds.WALKER_KING_LAUGH, 10.0F, 1.0F);
				if (source.getDirectEntity() instanceof LivingEntity/* && (source.getImmediateSource() instanceof EntityPlayer) */ && ((LivingEntity) source.getDirectEntity()).getMainHandItem().getItem() instanceof AxeItem) {
					if (DungeonGenUtils.percentageRandom(0.75, this.getRandom())) {
						Vector3d v = source.getDirectEntity().position().subtract(this.position()).normalize().scale(1.25);
						v = v.add(0, 0.75, 0);

						LivingEntity attacker = (LivingEntity) source.getDirectEntity();
						attacker.motionX = v.x;
						attacker.motionY = v.y;
						attacker.motionZ = v.z;
						attacker.velocityChanged = true;
						this.swing(Hand.OFF_HAND);

						return false;
					}
				}
			}

			if (this.getRandom().nextDouble() < 0.2 && source.getEntity() != null) {
				// Revenge Attack
				if (this.getRandom().nextDouble() < 0.7) {
					this.canAttack((LivingEntity) source.getEntity());
					this.playSound(CQRSounds.WALKER_KING_LAUGH, 10.0F, 1.0F);
					this.teleportBehindEntity(source.getEntity());
				}
			}
		}
		return super.hurt(source, amount, sentFromPart);
	}

	@Override
	public boolean canBlockDamageSource(DamageSource damageSourceIn) {
		if (super.canBlockDamageSource(damageSourceIn)) {
			if (this.getRandom().nextDouble() < 0.3) {
				return true;
			}
			if (this.getRandom().nextDouble() < 0.1) {
				// Attack back
				this.counterAttack();
			}
		}
		return false;
	}

	private void counterAttack() {
		this.counterAttack(this.getTarget());
	}

	private void counterAttack(Entity entitylivingbase) {
		double d0 = Math.min(entitylivingbase.posY, this.posY);
		double d1 = Math.max(entitylivingbase.posY, this.posY) + 1.0D;
		float f = (float) MathHelper.atan2(entitylivingbase.posZ - this.posZ, entitylivingbase.posX - this.posX);
		for (int i = 0; i < 5; ++i) {
			float f1 = f + i * (float) Math.PI * 0.4F;
			this.spawnFangs(this.posX + MathHelper.cos(f1) * 1.5D, this.posZ + MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
		}

		for (int k = 0; k < 8; ++k) {
			float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
			this.spawnFangs(this.posX + MathHelper.cos(f2) * 2.5D, this.posZ + MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
		}

		for (int k = 0; k < 11; ++k) {
			float f2 = f + k * (float) Math.PI * 2.0F / 11.0F + ((float) Math.PI * 2F / 5F);
			this.spawnFangs(this.posX + MathHelper.cos(f2) * 3.5D, this.posZ + MathHelper.sin(f2) * 4.5D, d0, d1, f2, 6);
		}
	}

	private void spawnFangs(double x, double z, double minY, double maxY, float rotationYawRadians, int warmupDelayTicks) {
		BlockPos blockpos = new BlockPos(x, maxY, z);
		boolean flag = false;
		double d0 = 0.0D;

		while (true) {
			if (!this.level.isBlockNormalCube(blockpos, true) && this.level.isBlockNormalCube(blockpos.below(), true)) {
				if (!this.level.isAirBlock(blockpos)) {
					BlockState iblockstate = this.level.getBlockState(blockpos);
					AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.level, blockpos);

					if (axisalignedbb != null) {
						d0 = axisalignedbb.maxY;
					}
				}

				flag = true;
				break;
			}

			blockpos = blockpos.below();

			if (blockpos.getY() < MathHelper.floor(minY) - 1) {
				break;
			}
		}

		if (flag) {
			EntityIceSpike entityevokerfangs = new EntityIceSpike(this.level, x, blockpos.getY() + d0, z, rotationYawRadians, warmupDelayTicks, this);
			this.level.addFreshEntity(entityevokerfangs);
		}
	}

	@Override
	public boolean hasCape() {
		return this.deathTime <= 0;
	}

	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_WALKER;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.AbyssWalkerKing;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.WALKERS;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.WALKER_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.WALKER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.WALKER_KING_DEATH_EFFECT;
	}

	@Override
	protected SoundEvent getFinalDeathSound() {
		return CQRSounds.WALKER_KING_DEATH;
	}

	@Override
	protected float getSoundPitch() {
		return 0.75F * super.getSoundPitch();
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);

		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, this.getSword());
		this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.SHIELD_WALKER_KING, 1));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING, 3));

		this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(CQRItems.KING_CROWN, 1));

		// Give him some armor...
		if (CQRConfig.bosses.armorForTheWalkerKing) {
			CompoundNBT nbttagcompound = new CompoundNBT();
			CompoundNBT nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", 0x9000FF);
			ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) CQRItems.CHESTPLATE_DIAMOND_DYABLE).setColor(chest, 0x9000FF);
			this.setItemStackToSlot(EquipmentSlotType.CHEST, chest);

			ItemStack legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) CQRItems.LEGGINGS_DIAMOND_DYABLE).setColor(legs, 0x9000FF);
			this.setItemStackToSlot(EquipmentSlotType.LEGS, legs);

			ItemStack boobs = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) CQRItems.BOOTS_DIAMOND_DYABLE).setColor(boobs, 0x9000FF);
			this.setItemStackToSlot(EquipmentSlotType.FEET, boobs);
		}
	}

	private ItemStack getSword() {
		ItemStack sword = new ItemStack(CQRItems.SWORD_WALKER, 1);

		/*
		 * for(int i = 0; i < 1 + getRNG().nextInt(3 * (world.getDifficulty().ordinal() +1)); i++) { sword =
		 * EnchantmentHelper.addRandomEnchantment(getRNG(), sword, 20
		 * + getRNG().nextInt(41), true); }
		 */
		sword = EnchantmentHelper.enchantItem(this.getRandom(), sword, 30, true);
		if (!EnchantmentHelper.hasVanishingCurse(sword)) {
			sword.enchant(Enchantments.VANISHING_CURSE, 1);
		}

		return sword;
	}

	@Override
	public void die(DamageSource cause) {
		this.level.getWorldInfo().setThundering(false);
		super.die(cause);
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if (!this.level.isClientSide && this.level.getGameRules().getBoolean("doMobLoot")) {
			if (this.deathTime > 150 && this.deathTime % 5 == 0) {
				this.dropExperience(MathHelper.floor(50F));
			}
		}
	}

	@Override
	protected void onFinalDeath() {
		if (!this.level.isClientSide && this.level.getGameRules().getBoolean("doMobLoot")) {
			this.dropExperience(MathHelper.floor(1200));
		}
	}

	@Override
	protected boolean usesEnderDragonDeath() {
		return true;
	}

	@Override
	protected boolean doesExplodeOnDeath() {
		return false;
	}

	@Override
	protected IParticleData getDeathAnimParticles() {
		return ParticleTypes.EXPLOSION_HUGE;
	}

	@Override
	protected int getExperiencePoints(PlayerEntity player) {
		return super.getExperiencePoints(player);
	}

	private void dropExperience(int p_184668_1_) {
		while (p_184668_1_ > 0) {
			int i = ExperienceOrbEntity.getXPSplit(p_184668_1_);
			p_184668_1_ -= i;
			this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.posX, this.posY, this.posZ, i));
		}
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CQRCreatureAttributes.VOID;
	}

}
