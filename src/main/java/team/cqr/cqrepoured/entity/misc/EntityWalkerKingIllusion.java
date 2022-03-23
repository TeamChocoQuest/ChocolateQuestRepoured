package team.cqr.cqrepoured.entity.misc;

import com.google.common.base.Predicates;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.Capes;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;
import team.cqr.cqrepoured.entity.mobs.EntityCQRWalker;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQREntityTypes;

import java.util.UUID;

public class EntityWalkerKingIllusion extends EntityCQRWalker {

	private EntityCQRWalkerKing parent;
	private int ttl = 1200;
	private int searchTicksForParent = 20;
	private int damageCounter = 0;
	private UUID parentUUID = null;

	public EntityWalkerKingIllusion(EntityType<? extends EntityWalkerKingIllusion> type, World world) {
		super(type, world);
	}
	
	public EntityWalkerKingIllusion(World worldIn) {
		this(CQREntityTypes.WALKER_KING_ILLUSION.get(), worldIn);
	}

	public EntityWalkerKingIllusion(int ttl, EntityCQRWalkerKing parent, World world) {
		this(world);
		this.parent = parent;
		this.ttl = ttl;
		this.parentUUID = parent.getUUID();

		this.cloneParentEquipment(parent);
	}

	private void cloneParentEquipment(AbstractEntityCQR parent) {
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, parent.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION));
		this.setItemSlot(EquipmentSlotType.CHEST, parent.getItemBySlot(EquipmentSlotType.CHEST));
		this.setItemSlot(EquipmentSlotType.HEAD, parent.getItemBySlot(EquipmentSlotType.HEAD));
		this.setItemSlot(EquipmentSlotType.LEGS, parent.getItemBySlot(EquipmentSlotType.LEGS));
		this.setItemSlot(EquipmentSlotType.FEET, parent.getItemBySlot(EquipmentSlotType.FEET));
		this.setItemSlot(EquipmentSlotType.OFFHAND, parent.getItemBySlot(EquipmentSlotType.OFFHAND));
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(parent.getItemBySlot(EquipmentSlotType.MAINHAND).getItem(), 1));
	}

	@Override
	protected int getExperienceReward(PlayerEntity player) {
		return 0;
	}

	@Override
	protected void dropAllDeathLoot(DamageSource pDamageSource) {
	}
	
	@Override
	protected ResourceLocation getDefaultLootTable() {
		return LootTables.EMPTY;
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();
		
		if (this.parent != null) {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.parent.getMaxHealth());
			this.setHealth(this.parent.getHealth());
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return this.hurt(source, amount, false);
	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		// return super.attackEntityFrom(source, amount, sentFromPart);
		if (!this.level.isClientSide && this.damageCounter >= 2 * (this.level.getDifficulty().ordinal() <= 0 ? 1 : this.level.getDifficulty().ordinal())) {
			this.remove();
		}
		this.damageCounter++;
		return true;
	}

	@Override
	public void remove() {
		if (this.level.isClientSide) {
			this.playDeathEffect();
		}
		super.remove();
	}

	private void playDeathEffect() {
		if (this.level.isClientSide) {
			for (int i = 0; i < 15; i++) {
				this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.025, 0.0);
				this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.025, 0.01, 0.025);
				this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.025, 0.01, -0.025);
				this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), -0.025, 0.01, 0.025);
				this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), -0.025, 0.01, -0.025);
			}
			this.playSound(SoundEvents.FIRE_EXTINGUISH, 2, 0.75F);
		}
	}

	@Override
	public void baseTick() {
		if (!this.level.isClientSide) {
			if (this.ttl < 0) {
				this.remove();
				return;
			}
			// Search parent
			if (this.parent == null && this.parentUUID != null) {
				if (this.searchTicksForParent > 0) {
					if (!this.level.isClientSide) {
						this.level.getEntities(this, new AxisAlignedBB(this.blockPosition().offset(-10, -10, -10), this.blockPosition().offset(10, 10, 10)), Predicates.instanceOf(EntityCQRWalkerKing.class)).forEach(t -> {
							if (t.getUUID().equals(EntityWalkerKingIllusion.this.parentUUID)) {
								EntityWalkerKingIllusion.this.parent = (EntityCQRWalkerKing) t;
							}
						});

						this.searchTicksForParent--;
					}
				} else {
					this.remove();
					return;
				}
			}
			if (this.parent == null || this.parent.isDeadOrDying()) {
				this.remove();
				return;
			}
			super.baseTick();
			this.setHealth(this.parent.getHealth());

			if (this.parent.getTarget() != null || this.getTarget() != null) {
				this.ttl--;
			} else {
				this.ttl -= 10;
			}
		} else {
			super.baseTick();
		}
	}

	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_WALKER;
	}

	@Override
	public boolean hasCape() {
		return true;
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ttl", this.ttl);
		compound.put("illusionParent", NBTUtil.createUUID(this.parentUUID));
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.ttl = compound.getInt("ttl");
		this.parentUUID = NBTUtil.loadUUID(compound.getCompound("illusionParent"));
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

}
