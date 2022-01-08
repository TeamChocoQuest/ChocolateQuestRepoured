package team.cqr.cqrepoured.entity.boss.spectrelord;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;

public class EntityTargetingLaser extends AbstractEntityLaser {

	private LivingEntity target;
	protected float maxRotationPerTick = 2.0F;

	public EntityTargetingLaser(World worldIn) {
		this(worldIn, null, 4.0F, null);
	}

	public EntityTargetingLaser(World worldIn, LivingEntity caster, float length, LivingEntity target) {
		super(worldIn, caster, length);
		this.target = target;
	}

	@Override
	public void updatePositionAndRotation() {
		// TODO reduce unnecessary vec3d creation
		Vector3d vec1 = this.caster.position().add(0, this.caster.getBbHeight() * 0.6D,0);
		vec1 = vec1.add(this.getOffsetVector());
		Vector3d vec2 = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
		Vector3d vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		// TODO make pitch rotatable to < -90 and > 90
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		float deltaYaw = MathHelper.wrapDegrees(yaw - this.rotationYawCQR);
		float deltaPitch = MathHelper.wrapDegrees(pitch - this.rotationPitchCQR);
		float delta = this.maxRotationPerTick / (float) Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
		if (delta > 1.0F) {
			delta = 1.0F;
		}
		deltaYaw *= delta;
		deltaPitch *= delta;
		this.rotationYawCQR += deltaYaw;
		this.rotationYawCQR = MathHelper.wrapDegrees(this.rotationYawCQR);
		this.rotationPitchCQR += deltaPitch;
		Vector3d vec4 = Vector3d.directionFromRotation(this.rotationPitchCQR, this.rotationYawCQR);
		this.setPos(vec1.x + vec4.x * 0.25D, vec1.y + vec4.y * 0.25D, vec1.z + vec4.z * 0.25D);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeInt(this.target.getId());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.target = (LivingEntity) this.level.getEntity(additionalData.readInt());
	}

}
