package team.cqr.cqrepoured.entity.misc;

import java.util.List;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntityColoredLightningBolt extends LightningBoltEntity implements IEntityAdditionalSpawnData {

	/** Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc. */
	private int lightningState;
	/** Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time. */
	private int boltLivingTime;
	protected boolean hitEntities;
	protected boolean spreadFire;
	public float red;
	public float green;
	public float blue;
	public float alpha;

	public EntityColoredLightningBolt(World worldIn) {
		this(CQREntityTypes.COLORED_LIGHTNING.get(), worldIn, 0.0D, 0.0D, 0.0D, false, false);
	}

	public EntityColoredLightningBolt(World worldIn, double x, double y, double z, boolean hitEntities, boolean spreadFire) {
		this(CQREntityTypes.COLORED_LIGHTNING.get(), worldIn, x, y, z, hitEntities, spreadFire, 0.45F, 0.45F, 0.5F, 0.3F);
	}
	
	public EntityColoredLightningBolt(EntityType<? extends EntityColoredLightningBolt> type, World worldIn) {
		this(worldIn, 0.0D, 0.0D, 0.0D, false, false);
	}

	public EntityColoredLightningBolt(EntityType<? extends EntityColoredLightningBolt> type, World worldIn, double x, double y, double z, boolean hitEntities, boolean spreadFire) {
		this(worldIn, x, y, z, hitEntities, spreadFire, 0.45F, 0.45F, 0.5F, 0.3F);
	}

	/**
	 * Vanilla color is: 0.45F, 0.45F, 0.5F, 0.3F
	 */
	public EntityColoredLightningBolt(World worldIn, double x, double y, double z, boolean hitEntities, boolean spreadFire, float red, float green, float blue, float alpha) {
		this(CQREntityTypes.COLORED_LIGHTNING.get(), worldIn, x, y, z, hitEntities, spreadFire, red, green, blue, alpha);
	}
	
	public EntityColoredLightningBolt(EntityType<? extends EntityColoredLightningBolt> type, World worldIn, double x, double y, double z, boolean hitEntities, boolean spreadFire, float red, float green, float blue, float alpha) {
		super(type, worldIn/*, x, y, z, true*/);
		this.setPos(x, y, z);
		//this.isImmuneToFire = true;
		this.noCulling = true;
		this.lightningState = 2;
		this.boltLivingTime = this.random.nextInt(3) + 1;
		this.hitEntities = hitEntities;
		this.spreadFire = spreadFire;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		BlockPos blockpos = this.blockPosition();

		if (spreadFire && !worldIn.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && (worldIn.getDifficulty() == Difficulty.NORMAL || worldIn.getDifficulty() == Difficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10)) {
			 BlockState fireBlockState = AbstractFireBlock.getState(this.level, blockpos);
			if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && fireBlockState.canSurvive(worldIn, blockpos)) {
				worldIn.setBlockAndUpdate(blockpos, Blocks.FIRE.defaultBlockState());
			}

			for (int i = 0; i < 4; i++) {
				BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

				if (worldIn.getBlockState(blockpos1).getMaterial() == Material.AIR && fireBlockState.canSurvive(worldIn, blockpos1)) {
					worldIn.setBlockAndUpdate(blockpos1, Blocks.FIRE.defaultBlockState());
				}
			}
		}
	}

	/*@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}*/

	@Override
	public void tick() {
		if (!this.level.isClientSide) {
			this.setSharedFlag(6, this.isGlowing());
		}

		this.baseTick();

		if (this.lightningState == 2) {
			this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
	         this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
		}

		--this.lightningState;

		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.remove();
			} else if (this.lightningState < -this.random.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;

				if (this.spreadFire && !this.level.isClientSide) {
					this.seed = this.random.nextLong();
					BlockPos blockpos = this.blockPosition();

					if (this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && this.level.isAreaLoaded(blockpos, 10) && this.level.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.defaultBlockState().canSurvive(this.level, blockpos)) {
						this.level.setBlockAndUpdate(blockpos, Blocks.FIRE.defaultBlockState());
					}
				}
			}
		}

		if (this.lightningState >= 0) {
			if (this.level.isClientSide) {
				this.level.setSkyFlashTime(2);
			} else if (this.hitEntities) {
				AxisAlignedBB aabb = new AxisAlignedBB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D);
				List<Entity> list = this.level.getEntities(this, aabb);

				for (Entity entity : list) {
					if (!ForgeEventFactory.onEntityStruckByLightning(entity, this)) {
						if (CQRConfig.advanced.flyingCowardPenaltyEnabled && (EntityUtil.isEntityFlying(entity) || EntityUtil.isEntityFlying(entity.getControllingPassenger()))) {
							entity.hurt(DamageSource.MAGIC, (float) CQRConfig.advanced.flyingCowardPenaltyDamage);
						}
						entity.thunderHit((ServerWorld)this.level, this);
					}
				}
			}
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("red", this.red);
		compound.putFloat("green", this.green);
		compound.putFloat("blue", this.blue);
		compound.putFloat("alpha", this.alpha);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.red = compound.getFloat("red");
		this.green = compound.getFloat("green");
		this.blue = compound.getFloat("blue");
		this.alpha = compound.getFloat("alpha");
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeFloat(this.red);
		buffer.writeFloat(this.green);
		buffer.writeFloat(this.blue);
		buffer.writeFloat(this.alpha);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.red = additionalData.readFloat();
		this.green = additionalData.readFloat();
		this.blue = additionalData.readFloat();
		this.alpha = additionalData.readFloat();
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
