package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;

public class EntityTargetingLaser extends AbstractEntityLaser {

	protected LivingEntity target;
	protected float maxRotationPerTick = 2.0F;

	public EntityTargetingLaser(EntityType<? extends EntityTargetingLaser> type, Level worldIn) {
		this(type, worldIn, null, 4.0F, null);
	}

	public EntityTargetingLaser(EntityType<? extends EntityTargetingLaser> type, Level worldIn, LivingEntity caster, float length, LivingEntity target) {
		super(type, worldIn, caster, length);
		this.target = target;
	}

	@Override
	public void setupPositionAndRotation() {
		// TODO reduce unnecessary vec3d creation
		Vec3 vec1 = this.caster.position();
		vec1 = vec1.add(this.getOffsetVector());
		Vec3 vec2 = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
		Vec3 vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		this.rotationYawCQR = yaw;
		this.rotationPitchCQR = pitch;
		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void updatePositionAndRotation() {
		// TODO reduce unnecessary vec3d creation

		Vec3 vec1 = this.caster.position();

		vec1 = vec1.add(this.getOffsetVector());
		Vec3 vec2 = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
		Vec3 vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		// TODO make pitch rotatable to < -90 and > 90
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		float deltaYaw = Mth.wrapDegrees(yaw - this.rotationYawCQR);
		float deltaPitch = Mth.wrapDegrees(pitch - this.rotationPitchCQR);
		float delta = this.maxRotationPerTick / (float) Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
		if (delta > 1.0F) {
			delta = 1.0F;
		}
		deltaYaw *= delta;
		deltaPitch *= delta;
		this.rotationYawCQR += deltaYaw;
		this.rotationYawCQR = Mth.wrapDegrees(this.rotationYawCQR);
		this.rotationPitchCQR += deltaPitch;

		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeInt(this.target.getId());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.target = (LivingEntity) this.level.getEntity(additionalData.readInt());
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
