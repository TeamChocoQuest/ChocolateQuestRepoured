package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityRotatingLaser extends AbstractEntityLaser {

	private float deltaRotationYawPerTick;
	private float deltaRotationPitchPerTick;

	public EntityRotatingLaser(EntityType<? extends EntityRotatingLaser> type, Level worldIn) {
		this(type, worldIn, null, 4.0F, 1.0F, 0.0F);
	}

	public EntityRotatingLaser(Level worldIn, LivingEntity caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		this(CQREntityTypes.LASER_ROTATING.get(), worldIn, caster, length, deltaRotationYawPerTick, deltaRotationPitchPerTick);
	}
	
	public EntityRotatingLaser(EntityType<? extends EntityRotatingLaser> type, Level worldIn, LivingEntity caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		super(type, worldIn, caster, length);
		this.deltaRotationYawPerTick = deltaRotationYawPerTick;
		this.deltaRotationPitchPerTick = deltaRotationPitchPerTick;
	}

	@Override
	public void setupPositionAndRotation() {
		// TODO reduce unnecessary vec3d creation
		Vec3 vec1 =  this.caster.position();
		vec1 = vec1.add(this.getOffsetVector());
		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void updatePositionAndRotation() {
		this.rotationYawCQR = Mth.wrapDegrees(this.rotationYawCQR + this.deltaRotationYawPerTick);
		this.rotationPitchCQR = Mth.wrapDegrees(this.rotationPitchCQR + this.deltaRotationPitchPerTick);
		// TODO reduce unnecessary vec3d creation
		Vec3 vec1 = this.caster.position();
		vec1 = vec1.add(this.getOffsetVector());
		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeFloat(this.deltaRotationYawPerTick);
		buffer.writeFloat(this.deltaRotationPitchPerTick);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.deltaRotationYawPerTick = additionalData.readFloat();
		this.deltaRotationPitchPerTick = additionalData.readFloat();
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

}
