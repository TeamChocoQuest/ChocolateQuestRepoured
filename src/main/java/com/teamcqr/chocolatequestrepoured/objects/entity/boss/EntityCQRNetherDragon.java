package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.init.CQRSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAICircleAroundLocation;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAIFlyToLocation;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAIFlyToTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAISpiralUpToCirclingCenter;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator.MoveHelperDirectFlight;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator.PathNavigateDirectLine;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAINetherDragonNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHotFireball;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.EntityUtil;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
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
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class EntityCQRNetherDragon extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob {

	/**
	 * AI: Circle around about 30 blocks above your home location in a radius of ~30 blocks
	 * 
	 * If you see a player: Charge at it, bite it, fly in a 22.5° angle upwards until you flew 5 blocks up Then begin spiraling up to your "strafing y", there you
	 * fly 0.5 - 3 rounds on your circle and attack again While you are circling, you may
	 * change to a higher, thinner circler, about 10 blocks above the normal. You fly up to it by spiraling up or down, whilst charging at the player you may spit
	 * fire or shoot fireballs
	 */

	public final int INITIAL_SEGMENT_COUNT = 18;
	public final int SEGMENT_COUNT_ON_DEATH = 4;
	public int segmentCount = this.INITIAL_SEGMENT_COUNT;
	/*
	 * 0: Normal mode 1: Transition to phase 2 2: skeletal phase
	 */
	private int phase = 0;
	private int phaseChangeTimer = 0;
	private float damageTmpPhaseTwo = CQRConfig.bosses.netherDragonStageTwoSegmentHP;
	private int fireballTimer = 240;
	private int mouthTimer = 0;
	boolean deathPhaseEnd = false;

	private EntityCQRNetherDragonSegment[] dragonBodyParts = new EntityCQRNetherDragonSegment[this.INITIAL_SEGMENT_COUNT];

	// private boolean mouthOpen = false;
	private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SKELE_COUNT = EntityDataManager.<Integer>createKey(EntityCQRNetherDragon.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> PHASE = EntityDataManager.<Integer>createKey(EntityCQRNetherDragon.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SPIT_FIRE = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SERVER_PART_LENGTH = EntityDataManager.<Integer>createKey(EntityCQRNetherDragon.class, DataSerializers.VARINT);

	// AI stuff
	private Vec3d targetLocation = null;
	private boolean flyingUp = false;

	private static ArrayList<ResourceLocation> breakableBlocks = new ArrayList<>();

	/*
	 * Notes: This dragon is meant to "swim" through the skies, it moves like a snake, so the model needs animation, also the parts are meant to move like the parts
	 * from Twilight Forests Naga
	 * 
	 * Also the nether dragon destroys all blocks in its hitbox, if these are not lava, also if the block it moved through are leaves or burnable, it will set them
	 * on fire It will also break obsidian blocks, but not command blocks or structure blocks
	 * or bedrock
	 */

	public EntityCQRNetherDragon(World worldIn) {
		super(worldIn);
		this.experienceValue = 100;
		this.noClip = true;

		this.ignoreFrustumCheck = true;

		// Init the body parts
		this.initBody();

		this.moveHelper = new MoveHelperDirectFlight(this);
		this.moveParts();
	}

	public static void reloadBreakableBlocks() {
		breakableBlocks.clear();
		for (String s : CQRConfig.bosses.netherDragonBreakableBlocks) {
			ResourceLocation rs = new ResourceLocation(s);
			breakableBlocks.add(rs);
		}
	}

	public void setFlyingUp(boolean value) {
		this.flyingUp = value;
	}

	public boolean isFlyingUp() {
		return this.flyingUp;
	}

	private void initBody() {
		if (this.segmentCount < 0) {
			this.segmentCount = this.INITIAL_SEGMENT_COUNT;
		}

		this.dragonBodyParts = new EntityCQRNetherDragonSegment[this.segmentCount];
		for (int i = 0; i < this.dragonBodyParts.length; i++) {
			this.dragonBodyParts[i] = new EntityCQRNetherDragonSegment(this, i + 1, false);
			this.world.spawnEntity(this.dragonBodyParts[i]);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(1.5D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.25D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public float getDefaultWidth() {
		return 2.0F;
	}

	@Override
	public float getDefaultHeight() {
		return 2.0F;
	}

	@Override
	protected boolean usesEnderDragonDeath() {
		return false;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(MOUTH_OPEN, false);
		this.dataManager.register(SKELE_COUNT, -1);
		this.dataManager.register(PHASE, this.phase);
		this.dataManager.register(SPIT_FIRE, false);
		this.dataManager.register(SERVER_PART_LENGTH, this.segmentCount);
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		if (this.phase == 0) {
			damage = damage / 4.0F + Math.min(damage, 1.0F);
			if (damage >= this.getHealth()) {
				this.phase++;
				if (!this.world.isRemote) {
					this.dataManager.set(PHASE, this.phase);
				}
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				this.setHealth(this.getMaxHealth() - 1);
				damage = 0;
				return false;
			}
		}
		if (this.phase == 1) {
			return false;
		}

		if (this.phase == 2) {
			this.damageTmpPhaseTwo -= damage;
			if (this.damageTmpPhaseTwo <= 0) {
				this.damageTmpPhaseTwo = CQRConfig.bosses.netherDragonStageTwoSegmentHP;
				// DONE: Remove last segment
				damage = this.getMaxHealth() / (this.INITIAL_SEGMENT_COUNT - this.SEGMENT_COUNT_ON_DEATH);
				this.setHealth(this.getHealth() - damage);
				if (damage >= this.getHealth()) {
					super.attackEntityFrom(source, damage + 1, true);
				}
			}
			this.updateSegmentCount();
			return super.attackEntityFrom(source, 0, true);
		}
		if (this.phase == 0) {
			return this.attackEntityFrom(source, damage, true);
		}

		return super.attackEntityFrom(source, damage, true);
	}

	private void removeLastSegment() {
		int indx = Math.max(0, this.dragonBodyParts.length - 1);
		EntityCQRNetherDragonSegment segment = this.dragonBodyParts[indx];
		if (indx > 0) {
			EntityCQRNetherDragonSegment[] partsTmp = new EntityCQRNetherDragonSegment[this.dragonBodyParts.length - 1];
			for (int i = 0; i < partsTmp.length; i++) {
				partsTmp[i] = this.dragonBodyParts[i];
			}
			this.dragonBodyParts = partsTmp;
		} else {
			this.dragonBodyParts = new EntityCQRNetherDragonSegment[0];
		}
		segment.die();
		if (!this.world.isRemote) {
			segment.onRemovedFromBody();
		}
		// world.removeEntityDangerously(segment);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.canHarmInCreative()) {
			return super.attackEntityFrom(source, amount);
		}

		if (source.isFireDamage() || source.isExplosion()) {
			return false;
		}

		// Phase change
		if (this.phase == 0 && amount >= this.getHealth()) {
			if (!this.world.isRemote) {
				this.phase++;
				this.dataManager.set(PHASE, this.phase);
				this.setHealth(this.getMaxHealth() - 1);
			}
			this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
			// DONE: Init phase 2!!
			amount = 0;
			return false;
		} else if (this.phase != 0 && amount > 0) {
			// Play blaze sound
			this.playSound(SoundEvents.ENTITY_BLAZE_HURT, 2F, 1.5F);
			return false;
		}

		return super.attackEntityFrom(source, amount, false);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(5, new BossAIFlyToTarget(this));
		this.tasks.addTask(8, new BossAIFlyToLocation(this));
		this.tasks.addTask(10, new BossAISpiralUpToCirclingCenter(this));
		this.tasks.addTask(12, new BossAICircleAroundLocation(this));

		this.targetTasks.addTask(0, new EntityAINetherDragonNearestAttackTarget(this));
		this.targetTasks.addTask(2, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.NetherDragon;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if (this.mouthTimer > 0 || this.world.isRemote) {
			return;
		}
		if (this.getRNG().nextDouble() < 0.4) {
			// Shoot fireball
			this.mouthTimer = 10;

			Vec3d velocity = target.getPositionVector().subtract(this.getPositionVector());
			velocity = velocity.normalize().scale(1.5);
			ProjectileHotFireball proj = new ProjectileHotFireball(this.world, this, this.posX + velocity.x, this.posY + velocity.y, this.posZ + velocity.z);
			// proj.setPosition(this.posX + velocity.x, this.posY + velocity.y, this.posZ + velocity.z);
			proj.motionX = velocity.x;
			proj.motionY = velocity.y;
			proj.motionZ = velocity.z;
			proj.velocityChanged = true;
			this.world.spawnEntity(proj);
		} else {
			// Spit fire
			this.mouthTimer = 160;
			this.dataManager.set(SPIT_FIRE, true);
		}
	}

	public void setBreathingFireFlag(boolean value) {
		if (value) {
			this.mouthTimer = 100;
		} else {
			this.mouthTimer = 10;
		}
		this.dataManager.set(SPIT_FIRE, value);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (super.attackEntityAsMob(entityIn)) {
			if (this.phase > 1 && (entityIn instanceof EntityLivingBase)) {
				((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 100 + entityIn.world.getDifficulty().ordinal() * 40, 3));
			}
			if (!this.world.isRemote) {
				this.mouthTimer = 5;
			}
			return true;
		}
		return false;
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// Unused?
	}

	// This code is not entirely made by me, it is oriented from this:
	// https://github.com/TeamTwilight/twilightforest/blob/1.12.x/src/main/java/twilightforest/entity/boss/EntityTFNaga.java
	protected void moveParts() {
		for (int i = 0; i < this.dragonBodyParts.length; i++) {
			if (this.dragonBodyParts[i] == null) {
				continue;
			}
			Entity leader = i == 0 ? this : this.dragonBodyParts[i - 1];
			if (leader == null) {
				continue;
			}

			double headerX = leader.posX;
			double headerY = leader.posY;
			double headerZ = leader.posZ;

			float angle = (((leader.rotationYaw + 180) * new Float(Math.PI)) / 180F);

			double straightDegree = 0.05D + (1.0 / (float) (i + 1)) * 0.5D;

			double calculatedRotatedX = -MathHelper.sin(angle) * straightDegree;
			double calculatedRotatedZ = MathHelper.cos(angle) * straightDegree;

			double x = this.dragonBodyParts[i].posX;
			double y = this.dragonBodyParts[i].posY;
			double z = this.dragonBodyParts[i].posZ;

			Vec3d deltaPos = new Vec3d(x - headerX, y - headerY, z - headerZ);
			deltaPos = deltaPos.normalize();

			deltaPos = deltaPos.add(new Vec3d(calculatedRotatedX, 0, calculatedRotatedZ).normalize());

			// Dont change these values, they are important for the correct allignment of the segments!!!
			double f = i != 0 ? 0.378D : 0.338D;

			double targetX = headerX + f * deltaPos.x;
			double targetY = headerY + f * deltaPos.y;
			double targetZ = headerZ + f * deltaPos.z;

			// Set rotated position
			this.dragonBodyParts[i].setPosition(targetX, targetY, targetZ);

			double distance = (double) MathHelper.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z);
			// Finally apply the new rotation -> Rotate the block
			this.dragonBodyParts[i].setRotation((float) (Math.atan2(deltaPos.z, deltaPos.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(deltaPos.y, distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		this.destroyBlocksInAABB(this.getEntityBoundingBox().grow(0.5).offset(new Vec3d(this.motionX, this.motionY, this.motionZ).scale(1.5)));
		for (EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if (segment != null) {
				this.destroyBlocksInAABB(segment.getEntityBoundingBox());
			}
		}

		this.fireballTimer--;
		if (!this.world.isRemote && this.phase > 1 && this.fireballTimer <= 0) {
			this.fireballTimer = CQRConfig.bosses.netherDragonStageTwoFireballInterval;
			this.shootFireballFromBody();
		}

		if (this.dataManager.get(SPIT_FIRE)) {
			this.breatheFire();
		}

	}

	@Override
	public double getAttackReach(EntityLivingBase target) {
		return super.getAttackReach(target) * this.INITIAL_SEGMENT_COUNT;
	}

	public void shootFireballFromBody() {
		if (this.dragonBodyParts != null && this.dragonBodyParts.length > 0) {
			int indx = this.getRNG().nextInt(this.dragonBodyParts.length);
			while (this.dragonBodyParts[indx] == null) {
				indx = this.getRNG().nextInt(this.dragonBodyParts.length);
			}
			Entity pre = indx == 0 ? this : this.dragonBodyParts[indx - 1];
			Vec3d v = this.dragonBodyParts[indx].getPositionVector();
			if (this.hasAttackTarget() && this.getRNG().nextDouble() > 0.6) {
				v = this.getAttackTarget().getPositionVector().subtract(v).add(0, 0.5, 0);
			} else {
				v = pre.getPositionVector().subtract(v);
				v = v.add(new Vec3d(0, 1 - (2 * this.getRNG().nextDouble()), 0));
				if (this.getRNG().nextBoolean()) {
					v = VectorUtil.rotateVectorAroundY(v, 45);
					int angle = this.getRNG().nextInt(61);
					v = VectorUtil.rotateVectorAroundY(v, angle);
				} else {
					v = VectorUtil.rotateVectorAroundY(v, -45);
					int angle = -this.getRNG().nextInt(61);
					v = VectorUtil.rotateVectorAroundY(v, angle);
				}
			}

			v = v.normalize();
			ProjectileHotFireball proj = new ProjectileHotFireball(this.world, this, this.dragonBodyParts[indx].posX + v.x, this.dragonBodyParts[indx].posY + v.y, this.dragonBodyParts[indx].posZ + v.z);
			v = v.scale(1.5);
			proj.motionX = v.x;
			proj.motionY = v.y;
			proj.motionZ = v.z;
			proj.velocityChanged = true;
			this.world.spawnEntity(proj);
		}
	}

	public void breatheFire() {
		double motionX, motionZ;
		Vec3d look = this.getLookVec();
		motionX = look.x;
		motionZ = look.z;
		Vec3d flameStartPos = this.getPositionVector().add((new Vec3d(motionX, 0, motionZ).scale((this.width / 2) - 0.25).subtract(0, 0.2, 0)));
		flameStartPos = flameStartPos.add(0, this.height / 2, 0);
		Vec3d v = new Vec3d(motionX, 0, motionZ).scale(0.75);
		double ANGLE_MAX = 22.5;
		double MAX_LENGTH = 24;
		double angle = ANGLE_MAX / MAX_LENGTH;
		double dY = -0.05;
		v = VectorUtil.rotateVectorAroundY(v, -(ANGLE_MAX / 2));
		if (this.world.isRemote) {
			for (int i = 0; i <= MAX_LENGTH; i++) {
				for (int iY = 0; iY <= 10; iY++) {
					Vec3d vOrig = v;
					v = new Vec3d(v.x, -0.15 + iY * dY, v.z).scale(1.5);

					this.world.spawnParticle(EnumParticleTypes.FLAME, true, flameStartPos.x, flameStartPos.y, flameStartPos.z, v.x * 0.5, v.y * 0.5, v.z * 0.5);
					this.world.spawnParticle(EnumParticleTypes.FLAME, true, flameStartPos.x, flameStartPos.y, flameStartPos.z, v.x, v.y, v.z);

					v = vOrig;
				}
				v = VectorUtil.rotateVectorAroundY(v, angle);
			}
		} else {
			v = new Vec3d(motionX, 0, motionZ).scale(0.75);
			double angleTan = Math.tan(Math.toRadians(ANGLE_MAX / 2D));
			MAX_LENGTH *= 1.25;
			int count = 9;
			double currentLength = MAX_LENGTH / count;
			double lengthIncr = currentLength;
			for (int i = 0; i < count; i++) {
				double r = angleTan * currentLength;
				// System.out.println("R=" + r);
				Vec3d v2 = new Vec3d(v.x, -0.15 + (5 * -0.05), v.z).scale(currentLength - r);
				Vec3d pCenter = flameStartPos.add(v2);
				AxisAlignedBB aabb = new AxisAlignedBB(pCenter.x - r, pCenter.y - r, pCenter.z - r, pCenter.x + r, pCenter.y + r, pCenter.z + r).shrink(0.25);
				for (Entity ent : this.world.getEntitiesInAABBexcluding(this, aabb, TargetUtil.createPredicateNonAlly(this.getFaction()))) {
					ent.setFire(8);
					ent.attackEntityFrom(DamageSource.ON_FIRE, 5);
				}

				// DEBUG TO SEE THE "ZONE"
				/*
				 * for(BlockPos p : BlockPos.getAllInBox((int)aabb.minX, (int)aabb.minY, (int)aabb.minZ, (int)aabb.maxX, (int)aabb.maxY, (int)aabb.maxZ)){
				 * world.setBlockState(p, Blocks.GLASS.getDefaultState()); }
				 */

				currentLength += lengthIncr;
			}
		}
	}

	// Copied from ender dragon
	private boolean destroyBlocksInAABB(AxisAlignedBB aabb) {
		if (!CQRConfig.bosses.netherDragonDestroysBlocks || this.isDead || (this.getWorld().getGameRules().hasRule("mobGriefing") && !this.getWorld().getGameRules().getBoolean("mobGriefing")) || this.world.isRemote) {
			return false;
		}

		int x1 = MathHelper.floor(aabb.minX);
		int y1 = MathHelper.floor(aabb.minY);
		int z1 = MathHelper.floor(aabb.minZ);
		int x2 = MathHelper.floor(aabb.maxX);
		int y2 = MathHelper.floor(aabb.maxY);
		int z2 = MathHelper.floor(aabb.maxZ);

		boolean cancelled = false;
		boolean blockDestroyed = false;

		for (int k1 = x1; k1 <= x2; ++k1) {
			for (int l1 = y1; l1 <= y2; ++l1) {
				for (int i2 = z1; i2 <= z2; ++i2) {
					BlockPos blockpos = new BlockPos(k1, l1, i2);
					IBlockState iblockstate = this.world.getBlockState(blockpos);
					Block block = iblockstate.getBlock();

					if (!block.isAir(iblockstate, this.world, blockpos) && iblockstate.getMaterial() != Material.FIRE) {
						if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
							cancelled = true;
						}
						// Check if the entity can destroy the blocks -> Event that can be cancelled by e.g. anti griefing mods or the protection system
						else if (net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate)) {
							boolean container = block.hasTileEntity(iblockstate) && block.createTileEntity(this.world, iblockstate).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
							if (breakableBlocks.contains(block.getRegistryName()) && !container && block.isCollidable() && block != Blocks.BEDROCK && block != Blocks.STRUCTURE_BLOCK && block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK
									&& block != Blocks.END_GATEWAY && block != Blocks.END_PORTAL && block != Blocks.PORTAL && block != CQRBlocks.PHYLACTERY && block != CQRBlocks.FORCE_FIELD_NEXUS && block != CQRBlocks.EXPORTER) {
								blockDestroyed = this.world.setBlockToAir(blockpos) || blockDestroyed;
							} else {
								cancelled = true;
							}
						} else {
							cancelled = true;
						}
					}
				}
			}
		}

		if (blockDestroyed) {
			double x = aabb.minX + (aabb.maxX - aabb.minX) * (double) this.rand.nextFloat();
			double y = aabb.minY + (aabb.maxY - aabb.minY) * (double) this.rand.nextFloat();
			double z = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double) this.rand.nextFloat();

			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0.0D, 0.0D, 0.0D);
		}

		return cancelled;
	}

	@Override
	public void onUpdate() {
		if (this.phase == 1 && !this.world.isRemote) {
			this.phaseChangeTimer--;
			if (this.phaseChangeTimer <= 0) {
				this.phaseChangeTimer = 2;
				for (int i = 0; i < this.dragonBodyParts.length; i++) {
					if (!this.dragonBodyParts[i].isSkeletal()) {
						this.dragonBodyParts[i].switchToSkeletalState();
						if (!this.world.isRemote) {
							if (i == 0) {
								this.dataManager.set(SKELE_COUNT, 1);
							} else {
								this.dataManager.set(SKELE_COUNT, i);
							}
						}
						break;
					}
				}
			}

			if (this.dragonBodyParts[this.dragonBodyParts.length - 1].isSkeletal()) {
				this.dataManager.set(SKELE_COUNT, this.dragonBodyParts.length + 1);
				this.phase++;
				if (!this.world.isRemote) {
					this.dataManager.set(PHASE, this.phase);
				}
			}
		}

		if (this.world.isRemote && this.dataManager.get(PHASE) > this.phase) {
			this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
			this.phase++;
		}

		if (!this.world.isRemote && this.mouthTimer > 0) {
			this.mouthTimer--;
			this.dataManager.set(MOUTH_OPEN, this.mouthTimer > 0);
			if (this.mouthTimer <= 0 && this.dataManager.get(SPIT_FIRE)) {
				this.dataManager.set(SPIT_FIRE, false);
			}
		}

		if (this.world.isRemote && this.firstUpdate && this.dragonBodyParts.length > this.getSegmentCount()) {
			this.updateSegmentCount();
		}

		if (this.world.isRemote) {
			this.lengthSyncClient();
		} else {
			if (this.phase > 1) {
				this.updateSegmentCount();
			}
			this.lengthSyncServer();
		}

		super.onUpdate();

		// update bodySegments parts
		for (EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if (segment != null) {
				this.world.updateEntityWithOptionalForce(segment, true);
				if (this.phase == 2 && !segment.isSkeletal() && !this.world.isRemote) {
					segment.switchToSkeletalState();
				}
			}
		}

		this.moveParts();

	}

	private void lengthSyncServer() {
		this.dataManager.set(SERVER_PART_LENGTH, this.dragonBodyParts.length);
	}

	private void lengthSyncClient() {
		int serverLength = this.dataManager.get(SERVER_PART_LENGTH);
		if (serverLength > 0 && serverLength < this.dragonBodyParts.length) {
			EntityCQRNetherDragonSegment[] partsTmp = new EntityCQRNetherDragonSegment[serverLength];
			for (int i = 0; i < this.dragonBodyParts.length; i++) {
				if (i < partsTmp.length) {
					partsTmp[i] = this.dragonBodyParts[i];
				} else {
					this.world.removeEntity(this.dragonBodyParts[i]);
				}
			}
			this.dragonBodyParts = partsTmp;
		}
	}

	private void updateSegmentCount() {
		if (!this.world.isRemote) {
			double divisor = this.getMaxHealth() / (this.INITIAL_SEGMENT_COUNT - this.SEGMENT_COUNT_ON_DEATH);
			int actualSegmentCount = (int) Math.floor(this.getHealth() / divisor) + this.SEGMENT_COUNT_ON_DEATH;
			if (actualSegmentCount < (this.dragonBodyParts.length - 1)) {
				this.removeLastSegment();
			}
			this.segmentCount = this.dragonBodyParts.length;
		}
	}

	public int getSkeleProgress() {
		return this.dataManager.get(SKELE_COUNT);
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.NETHER_DRAGON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.NETHER_DRAGON_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	public void setDead() {
		super.setDead();
		for (EntityCQRNetherDragonSegment dragonPart : this.dragonBodyParts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			if (dragonPart != null) {
				dragonPart.onRemovedFromBody();
			}
		}
	}

	@Override
	public Entity[] getParts() {
		return this.dragonBodyParts;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfoServer.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfoServer.removePlayer(player);
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateDirectLine(this, worldIn) {
			@Override
			public float getPathSearchRange() {
				return 128.0F;
			}
		};
	}

	@Override
	public int getHealingPotions() {
		return 0;
	}

	public void setMouthOpen(boolean open) {
		this.dataManager.set(MOUTH_OPEN, open);
	}

	public boolean isMouthOpen() {
		return this.dataManager.get(MOUTH_OPEN);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeBoolean(this.dataManager.get(MOUTH_OPEN));
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.dataManager.set(MOUTH_OPEN, additionalData.readBoolean());
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_DRAGON_NETHER;
	}

	@Override
	public boolean canOpenDoors() {
		return false;
	}

	@Override
	public boolean canPutOutFire() {
		return false;
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}

	@Override
	protected void onDeathUpdate() {
		++this.deathTicks;

		if (this.deathTicks % 5 == 0) {
			if (this.dragonBodyParts.length > 0) {
				EntityCQRNetherDragonSegment segment = this.dragonBodyParts[this.dragonBodyParts.length - 1];
				if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot")) {
					this.dropExperience(MathHelper.floor((float) 120), segment.posX, segment.posY, segment.posZ);
					this.world.createExplosion(segment, segment.posX, segment.posY, segment.posZ, 1, false);
					this.removeLastSegment();
				}
			} else {
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				this.onFinalDeath();
				this.setDead();
			}
		}
	}

	private void dropExperience(int p_184668_1_, double x, double y, double z) {
		while (p_184668_1_ > 0) {
			int i = EntityXPOrb.getXPSplit(p_184668_1_);
			p_184668_1_ -= i;
			EntityXPOrb xp = new EntityXPOrb(this.world, x, y, z, i);
			xp.setEntityInvulnerable(true);
			this.world.spawnEntity(xp);
		}
	}

	public int getSegmentCount() {
		return this.segmentCount;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("segmentCount", this.segmentCount);
		compound.setInteger("phase", this.phase);
		compound.setInteger("skeleCount", this.getSkeleProgress());

		// AI stuff
		if (this.targetLocation != null) {
			compound.setTag("targetLocation", VectorUtil.createVectorNBTTag(this.targetLocation));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("segmentCount")) {
			this.segmentCount = compound.getInteger("segmentCount");
			this.dataManager.set(SERVER_PART_LENGTH, this.segmentCount);
		}
		if (compound.hasKey("skeleCount")) {
			this.dataManager.set(SKELE_COUNT, compound.getInteger("skeleCount"));
		}
		if (compound.hasKey("phase")) {
			this.phase = compound.getInteger("phase");

			this.dataManager.set(PHASE, this.phase);
		}

		// AI stuff
		if (compound.hasKey("targetLocation")) {
			this.targetLocation = VectorUtil.getVectorFromTag(compound.getCompoundTag("targetLocation"));
		}
	}

	@Override
	protected void onFinalDeath() {
		for (EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot")) {
				this.dropExperience(MathHelper.floor((float) 120), segment.posX, segment.posY, segment.posZ);
			}
			this.world.createExplosion(segment, segment.posX, segment.posY, segment.posZ, 1, false);
			this.world.removeEntityDangerously(segment);
		}
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot")) {
			this.dropExperience(MathHelper.floor((float) 800), this.posX, this.posY, this.posZ);
		}
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1, false);
	}

	@Override
	public EnumParticleTypes getDeathAnimParticles() {
		return EnumParticleTypes.LAVA;
	}

	public BlockPos getCirclingCenter() {
		if (this.getHomePositionCQR() == null) {
			this.setHomePositionCQR(this.getPosition());
		}
		return this.getHomePositionCQR();
	}

	// Methods from entity flying
	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
	}

	@Override
	public void travel(float strafe, float vertical, float forward) {
		EntityUtil.move3D(this, strafe, vertical, forward, this.getMoveHelper().getSpeed(), this.rotationYaw, this.rotationPitch);
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9;
		this.motionY *= 0.9;
		this.motionZ *= 0.9;
		this.velocityChanged = true;
	}

	/**
	 * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or for AI reasons)
	 */
	@Override
	public boolean isOnLadder() {
		return false;
	}

	// AI stuff
	@Nullable
	public Vec3d getTargetLocation() {
		return this.targetLocation;
	}

	public void setTargetLocation(Vec3d newTarget) {
		this.targetLocation = newTarget;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return this.phase > 0 ? EnumCreatureAttribute.UNDEAD : EnumCreatureAttribute.UNDEFINED;
	}

}
