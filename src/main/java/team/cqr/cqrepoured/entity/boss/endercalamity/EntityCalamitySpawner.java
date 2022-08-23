package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCalamitySpawner extends Entity {

	private int timer;
	
	private String faction;
	private float sizeScaling;
	private double healthScaling;

	private static final int CALAMITY_SPAWN_DURATION = 800;

	public EntityCalamitySpawner(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
		this.setNoGravity(true);
		this.setInvisible(true);
		this.setEntityInvulnerable(true);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}

	public static ItemStack FIREWORK_PURPLE_SPARK = Items.FIREWORKS.getDefaultInstance();

	private static final int FIREWORK_DURATION = 120;
	private static final int FIREWORK_DIVISOR = 5;

	@Override
	public void onEntityUpdate() {
		if (this.world.isRemote) {
			super.onEntityUpdate();
			return;
		} else {
			this.timer++;
			int tmpTimer = CALAMITY_SPAWN_DURATION - this.timer;
			double percentage = (double) tmpTimer / (double) FIREWORK_DURATION;

			if (this.timer >= 200) {
				if (this.timer % 40 == 0) {
					this.spawnScaryEffect((int) (Math.round(25.0D * percentage) + 5));
					if ((CALAMITY_SPAWN_DURATION - this.timer > FIREWORK_DURATION + 60)) {
						if (DungeonGenUtils.percentageRandom(0.75, this.rand)) {
							this.spawnFireworks((int) (Math.round(3.0D * percentage) + 1));
						}
					}
				}
				// Keep the lightning? Idk, it looks cool but it is a bit overused :/
				if ((this.timer - 2) % 40 == 0 && this.rand.nextBoolean()) {
					EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(this.world, this.posX, this.posY, this.posZ, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
					lightning.setPosition(this.posX, this.posY, this.posZ);
					this.world.spawnEntity(lightning);
				}

				if (this.timer >= CALAMITY_SPAWN_DURATION) {
					if (this.timer == CALAMITY_SPAWN_DURATION && !this.world.isRemote) {
						// DONE: SPawn ender calamity
						this.spawnCalamity();
					}
					this.setDead();
					return;
				}

				// DONE: When it is about 40 ticks until it spawns, spawn particles leading to the center every 5 ticks and rotate by 5
				// degrees every tick
				if (CALAMITY_SPAWN_DURATION - this.timer <= FIREWORK_DURATION) {
					if (tmpTimer % FIREWORK_DIVISOR == 0) {
						// Percentage defines radius

						double radius = 2 * EntityCQREnderCalamity.getArenaRadius();
						radius *= percentage;
						radius += 1.5;
						Vec3d vector = new Vec3d(radius, 0, 0);
						final int lines = 5;
						final int rotationDegree = 360 / lines;
						vector = VectorUtil.rotateVectorAroundY(vector, 4 * rotationDegree * percentage);
						for (int i = 0; i < lines; i++) {
							Vec3d particlePosition = this.getPositionVector().add(vector);

							this.spawnFirework(particlePosition.x, particlePosition.y + 1.0, particlePosition.z, FIREWORK_PURPLE_SPARK);

							vector = VectorUtil.rotateVectorAroundY(vector, rotationDegree);
						}
					}
				}
			}
			super.onEntityUpdate();
		}
	}

	private void spawnFirework(double x, double y, double z, ItemStack stack) {
		EntityFireworkRocket firework = new EntityFireworkRocket(this.world, x, y, z, FIREWORK_PURPLE_SPARK);
		firework.lifetime = 1;

		firework.setInvisible(true);
		firework.setSilent(true);

		this.world.spawnEntity(firework);
	}

	private Vec3d getRandomPositionAroundPosition() {
		Vec3d v = new Vec3d(EntityCQREnderCalamity.getArenaRadius() * this.rand.nextDouble(), 0, 0);
		v = VectorUtil.rotateVectorAroundY(v, DungeonGenUtils.randomBetween(0, 360, this.rand));

		return v.add(this.getPositionVector());
	}

	private void spawnCalamity() {
		EntityCQREnderCalamity calamity = new EntityCQREnderCalamity(this.world);
		calamity.setFaction(this.faction, false);
		calamity.setHomePositionCQR(this.getPosition());
		calamity.setHealthScale(this.healthScaling);
		calamity.setSizeVariation(this.sizeScaling);
		calamity.setPosition(calamity.getHomePositionCQR().getX(), calamity.getHomePositionCQR().getY(), calamity.getHomePositionCQR().getZ());

		this.world.spawnEntity(calamity);

		EntityUtil.addEntityToAllRegionsAt(this.getPosition(), calamity);
		EntityUtil.removeEntityFromAllRegionsAt(this.getPosition(), this);
	}

	// Spawns some firework and flame particles and plays a scary sound
	protected void spawnScaryEffect(int count) {
		this.playSound(SoundEvents.ENTITY_ENDERMEN_SCREAM, 1.0F, 0.5F + 0.5F * this.rand.nextFloat());
	}

	// Spawns some fireworks and a small enderman too
	protected void spawnFireworks(int count) {
		for (int i = 0; i < count; i++) {
			Vec3d v = this.getRandomPositionAroundPosition();
			this.spawnFirework(v.x, v.y, v.z, FIREWORK_PURPLE_SPARK);
		}
	}

	// Spawns a few particles which throw out a lingering potion that places a small cloud later
	protected void spawnEnderClouds(int count, int minSize, int maxSize) {
		/*
		 * for (int i = 0; i < count; i++) {
		 * Vec3d v = this.getRandomPositionAroundPosition();
		 * }
		 */
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.timer = compound.getInteger("entityTimer");
		this.setFaction(compound.getString("faction"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("entityTimer", this.timer);
		compound.setString("faction", this.getFaction());
	}

	public String getFaction() {
		return this.faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public void setSizeScaling(float sizeScaling) {
		this.sizeScaling = sizeScaling;
	}

	public void setHealthScaling(double healthScaling) {
		this.healthScaling = healthScaling;
	}

	static {
		NBTTagCompound compound = FIREWORK_PURPLE_SPARK.getTagCompound();
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		NBTTagCompound fwCompound = new NBTTagCompound();
		NBTTagList explosionCompoundList = new NBTTagList();

		NBTTagCompound explosionCompound = new NBTTagCompound();
		explosionCompound.setInteger("Type", 4);
		explosionCompound.setIntArray("Colors", new int[] { 0x7B2FBE });
		explosionCompound.setIntArray("FadeColors", new int[] { 0x253192, 0x6689D3, 0xC354CD });

		explosionCompoundList.appendTag(explosionCompound);
		fwCompound.setTag("Explosions", explosionCompoundList);
		compound.setTag("Fireworks", fwCompound);
		FIREWORK_PURPLE_SPARK.setTagCompound(compound);
	}

}
