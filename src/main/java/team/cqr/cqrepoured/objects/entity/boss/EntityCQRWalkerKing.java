package team.cqr.cqrepoured.objects.entity.boss;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQREnchantments;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.Capes;
import team.cqr.cqrepoured.objects.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerLightningCircles;
import team.cqr.cqrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerLightningSpiral;
import team.cqr.cqrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerTornadoAttack;
import team.cqr.cqrepoured.objects.entity.ai.boss.walkerking.EntityAIWalkerIllusions;
import team.cqr.cqrepoured.objects.entity.ai.spells.EntityAIAntiAirSpellWalker;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.objects.entity.misc.EntityIceSpike;
import team.cqr.cqrepoured.objects.items.armor.ItemArmorDyable;
import team.cqr.cqrepoured.util.CQRConfig;
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
			this.bossInfoServer.setCreateFog(true);
			this.bossInfoServer.setDarkenSky(true);
			this.bossInfoServer.setPlayEndBossMusic(true);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(12D);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.spellHandler.addSpell(0, new EntityAIAntiAirSpellWalker(this));
		this.spellHandler.addSpell(1, new EntityAIWalkerIllusions(this, 600, 40));
		this.tasks.addTask(15, new BossAIWalkerTornadoAttack(this));
		this.tasks.addTask(16, new BossAIWalkerLightningCircles(this));
		this.tasks.addTask(17, new BossAIWalkerLightningSpiral(this));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
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
			boolean teleport = this.getAttackTarget() != null || this.getHomePositionCQR() != null;
			if (this.getAttackTarget() != null && !this.world.isRemote) {
				Vec3d v = this.getAttackTarget().getLookVec();
				v = v.normalize();
				v = v.subtract(0, v.y, 0);
				v = v.scale(3);
				teleportPos = new BlockPos(this.getAttackTarget().getPositionVector().subtract(v));
				if (this.world.isBlockFullCube(teleportPos) || this.world.isBlockFullCube(teleportPos.offset(EnumFacing.UP)) || this.world.isAirBlock(teleportPos.offset(EnumFacing.DOWN))) {
					teleportPos = this.getAttackTarget().getPosition();
				}
			} else if (this.getHomePositionCQR() != null && !this.world.isRemote) {
				teleportPos = this.getHomePositionCQR();
			}
			if (teleport) {
				// spawn cloud
				for (int ix = -1; ix <= 1; ix++) {
					for (int iz = -1; iz <= 1; iz++) {
						((WorldServer) this.world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + ix, this.posY + 2, this.posZ + iz, 10, 0, 0, 0, 0.25, 0, 0, 0);
					}
				}
				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1, 1, true);
				this.attemptTeleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
			}
		}
		if (this.active && !this.world.isRemote) {
			if (this.getAttackTarget() == null) {
				this.activationCooldown--;
				if (this.activationCooldown < 0) {
					this.active = false;
					this.world.getWorldInfo().setThundering(false);
					this.activationCooldown = 80;
				}
			} else {
				this.world.getWorldInfo().setCleanWeatherTime(0);
				this.world.getWorldInfo().setRainTime(400);
				this.world.getWorldInfo().setThunderTime(200);
				this.world.getWorldInfo().setRaining(true);
				this.world.getWorldInfo().setThundering(true);
			}
			this.lightningTick++;
			if (this.lightningTick > this.borderLightning) {
				// strike lightning
				this.lightningTick = 0;
				this.borderLightning = 50;
				int x = -20 + this.getRNG().nextInt(41);
				int z = -20 + this.getRNG().nextInt(41);
				int y = -10 + this.getRNG().nextInt(21);

				EntityColoredLightningBolt entitybolt = new EntityColoredLightningBolt(this.world, this.posX + x, this.posY + y, this.posZ + z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
				this.world.spawnEntity(entitybolt);
			}

			if (this.isInLava() && this.hasAttackTarget() && this.lavaCounterAttackCooldown <= 0) {
				this.teleportBehindEntity(this.getAttackTarget());
				this.attackEntityAsMob(this.getAttackTarget());
				this.lavaCounterAttackCooldown = 20;
			}
			if (this.lavaCounterAttackCooldown > 0) {
				this.lavaCounterAttackCooldown--;
			}

		} else if (this.world.isRemote) {
			this.active = false;
		}
		super.onLivingUpdate();
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		this.heal(1F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return super.attackEntityFrom(source, amount);
	}

	private void backStabAttacker(DamageSource source) {
		if (source.getTrueSource() != null) {
			if (this.teleportBehindEntity(source.getTrueSource())) {
				this.attackEntityAsMob(source.getTrueSource());
			}
		}
	}

	private boolean teleportBehindEntity(Entity entity) {
		Vec3d p = entity.getPositionVector().subtract(entity.getLookVec().scale(2 + (entity.width * 0.5)));
		if (this.getNavigator().canEntityStandOnPos(new BlockPos(p.x, p.y, p.z))) {
			for (int ix = -1; ix <= 1; ix++) {
				for (int iz = -1; iz <= 1; iz++) {
					((WorldServer) this.world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + ix, this.posY + 2, this.posZ + iz, 10, 0, 0, 0, 0.25, 0, 0, 0);
				}
			}
			this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1, 1, true);
			return this.attemptTeleport(p.x, p.y, p.z);
		}
		return false;
	}

	private void handleAttackedByDragon(Entity dragon) {
		if (CQRConfig.advanced.enableSpecialFeatures && dragon.getControllingPassenger() != null /* && (getRNG().nextInt(100) +1) > 95 */) {
			if (dragon instanceof EntityLiving && dragon.getControllingPassenger() instanceof EntityLivingBase) {
				dragon.getControllingPassenger().dismountRidingEntity();
				// ((EntityLiving)dragon).setAttackTarget((EntityLivingBase) dragon.getControllingPassenger());
				/*
				 * if(dragon instanceof EntityTameable) { try { ((EntityTameable)dragon).setOwnerId(null); } catch(NullPointerException ex) {
				 * 
				 * } try { ((EntityTameable)dragon).setTamedBy(null); } catch(NullPointerException ex) {
				 * 
				 * } ((EntityTameable)dragon).setTamed(false); }
				 */
			}
		}

		// KILL IT!!!
		int lightningCount = 6 + this.getRNG().nextInt(3);
		double angle = 360 / lightningCount;
		double dragonSize = dragon.width > dragon.height ? dragon.width : dragon.height;
		Vec3d v = new Vec3d(3 + (3 * dragonSize), 0, 0);
		for (int i = 0; i < lightningCount; i++) {
			Vec3d p = VectorUtil.rotateVectorAroundY(v, i * angle);
			int dY = -3 + this.getRNG().nextInt(7);
			EntityColoredLightningBolt clb = new EntityColoredLightningBolt(this.world, dragon.posX + p.x, dragon.posY + dY, dragon.posZ + p.z, false, false, 1F, 0.00F, 0.0F, 0.4F);
			this.world.spawnEntity(clb);
		}
		dragon.attackEntityFrom(DamageSource.MAGIC, 10F);
	}

	private void handleActivation() {
		if (!this.world.isRemote && !this.world.getWorldInfo().isThundering()) {
			this.active = true;
			this.activationCooldown = 80;
			this.world.getWorldInfo().setCleanWeatherTime(0);
			this.world.getWorldInfo().setRainTime(400);
			this.world.getWorldInfo().setThunderTime(200);
			this.world.getWorldInfo().setRaining(true);
			this.world.getWorldInfo().setThundering(true);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		if (source == DamageSource.WITHER) {
			this.heal(amount / 2);
			return true;
		}
		if (source == DamageSource.FALL) {
			return true;
		}

		boolean spectralFlag = false;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			if (EnchantmentHelper.getEnchantmentLevel(CQREnchantments.SPECTRAL, ((EntityLivingBase) source.getTrueSource()).getHeldItemMainhand()) > 0 || EnchantmentHelper.getEnchantmentLevel(CQREnchantments.SPECTRAL, ((EntityLivingBase) source.getTrueSource()).getHeldItemOffhand()) > 0) {
				amount *= 2;
				spectralFlag = true;
			}
		}
		if (!spectralFlag && !(source.getImmediateSource() != null && source.getImmediateSource() instanceof EntitySpectralArrow) && (CQRConfig.bosses.harderWalkerKing && !CQRConfig.bosses.armorForTheWalkerKing)) {
			amount *= 0.5F;
		}

		if (source.getImmediateSource() != null) {
			if (source.getImmediateSource() instanceof EntitySpectralArrow) {
				amount *= 2;
				super.attackEntityFrom(source, amount, sentFromPart);
				return true;
			}
			if (source.getImmediateSource() instanceof EntityThrowable && !this.world.isRemote) {
				// STAB HIM IN THE BACK!!
				this.backStabAttacker(source);
				return false;
			}
		}

		this.handleActivation();

		if (source.getTrueSource() != null && !this.world.isRemote) {
			ResourceLocation resLoc = EntityList.getKey(source.getTrueSource());
			if (resLoc != null) {
				// Start IceAndFire compatibility
				boolean flag = resLoc.getNamespace().equalsIgnoreCase("iceandfire") && CQRConfig.advanced.enableSpecialFeatures;
				if (flag) {
					amount /= 2;
				}
				// End IceAndFire compatibility

				CQRFaction fac = FactionRegistry.instance().getFactionOf(source.getTrueSource());
				boolean dragonFactionFlag = fac != null && (fac.getName().equalsIgnoreCase("DRAGON") || fac.getName().equalsIgnoreCase("DRAGONS"));

				// If we are attacked by a dragon: KILL IT
				if (this.dragonAttackCooldown <= 0 && (dragonFactionFlag || resLoc.getPath().contains("dragon") || resLoc.getPath().contains("wyrm") || resLoc.getPath().contains("wyvern") || flag)) {
					this.dragonAttackCooldown = 80;
					this.handleAttackedByDragon(source.getTrueSource());
				}
			}
		}

		if (CQRConfig.bosses.harderWalkerKing && !this.world.isRemote) {
			if (this.getRNG().nextDouble() < 0.2 && source.getTrueSource() != null) {
				// Revenge Attack
				if (this.getRNG().nextDouble() < 0.7) {
					this.attackEntityAsMob(source.getTrueSource());
					this.teleportBehindEntity(source.getTrueSource());
				}
			}
		}
		return super.attackEntityFrom(source, amount, sentFromPart);
	}

	@Override
	public boolean canBlockDamageSource(DamageSource damageSourceIn) {
		if (!CQRConfig.bosses.harderWalkerKing) {
			return super.canBlockDamageSource(damageSourceIn);
		}
		if (super.canBlockDamageSource(damageSourceIn)) {
			if (this.getRNG().nextDouble() < 0.3) {
				return true;
			}
			if (this.getRNG().nextDouble() < 0.1) {
				// Attack back
				this.counterAttack();
			}
		}
		return false;
	}

	private void counterAttack() {
		this.counterAttack(this.getAttackTarget());
	}

	private void counterAttack(Entity entitylivingbase) {
		double d0 = Math.min(entitylivingbase.posY, this.posY);
		double d1 = Math.max(entitylivingbase.posY, this.posY) + 1.0D;
		float f = (float) MathHelper.atan2(entitylivingbase.posZ - this.posZ, entitylivingbase.posX - this.posX);
		for (int i = 0; i < 5; ++i) {
			float f1 = f + (float) i * (float) Math.PI * 0.4F;
			this.spawnFangs(this.posX + (double) MathHelper.cos(f1) * 1.5D, this.posZ + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
		}

		for (int k = 0; k < 8; ++k) {
			float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
			this.spawnFangs(this.posX + (double) MathHelper.cos(f2) * 2.5D, this.posZ + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
		}

		for (int k = 0; k < 11; ++k) {
			float f2 = f + (float) k * (float) Math.PI * 2.0F / 11.0F + ((float) Math.PI * 2F / 5F);
			this.spawnFangs(this.posX + (double) MathHelper.cos(f2) * 3.5D, this.posZ + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, 6);
		}
	}

	private void spawnFangs(double x, double z, double minY, double maxY, float rotationYawRadians, int warmupDelayTicks) {
		BlockPos blockpos = new BlockPos(x, maxY, z);
		boolean flag = false;
		double d0 = 0.0D;

		while (true) {
			if (!this.world.isBlockNormalCube(blockpos, true) && this.world.isBlockNormalCube(blockpos.down(), true)) {
				if (!this.world.isAirBlock(blockpos)) {
					IBlockState iblockstate = this.world.getBlockState(blockpos);
					AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

					if (axisalignedbb != null) {
						d0 = axisalignedbb.maxY;
					}
				}

				flag = true;
				break;
			}

			blockpos = blockpos.down();

			if (blockpos.getY() < MathHelper.floor(minY) - 1) {
				break;
			}
		}

		if (flag) {
			EntityIceSpike entityevokerfangs = new EntityIceSpike(this.world, x, (double) blockpos.getY() + d0, z, rotationYawRadians, warmupDelayTicks, this);
			this.world.spawnEntity(entityevokerfangs);
		}
	}

	@Override
	public boolean hasCape() {
		return this.deathTicks <= 0;
	}

	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_WALKER;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_WALKER_KING;
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
		return SoundEvents.ENTITY_WITHER_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WITHER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITHER_DEATH;
	}

	@Override
	protected SoundEvent getFinalDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.getSword());
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_WALKER_KING, 1));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING, 3));

		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(CQRItems.KING_CROWN, 1));

		// Give him some armor...
		if (CQRConfig.bosses.armorForTheWalkerKing) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", 9437439);
			ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) CQRItems.CHESTPLATE_DIAMOND_DYABLE).setColor(chest, 9437439);
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);

			ItemStack legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) CQRItems.LEGGINGS_DIAMOND_DYABLE).setColor(legs, 9437439);
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, legs);

			ItemStack boobs = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE, 1, 0, nbttagcompound);
			((ItemArmorDyable) CQRItems.BOOTS_DIAMOND_DYABLE).setColor(boobs, 9437439);
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, boobs);
		}
	}

	private ItemStack getSword() {
		ItemStack sword = new ItemStack(CQRItems.SWORD_WALKER, 1);

		/*
		 * for(int i = 0; i < 1 + getRNG().nextInt(3 * (world.getDifficulty().ordinal() +1)); i++) { sword = EnchantmentHelper.addRandomEnchantment(getRNG(), sword, 20
		 * + getRNG().nextInt(41), true); }
		 */
		sword = EnchantmentHelper.addRandomEnchantment(this.getRNG(), sword, 30, true);
		if (!EnchantmentHelper.hasVanishingCurse(sword)) {
			sword.addEnchantment(Enchantments.VANISHING_CURSE, 1);
		}

		return sword;
	}

	@Override
	public void onDeath(DamageSource cause) {
		this.world.getWorldInfo().setThundering(false);
		super.onDeath(cause);
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot")) {
			if (this.deathTicks > 150 && this.deathTicks % 5 == 0) {
				this.dropExperience(MathHelper.floor((float) 50F));
			}
		}
	}

	@Override
	protected void onFinalDeath() {
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot")) {
			this.dropExperience(MathHelper.floor((float) 1200));
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
	protected EnumParticleTypes getDeathAnimParticles() {
		return EnumParticleTypes.EXPLOSION_HUGE;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return super.getExperiencePoints(player);
	}

	private void dropExperience(int p_184668_1_) {
		while (p_184668_1_ > 0) {
			int i = EntityXPOrb.getXPSplit(p_184668_1_);
			p_184668_1_ -= i;
			this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
		}
	}
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return CQRCreatureAttributes.CREATURE_TYPE_ABYSS_WALKER;
	}

}
