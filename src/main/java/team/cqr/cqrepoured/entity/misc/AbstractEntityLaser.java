package team.cqr.cqrepoured.entity.misc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.ISizable;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.util.math.BoundingBox;

public abstract class AbstractEntityLaser extends Entity implements IEntityAdditionalSpawnData {

	public LivingEntity caster;
	public float length;
	public float rotationYawCQR;
	public float rotationPitchCQR;
	public float prevRotationYawCQR;
	public float prevRotationPitchCQR;
	public float serverRotationYawCQR;
	public float serverRotationPitchCQR;
	private final Object2IntMap<LivingEntity> hitInfoMap = new Object2IntOpenHashMap<>();
	private final Map<BlockPos, BreakingInfo> blockBreakMap = new HashMap<>();

	private static class BreakingInfo {

		private static int counter;
		private int lastTimeHit;
		private float progress;
		private int id = counter++ % 256;

	}

	protected AbstractEntityLaser(EntityType<? extends AbstractEntityLaser> type, Level worldIn) {
		this(type, worldIn, null, 4.0F);
	}

	protected AbstractEntityLaser(EntityType<? extends AbstractEntityLaser> type, Level worldIn, LivingEntity caster, float length) {
		super(type, worldIn);
		this.caster = caster;
		this.length = length;
		this.noCulling = true;
		this.noPhysics = true;
	}

