package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAICircleAroundLocation;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator.MoveHelperDirectFlight;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator.PathNavigateDirectLine;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHotFireball;
import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

	public enum EDragonMovementState {
		CHARGING,
		FLYING,
		// When it is flying up or down, it will spiral up or down
		FLYING_UPWARDS,
		FLYING_DOWNWARDS
	}

	public enum ENetherDragonAttacks {
		SPIT_FIRE, FIREBALL, LIGHTNINGS, BITE
	}

	/**
	 * AI:
	 * Circle around about 30 blocks above your home location in a radius of ~30 blocks
	 * 
	 * If you see a player: Charge at it, bite it, fly in a 22.5° angle upwards until you flew 5 blocks up
	 * Then begin spiraling up to your "strafing y", there you fly 0.5 - 3 rounds on your circle and attack again
	 * While you are circling, you may change to a higher, thinner circler, about 10 blocks above the normal.
	 * You fly up to it by spiraling up or down, whilst charging at the player you may spit fire or shoot fireballs
	 */

	public final int INITIAL_SEGMENT_COUNT = 18;
	public int segmentCount = INITIAL_SEGMENT_COUNT;
	/*
	 * 0: Normal mode
	 * 1: Transition to phase 2
	 * 2: skeletal phase
	 */
	private int phase = 0;
	private int phaseChangeTimer = 0;
	private float damageTmpPhaseTwo = 40;
	private int fireballTimer = 240;
	private int mouthTimer = 0;
	boolean deathPhaseEnd = false;

	private EDragonMovementState movementState = EDragonMovementState.FLYING;

	private EntityCQRNetherDragonSegment[] dragonBodyParts = new EntityCQRNetherDragonSegment[0]; 

	// private boolean mouthOpen = false;
	private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SKELE_COUNT = EntityDataManager.<Integer>createKey(EntityCQRNetherDragon.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> PHASE_INCREASED = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragon.class, DataSerializers.BOOLEAN);

	private boolean isReadyToAttack = true;

	/*
	 * Notes: This dragon is meant to "swim" through the skies, it moves like a snake, so the model needs animation, also the parts are meant to move like the parts from Twilight Forests Naga
	 * 
	 * Also the nether dragon destroys all blocks in its hitbox, if these are not lava, also if the block it moved through are leaves or burnable, it will set them on fire
	 * It will also break obsidian blocks, but not command blocks or structure blocks or bedrock
	 */

	public EntityCQRNetherDragon(World worldIn) {
		super(worldIn);
		this.experienceValue = 100;
		this.noClip = true;

		this.ignoreFrustumCheck = true;
		
		// Init the body parts
		initBody();
		
		this.moveHelper = new MoveHelperDirectFlight(this);
		moveParts();
	}
	
	private void initBody() {
		if(this.segmentCount < 0) {
			this.segmentCount = 18;
		}
		this.dragonBodyParts = new EntityCQRNetherDragonSegment[this.segmentCount];
		for (int i = 0; i < this.dragonBodyParts.length; i++) {
			this.dragonBodyParts[i] = new EntityCQRNetherDragonSegment(this, i + 1, false);
			world.spawnEntity(this.dragonBodyParts[i]);
		}
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(1.5D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.25D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2.0D);
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
		this.dataManager.register(PHASE_INCREASED, false);
	}
	
	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		if(this.phase == 0) {
			damage = damage / 4.0F + Math.min(damage, 1.0F);
			if(damage >= this.getHealth()) {
				this.phase++;
				this.dataManager.set(PHASE_INCREASED, true);
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				this.setHealth(this.getMaxHealth() -1);
				damage = 0;
				return false;
			}
		}
		if(this.phase == 1) {
			return false;
		}
		
		if(this.phase == 2) {
			damageTmpPhaseTwo -= damage;
			if(damageTmpPhaseTwo <= 0) {
				damageTmpPhaseTwo = 40;
				//DONE: Remove last segment
				damage = this.getMaxHealth() / (this.getSegmentCount() -2);
				this.setHealth(getHealth() - damage);
				if(damage >= this.getHealth()) {
					super.attackEntityFrom(source, damage +1, true);
				}
			}
			updateSegmentCount();
			return super.attackEntityFrom(source, 0, true);
		}
		if(this.phase == 0) {
			return attackEntityFrom(source, damage, true);
		}

		return super.attackEntityFrom(source, damage, true);
	}
	
	private void removeLastSegment() {
		int indx = Math.max(0, this.dragonBodyParts.length -1);
		EntityCQRNetherDragonSegment segment = this.dragonBodyParts[indx];
		if(indx > 0) {
			EntityCQRNetherDragonSegment[] partsTmp = new EntityCQRNetherDragonSegment[this.dragonBodyParts.length -1];
			for(int i = 0; i < partsTmp.length; i++) {
				partsTmp[i] = this.dragonBodyParts[i];
			}
			this.dragonBodyParts = partsTmp;
		} else {
			this.dragonBodyParts = new EntityCQRNetherDragonSegment[0];
		}
		segment.die();
		world.removeEntityDangerously(segment);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isFireDamage() || source.isExplosion()) {
			return false;
		}
		
		//Phase change
		if(this.phase == 0 && amount >= this.getHealth()) {
			this.phase++;
			this.dataManager.set(PHASE_INCREASED, true);
			this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
			//DONE: Init phase 2!!
			this.setHealth(this.getMaxHealth() -1);
			amount = 0;
			return false;
		} else if (phase != 0 && amount > 0) {
			//Play blaze sound
			playSound(SoundEvents.ENTITY_BLAZE_HURT, 2F, 1.5F);
			return false;
		}

		return super.attackEntityFrom(source, amount, false);
	}
	
	@Override
	protected void initEntityAI() {
		//this.tasks.addTask(5, new net.minecraft.entity.ai.EntityAIAttackRanged(this, 1.1, 30, 60, 40));
		//this.tasks.addTask(6, new BossAIChargeAtTarget(this));
		//this.tasks.addTask(7, new BossAIFlyToLocation(this));
		//this.tasks.addTask(8, new BossAISpiralUpOrDown(this));
		//this.tasks.addTask(10, new EntityAIAttack(this));
		//this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(18, new BossAICircleAroundLocation(this));
		//this.tasks.addTask(20, new BossAIFlyRandomly(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.NETHER_DRAGON.getValue();
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
		// TODO: Shoot fireball OR spit fire if close enough
		double distance = this.getDistance(target);

		if (distance > 25) {
			// Shoot fireball
		} else {
			// Spit fire
			this.setMouthOpen(true);
		}
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if(super.attackEntityAsMob(entityIn)) {
			if(this.phase > 1 && (entityIn instanceof EntityLivingBase)) {
				((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 60 + entityIn.world.getDifficulty().ordinal() * 20));
			}
			if(!this.world.isRemote) {
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
			if(dragonBodyParts[i] == null) {
				continue;
			}
			Entity leader = i == 0 ? this : this.dragonBodyParts[i - 1];
			if(leader == null) {
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
		
		/*if(this.firstUpdate) {
			initBody();
		}*/
		
		this.destroyBlocksInAABB(this.getEntityBoundingBox().grow(1.25).offset(new Vec3d(motionX, motionY, motionZ).scale(1.5)));
		for(EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if(segment != null) {
				destroyBlocksInAABB(segment.getEntityBoundingBox());
			}
		}
		
		this.fireballTimer--;
		if(!this.world.isRemote && this.phase > 1 && this.fireballTimer <= 0) {
			this.fireballTimer = 240;
			int indx = getRNG().nextInt(this.dragonBodyParts.length);
			while(this.dragonBodyParts[indx] == null) {
				indx = getRNG().nextInt(this.dragonBodyParts.length);
			}
			Entity pre = indx == 0 ? this : this.dragonBodyParts[indx -1];
			Vec3d v = pre.getPositionVector().subtract(this.dragonBodyParts[indx].getPositionVector());
			v = v.add(new Vec3d(0, 0.125 - (0.25 * getRNG().nextDouble()), 0));
			
			if(getRNG().nextBoolean()) {
				v = VectorUtil.rotateVectorAroundY(v, 45);
				int angle = getRNG().nextInt(61);
				v = VectorUtil.rotateVectorAroundY(v, angle);
			} else {
				v = VectorUtil.rotateVectorAroundY(v, -45);
				int angle = -getRNG().nextInt(61);
				v = VectorUtil.rotateVectorAroundY(v, angle);
			}
			v = v.normalize();
			ProjectileHotFireball proj = new ProjectileHotFireball(world, this.dragonBodyParts[indx].posX + v.x, this.dragonBodyParts[indx].posY + v.y, this.dragonBodyParts[indx].posZ + v.z);
			v = v.scale(0.5);
			proj.motionX = v.x;
			proj.motionY = v.y;
			proj.motionZ = v.z;
			proj.velocityChanged = true;
			world.spawnEntity(proj);
		}

		// TODO: Attack stuff -> in updateAI

	}

	// Copied from ender dragon
	private boolean destroyBlocksInAABB(AxisAlignedBB aabb) {
		if (this.isDead || (this.getWorld().getGameRules().hasRule("mobGriefing") && !this.getWorld().getGameRules().getBoolean("mobGriefing")) || this.world.isRemote) {
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
							boolean container = block.hasTileEntity(iblockstate) && block.createTileEntity(world,iblockstate).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
							if (!container && block.isCollidable() && block != Blocks.BEDROCK && block != Blocks.STRUCTURE_BLOCK && block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS
									&& block != Blocks.END_GATEWAY) {
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
		if(this.phase == 1 && !world.isRemote) {
			this.phaseChangeTimer--;
			if(this.phaseChangeTimer <= 0) {
				this.phaseChangeTimer = 2;
				for(int i = 0; i < this.dragonBodyParts.length; i++) {
					if(!this.dragonBodyParts[i].isSkeletal()) {
						this.dragonBodyParts[i].switchToSkeletalState();
						if(!world.isRemote) {
							if(i == 0) {
								this.dataManager.set(SKELE_COUNT, 1);
							} else {
								this.dataManager.set(SKELE_COUNT, i);
							}
						}
						break;
					}
				}
			}
			
			if(this.dragonBodyParts[this.dragonBodyParts.length -1].isSkeletal()) {
				this.dataManager.set(SKELE_COUNT, this.dragonBodyParts.length +1);
				this.phase++;
				this.dataManager.set(PHASE_INCREASED, true);
			}
		}
		
		if(world.isRemote && this.dataManager.get(PHASE_INCREASED)) {
			this.dataManager.set(PHASE_INCREASED, false);
			this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
			this.phase++;
		}
		
		if(!world.isRemote && mouthTimer > 0) {
			mouthTimer--;
			this.dataManager.set(MOUTH_OPEN, mouthTimer > 0);
		}
		
		if(world.isRemote && firstUpdate && this.dragonBodyParts.length > this.getSegmentCount()) {
			updateSegmentCount();
		}

		if(this.phase > 1) {
			updateSegmentCount();
		}
		
		super.onUpdate();
		
		// update bodySegments parts
		for (EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if(segment != null) {
				this.world.updateEntityWithOptionalForce(segment, true);
				if(this.phase == 2 && !segment.isSkeletal() && !world.isRemote) {
					segment.switchToSkeletalState();
				}
			}
		}

 		this.moveParts();
			
	}
	
	private void updateSegmentCount() {
		double divisor = this.getMaxHealth() / (INITIAL_SEGMENT_COUNT -2);
		int actualSegmentCount = (int) Math.floor(getHealth() / divisor); 
		if(actualSegmentCount < (this.dragonBodyParts.length -1 -2)) {
			removeLastSegment();
		}
		this.segmentCount = this.dragonBodyParts.length;
	}

	public int getSkeleProgress() {
		return this.dataManager.get(SKELE_COUNT);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return ModSounds.NETHER_DRAGON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSounds.NETHER_DRAGON_DEATH;
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
			if(dragonPart != null) {
				this.world.removeEntityDangerously(dragonPart);
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

	public EDragonMovementState getCurrentMovementState() {
		return this.movementState;
	}

	public void updateMovementState(EDragonMovementState charging) {
		this.movementState = charging;
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

	public void startAttack(ENetherDragonAttacks attackType) {
		if (this.isReadyToAttack) {
			this.setMouthOpen(true);
		}
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLootTableList.ENTITIES_DRAGON_NETHER;
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
		this.deathTime = deathTicks * (20 / 600);
		super.onDeathUpdate();
		double distC = this.getDistanceSq(getCirclingCenter());
		
		distC = Math.sqrt(distC);
		if(this.deathTicks >= 600) {
			this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
			this.setDead();
			onFinalDeath();
		}
		else if(distC > 12) {
			Vec3d center = new Vec3d(getCirclingCenter().getX(), getCirclingCenter().getY(), getCirclingCenter().getZ());
			this.getLookHelper().setLookPosition(center.x, center.y, center.z, 90, 90);
			//this.getMoveHelper().setMoveTo(center.x, center.y, center.z, 0.75);
			Vec3d v = center.subtract(getPositionVector()).normalize().scale(0.4);
			this.motionX = v.x;
			this.motionY = v.y;
			this.motionZ = v.z;
			this.velocityChanged = true;
		} else {
			this.noClip = false;
			if(this.onGround || this.posY <= getCirclingCenter().getY() || this.deathTicks >= 600) {
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				onFinalDeath();
				this.setDead();
			} else {
				this.setMoveVertical(-1);
			}
		}
	}
	
	private void dropExperience(int p_184668_1_, double x, double y, double z)
    {
        while (p_184668_1_ > 0)
        {
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
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.segmentCount = compound.getInteger("segmentCount");
		this.dataManager.set(SKELE_COUNT, compound.getInteger("skeleCount"));
		this.phase = compound.getInteger("phase");
	}

	@Override
	protected void onFinalDeath() {
		for(EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot"))
	        {
	            this.dropExperience(MathHelper.floor((float)120), segment.posX, segment.posY, segment.posZ);
	        }
			world.createExplosion(segment, segment.posX, segment.posY, segment.posZ, 1, false);
			world.removeEntityDangerously(segment);
		}
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot"))
        {
            this.dropExperience(MathHelper.floor((float)800), posX, posY, posZ);
        }
		world.createExplosion(this, this.posX, this.posY, this.posZ, 1, false);
	}
	
	@Override
	public EnumParticleTypes getDeathAnimParticles() {
		return EnumParticleTypes.LAVA;
	}
	
	public BlockPos getCirclingCenter() {
		if(this.getHomePositionCQR() == null) {
			this.setHomePositionCQR(getPosition());
		}
		return this.getHomePositionCQR();
	}
	
	//Methods from entity flying
	@Override
	public void fall(float distance, float damageMultiplier)
    {
    }
	
	@Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
    }
	
	@Override
    public void travel(float strafe, float vertical, float forward)
    {
        if (this.isInWater())
        {
            this.moveRelative(strafe, vertical, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.isInLava())
        {
            this.moveRelative(strafe, vertical, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float f = 0.91F;

            if (this.onGround)
            {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(strafe, vertical, forward, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (this.onGround)
            {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)f;
            this.motionY *= (double)f;
            this.motionZ *= (double)f;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isOnLadder()
    {
        return false;
    }
    
}
