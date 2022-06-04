package team.cqr.cqrepoured.entity.boss;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
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

	protected AbstractEntityLaser(EntityType<? extends AbstractEntityLaser> type, World worldIn) {
		this(type, worldIn, null, 4.0F);
	}

	protected AbstractEntityLaser(EntityType<? extends AbstractEntityLaser> type, World worldIn, LivingEntity caster, float length) {
		super(type, worldIn);
		this.caster = caster;
		this.length = length;
		this.noCulling = true;
		this.noPhysics = true;
	}

	public Vector3d getOffsetVector() {
		if (this.caster == null) return Vector3d.ZERO;
		Vector3d v = new Vector3d(0.0D, this.caster.getBbHeight() * 0.6D, 0.0D);
		v = v.add(this.caster.getLookAngle().scale(0.25D));
		return v;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true; //distance < 64.0D * 64.0D;
	}
	
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {
		
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {
		
	}
	
	@Override
	public void load(CompoundNBT pCompound) {
	}
	
	@Override
	public boolean save(CompoundNBT pCompound) {
		return false;
	}

	public double laserEffectRadius() {
		return 0.25D;
	}

	@Override
	public void baseTick() {
		if (!this.level.isClientSide && !this.caster.isAlive()) {
			this.remove();
		}

		super.baseTick();

		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;

		if (this.level.isClientSide) {
			this.rotationYawCQR = this.serverRotationYawCQR;
			this.rotationPitchCQR = this.serverRotationPitchCQR;
		} else {
			this.updatePositionAndRotation();
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), new SPacketSyncLaserRotation(this));
		}

		if (!this.level.isClientSide) {
			Vector3d start = this.position();
			Vector3d end = start.add(Vector3d.directionFromRotation(this.rotationPitchCQR, this.rotationYawCQR).scale(this.length));
			RayTraceContext rtc = new RayTraceContext(start, end, BlockMode.COLLIDER, FluidMode.NONE, null);
			RayTraceResult result = this.level.clip(rtc);//this.level.rayTraceBlocks(start, end, false, false, false);
			double d = result != null ? (float) result.getLocation().subtract(this.position()).length() : this.length;

			if (result != null) {
				BlockPos pos = new BlockPos(result.getLocation());
				BlockState state = this.level.getBlockState(pos);
				if (this.canHitBlock(pos, state)) {
					float breakProgress = this.onHitBlock(pos, state);
					if (breakProgress > 0.0F) {
						if (breakProgress >= 1.0F) {
							// destroy block
							this.level.destroyBlock(pos, true);
						} else {
							BreakingInfo breakingInfo = this.blockBreakMap.computeIfAbsent(pos, key -> new BreakingInfo());
							breakingInfo.lastTimeHit = this.tickCount;
							breakingInfo.progress += breakProgress;
							if (breakingInfo.progress >= 1.0F) {
								// destroy block
								this.level.destroyBlock(pos, true);
								this.blockBreakMap.remove(pos);
								int i = 0x1000000 + this.getId() * 256 + breakingInfo.id;
								this.level.destroyBlockProgress(i, pos, -1);
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
					this.level.destroyBlockProgress(i, entry.getKey(), -1);
				} else {
					this.level.destroyBlockProgress(i, entry.getKey(), (int) (breakingInfo.progress * 10.0F));
				}
			}

			Vector3d vec1 = new Vector3d(-this.laserEffectRadius(), -this.laserEffectRadius(), 0.0D);
			Vector3d vec2 = new Vector3d(this.laserEffectRadius(), this.laserEffectRadius(), d);
			BoundingBox bb = new BoundingBox(vec1, vec2, Math.toRadians(this.rotationYawCQR), Math.toRadians(this.rotationPitchCQR), start);
			for (LivingEntity entity : BoundingBox.getEntitiesInsideBB(this.level, this.caster, LivingEntity.class, bb)) {
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
		float hardness = state.getDestroySpeed(this.level, pos);
		if (hardness < 0.0F) {
			return 0.0F;
		}
		if (hardness == 0.0F) {
			return 1.0F;
		}
		int ticks;
		if (hardness <= 2.0F) {
			ticks = 40 + MathHelper.ceil(hardness * 20.0F);
		} else {
			ticks = MathHelper.ceil(20.0F * (8.0F * hardness) / (hardness + 2.0F));
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
		if (!this.level.isClientSide) {
			Iterator<Map.Entry<BlockPos, BreakingInfo>> iterator = this.blockBreakMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<BlockPos, BreakingInfo> entry = iterator.next();
				int i = 0x1000000 + this.getId() * 256 + entry.getValue().id;
				this.level.destroyBlockProgress(i, entry.getKey(), -1);
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
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.caster.getId());
		buffer.writeFloat(this.length);
		buffer.writeFloat(this.rotationYawCQR);
		buffer.writeFloat(this.rotationPitchCQR);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.caster = (LivingEntity) this.level.getEntity(additionalData.readInt());
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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
