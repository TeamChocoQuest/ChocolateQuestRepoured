package team.cqr.cqrepoured.entity.boss.spectrelord;

import org.joml.Vector3d;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityRotatingLaser extends AbstractEntityLaser {

	private float deltaRotationYawPerTick;
	private float deltaRotationPitchPerTick;

	public EntityRotatingLaser(EntityType<? extends EntityRotatingLaser> type, World worldIn) {
		this(type, worldIn, null, 4.0F, 1.0F, 0.0F);
	}

	public EntityRotatingLaser(World worldIn, LivingEntity caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		this(CQREntityTypes.LASER_ROTATING.get(), worldIn, caster, length, deltaRotationYawPerTick, deltaRotationPitchPerTick);
	}
	
	public EntityRotatingLaser(EntityType<? extends EntityRotatingLaser> type, World worldIn, LivingEntity caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		super(type, worldIn, caster, length);
		this.deltaRotationYawPerTick = deltaRotationYawPerTick;
		this.deltaRotationPitchPerTick = deltaRotationPitchPerTick;
	}

	@Override
	public void setupPositionAndRotation() {
		// TODO reduce unnecessary vec3d creation
		Vector3d vec1 =  this.caster.position();
		vec1 = vec1.add(this.getOffsetVector());
		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void updatePositionAndRotation() {
		this.rotationYawCQR = MathHelper.wrapDegrees(this.rotationYawCQR + this.deltaRotationYawPerTick);
		this.rotationPitchCQR = MathHelper.wrapDegrees(this.rotationPitchCQR + this.deltaRotationPitchPerTick);
		// TODO reduce unnecessary vec3d creation
		Vector3d vec1 = this.caster.position();
		vec1 = vec1.add(this.getOffsetVector());
		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		super.writeSpawnData(buffer);
		buffer.writeFloat(this.deltaRotationYawPerTick);
		buffer.writeFloat(this.deltaRotationPitchPerTick);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		super.readSpawnData(additionalData);
		this.deltaRotationYawPerTick = additionalData.readFloat();
		this.deltaRotationPitchPerTick = additionalData.readFloat();
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {
		
	}

}
