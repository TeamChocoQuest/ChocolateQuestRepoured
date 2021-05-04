package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.items.armor.ItemArmorDyable;
import team.cqr.cqrepoured.structureprot.IProtectedRegionManager;
import team.cqr.cqrepoured.structureprot.ProtectedRegionManager;
import team.cqr.cqrepoured.structureprot.ServerProtectedRegionManager;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntityCQREnderKing extends AbstractEntityCQRBoss {

	protected static final DataParameter<Boolean> WIDE = EntityDataManager.<Boolean>createKey(EntityCQREnderKing.class, DataSerializers.BOOLEAN);

	public EntityCQREnderKing(World worldIn) {
		super(worldIn);
		this.stepHeight = 1.0F;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}

	@Override
	protected void updateAITasks() {
		if (this.isInWater() || (this.isWet() && this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())) {
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}
		super.updateAITasks();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		amount /= 2;
		if (source instanceof EntityDamageSourceIndirect || source.isUnblockable()) {
			for (int i = 0; i < 64; ++i) {
				if (this.teleportRandomly()) {
					if (source.isUnblockable()) {
						return super.attackEntityFrom(source, amount);
					}
					return false;
				}
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(WIDE, DungeonGenUtils.percentageRandom(0.05));
	}

	@Override
	public ITextComponent getDisplayName() {
		if (this.isWide()) {
			return new TextComponentString("Wide Enderman");
		}
		return super.getDisplayName();
	}

	public boolean isWide() {
		return this.dataManager.get(WIDE);
	}

	@Override
	protected boolean doesExplodeOnDeath() {
		return true;
	}

	@Override
	protected boolean usesEnderDragonDeath() {
		return true;
	}

	@Override
	public void onDeath(DamageSource cause) {
		// TODO: SPawn the true boss, BEFORE super.onDeath (that one creates the living death event)
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);
		if (manager instanceof ServerProtectedRegionManager) {

			EntityCalamitySpawner cs = new EntityCalamitySpawner(world);
			cs.setFaction(this.getFaction().getName());
			

			world.spawnEntity(cs);

			EntityUtil.addEntityToAllRegionsAt(this.hasHomePositionCQR() ? this.getHomePositionCQR() : this.getPosition(), cs);
		}
		super.onDeath(cause);
	}

	protected boolean teleportRandomly() {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (double) (this.rand.nextInt(64) - 32);
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
			this.world.playSound((EntityPlayer) null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return flag;
	}

	@Override
	public float getBaseHealth() {
		return 2F * CQRConfig.baseHealths.Enderman;
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
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
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
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D,
						-this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CQRItems.GREAT_SWORD_DIAMOND));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING, 3));

		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(CQRItems.KING_CROWN, 1));

		// Give him some armor...
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display", 10)) {
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", 9437439);
		ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE, 1, 0, nbttagcompound);
		((ItemArmorDyable) CQRItems.CHESTPLATE_DIAMOND_DYABLE).setColor(chest, 9437439);
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_ENDERMAN;
	}

	@Override
	protected float getSoundVolume() {
		return 2F * super.getSoundVolume();
	}

	@Override
	protected float getSoundPitch() {
		return 0.75F * super.getSoundPitch();
	}

	@Override
	public int getTalkInterval() {
		// Super: 80
		return 60;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return CQRCreatureAttributes.CREATURE_TYPE_ENDERMAN;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("wide_enderman", isWide());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(WIDE, compound.getBoolean("wide_enderman"));
	}

}
