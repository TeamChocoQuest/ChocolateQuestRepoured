package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.EntityAITeleportToTargetWhenStuck;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQREnderman extends AbstractEntityCQR {

	public EntityCQREnderman(World worldIn) {
		super(worldIn);
		this.stepHeight = 1.0F;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		this.tasks.addTask(3, new EntityAITeleportToTargetWhenStuck<>(this));
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source instanceof IndirectEntityDamageSource) {
			for (int i = 0; i < 64; ++i) {
				if (this.teleportRandomly()) {
					return false;
				}
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected void updateAITasks() {
		if (this.isInWater() || (this.isWet() && this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty())) {
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}
		super.updateAITasks();
	}

	protected boolean teleportRandomly() {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	private boolean teleportTo(double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return false;
		}
		boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (flag) {
			this.world.playSound((PlayerEntity) null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return flag;
	}

	@Override
	public void teleport(double x, double y, double z) {
		this.teleportTo(x, y, z);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Enderman;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return /* this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : */ SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getEntityAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_ENDERMAN;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.875F;
	}

	@Override
	public float getDefaultWidth() {
		return 0.6F;
	}

	@Override
	public float getDefaultHeight() {
		return 2.9F;
	}

	@Override
	public void onLivingUpdate() {
		if (this.world.isRemote) {
			// Client
			for (int i = 0; i < 2; ++i) {
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
						(this.rand.nextDouble() - 0.5D) * 2.0D);
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CQRCreatureAttributes.VOID;
	}
}
