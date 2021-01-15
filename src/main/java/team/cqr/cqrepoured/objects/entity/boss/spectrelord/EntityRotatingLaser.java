package team.cqr.cqrepoured.objects.entity.boss.spectrelord;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;

public class EntityRotatingLaser extends AbstractEntityLaser {

	private float rotationPerTick;

	public EntityRotatingLaser(World worldIn) {
		this(worldIn, null, 4.0F, 1.0F);
	}

	public EntityRotatingLaser(World worldIn, EntityLivingBase caster, float length, float rotationPerTick) {
		super(worldIn, caster, length);
		this.rotationPerTick = rotationPerTick;
	}

	@Override
	public void updatePositionAndRotation() {
		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;
		this.rotationYawCQR += this.rotationPerTick;
		Vec3d vec1 = new Vec3d(this.caster.posX, this.caster.posY + this.caster.height * 0.6D, this.caster.posZ);
		Vec3d vec4 = Vec3d.fromPitchYaw(this.rotationPitchCQR, this.rotationYawCQR);
		this.setPosition(vec1.x + vec4.x * 0.25D, vec1.y + vec4.y * 0.25D, vec1.z + vec4.z * 0.25D);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeFloat(this.rotationPerTick);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.rotationPerTick = additionalData.readFloat();
	}

}