	public Vec3 getOffsetVector() {
		if (this.caster == null) return Vec3.ZERO;
		Vec3 v = new Vec3(0.0D, this.caster.getBbHeight() * 0.6D / (caster instanceof ISizable ? ((ISizable) caster).getSizeVariation() : 1), 0.0D);
		v = v.add(this.caster.getLookAngle().scale(0.25D));
		return v;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true; //distance < 64.0D * 64.0D;
	}
	
	
	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		
	}
	
	@Override
	public void load(CompoundTag pCompound) {
	}
	
	@Override
	public boolean save(CompoundTag pCompound) {
		return false;
	}

	public double laserEffectRadius() {
		return 0.25D;
	}

	@Override
	public void baseTick() {
		if (!this.level().isClientSide && !this.caster.isAlive()) {
			this.discard();
		}

		super.baseTick();

		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;

		if (this.level().isClientSide) {
			this.rotationYawCQR = this.serverRotationYawCQR;
			this.rotationPitchCQR = this.serverRotationPitchCQR;
		} else {
			this.updatePositionAndRotation();
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new SPacketSyncLaserRotation(this));
		}

		if (!this.level().isClientSide) {
			Vec3 start = this.position();
			Vec3 end = start.add(Vec3.directionFromRotation(this.rotationPitchCQR, this.rotationYawCQR).scale(this.length));
			ClipContext rtc = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
			HitResult result = this.level().clip(rtc);//this.level.rayTraceBlocks(start, end, false, false, false);
			double d = result != null ? (float) result.getLocation().subtract(this.position()).length() : this.length;

			if (result != null) {
				BlockPos pos = BlockPos.containing(result.getLocation());
				BlockState state = this.level().getBlockState(pos);
				if (this.canHitBlock(pos, state)) {
					float breakProgress = this.onHitBlock(pos, state);
					if (breakProgress > 0.0F) {
						if (breakProgress >= 1.0F) {
							// destroy block
							this.level().destroyBlock(pos, true);
						} else {
							BreakingInfo breakingInfo = this.blockBreakMap.computeIfAbsent(pos, key -> new BreakingInfo());
							breakingInfo.lastTimeHit = this.tickCount;
							breakingInfo.progress += breakProgress;
							if (breakingInfo.progress >= 1.0F) {
								// destroy block
								this.level().destroyBlock(pos, true);
								this.blockBreakMap.remove(pos);
								int i = 0x1000000 + this.getId() * 256 + breakingInfo.id;
								this.level().destroyBlockProgress(i, pos, -1);
							}
						}
					}
				}
			}
			Iterator<Map.Entry<BlockPos, BreakingInfo>> iterator = this.blockBreakMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<BlockPos, BreakingInfo> entry = iterator.next();
				BreakingInfo breakingInfo = entry.getValue();
				if (this.tickCount - breakingInfo.lastTimeHit >= this.blockBreakThreshhold()) {
					breakingInfo.progress -= this.blockBreakRevert();
				}
				int i = 0x1000000 + this.getId() * 256 + breakingInfo.id;
				if (breakingInfo.progress <= 0.0F) {
					iterator.remove();
					this.level().destroyBlockProgress(i, entry.getKey(), -1);
				} else {
					this.level().destroyBlockProgress(i, entry.getKey(), (int) (breakingInfo.progress * 10.0F));
				}
			}

			Vec3 vec1 = new Vec3(-this.laserEffectRadius(), -this.laserEffectRadius(), 0.0D);
			Vec3 vec2 = new Vec3(this.laserEffectRadius(), this.laserEffectRadius(), d);
			BoundingBox bb = new BoundingBox(vec1, vec2, Math.toRadians(this.rotationYawCQR), Math.toRadians(this.rotationPitchCQR), start);
			for (LivingEntity entity : BoundingBox.getEntitiesInsideBB(this.level(), this.caster, LivingEntity.class, bb)) {
				if (this.canHitEntity(entity) && this.tickCount - this.hitInfoMap.getInt(entity) >= this.getEntityHitRate()) {
					this.onEntityHit(entity);
					this.hitInfoMap.put(entity, this.tickCount);
				}
			}
		}
	}

	public boolean canHitBlock(BlockPos pos, BlockState state) {
		return true;
	}

	public float onHitBlock(BlockPos pos, BlockState state) {
		float hardness = state.getDestroySpeed(this.level(), pos);
		if (hardness < 0.0F) {
			return 0.0F;
		}
		if (hardness == 0.0F) {
			return 1.0F;
		}
		int ticks;
		if (hardness <= 2.0F) {
			ticks = 40 + Mth.ceil(hardness * 20.0F);
		} else {
			ticks = Mth.ceil(20.0F * (8.0F * hardness) / (hardness + 2.0F));
		}
		return 1.0F / ticks + 1.0E-7F;
	}

	public int blockBreakThreshhold() {
		return 60;
	}

	public float blockBreakRevert() {
		return 0.02F;
	}

	public int getEntityHitRate() {
		return 10;
	}

	public boolean canHitEntity(LivingEntity entity) {
		return !TargetUtil.isAllyCheckingLeaders(this.caster, entity);
	}

	public void onEntityHit(LivingEntity entity) {
		// TODO: Create own damage type for lasers
		entity.hurt(new DamageSource("ray").bypassArmor(), this.getDamage());
	}

	public boolean canBreakBlocks() {
		return false;
	}

	public int getBreakingSpeed() {
		return 1;
	}

	public float getDamage() {
		return 3.0F;
	}

	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
		if (!this.level().isClientSide) {
			Iterator<Map.Entry<BlockPos, BreakingInfo>> iterator = this.blockBreakMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<BlockPos, BreakingInfo> entry = iterator.next();
				int i = 0x1000000 + this.getId() * 256 + entry.getValue().id;
				this.level().destroyBlockProgress(i, entry.getKey(), -1);
			}
		}
	}

	public abstract void setupPositionAndRotation();

	public abstract void updatePositionAndRotation();

	//Part of the renderer now
	/*@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}*/

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(this.caster.getId());
		buffer.writeFloat(this.length);
		buffer.writeFloat(this.rotationYawCQR);
		buffer.writeFloat(this.rotationPitchCQR);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		this.caster = (LivingEntity) this.level().getEntity(additionalData.readInt());
		this.length = additionalData.readFloat();
		this.rotationYawCQR = additionalData.readFloat();
		this.rotationPitchCQR = additionalData.readFloat();
		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;
	}

	public float getColorR() {
		return 0.1F;
	}

	public float getColorG() {
		return 0.7F;
	}

	public float getColorB() {
		return 0.9F;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
