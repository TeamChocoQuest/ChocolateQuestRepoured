package team.cqr.cqrepoured.entity;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntitySlimePart extends Slime {

	private UUID ownerUuid;

	public EntitySlimePart(Level world) {
		this(CQREntityTypes.SMALL_SLIME.get(), world);
	}
	
	public EntitySlimePart(EntityType<? extends EntitySlimePart> type, Level worldIn) {
		super(type, worldIn);
	}

	public EntitySlimePart(EntityType<? extends EntitySlimePart> type, Level worldIn, LivingEntity owner) {
		this(type, worldIn);
		this.ownerUuid = owner.getUUID();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.getAvailableGoals().clear();
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity
			.createLivingAttributes()
			.add(Attributes.FOLLOW_RANGE, 16.0D)
			.add(Attributes.ATTACK_KNOCKBACK)
			.add(Attributes.MOVEMENT_SPEED, 0.35);
	}

	@Override
	public void tick() {
		if (this.tickCount > 400) {
			this.discard();;
		}

		super.tick();
	}

	@Override
	protected void doPush(Entity pEntity) {
		this.push(pEntity);
	}

	@Override
	public void push(Entity entityIn) {
		if (entityIn instanceof LivingEntity && entityIn.getUUID().equals(this.ownerUuid)) {
			((LivingEntity) entityIn).heal(2.0F);
			this.discard();
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ticksExisted", this.tickCount);
		compound.put("ownerUuid", NbtUtils.createUUID(this.ownerUuid));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.tickCount = compound.getInt("ticksExisted");
		this.ownerUuid = NbtUtils.loadUUID(compound.getCompound("ownerUuid"));
	}

}
