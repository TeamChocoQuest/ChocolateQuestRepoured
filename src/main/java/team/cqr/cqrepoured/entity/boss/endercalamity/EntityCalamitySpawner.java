package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCalamitySpawner extends Entity {

	private int timer;
	private String faction;

	private static final int CALAMITY_SPAWN_DURATION = 800;

	public EntityCalamitySpawner(World world) {
		this(CQREntityTypes.CALAMITY_SPAWNER.get(), world);
	}
	
	public EntityCalamitySpawner(EntityType<? extends EntityCalamitySpawner> type, World worldIn) {
		super(type, worldIn);
		this.setNoGravity(true);
		this.setInvisible(true);
		this.setInvulnerable(true);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canCollideWith(Entity pEntity) {
		return false;
	}
	
	@Override
	public void playerTouch(PlayerEntity pEntity) {
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public void push(Entity entityIn) {
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}

	public static ItemStack FIREWORK_PURPLE_SPARK = Items.FIREWORK_ROCKET.getDefaultInstance();

	private static final int FIREWORK_DURATION = 120;
	private static final int FIREWORK_DIVISOR = 5;

	@Override
	public void baseTick() {
		if (this.level.isClientSide) {
			super.baseTick();
			return;
		} else {
			this.timer++;
			int tmpTimer = CALAMITY_SPAWN_DURATION - this.timer;
			double percentage = (double) tmpTimer / (double) FIREWORK_DURATION;

			if (this.timer >= 200) {
				if (this.timer % 40 == 0) {
					this.spawnScaryEffect((int) (Math.round(25.0D * percentage) + 5));
					if ((CALAMITY_SPAWN_DURATION - this.timer > FIREWORK_DURATION + 60)) {
						if (DungeonGenUtils.percentageRandom(0.75, this.random)) {
							this.spawnFireworks((int) (Math.round(3.0D * percentage) + 1));
						}
					}
				}
				// Keep the lightning? Idk, it looks cool but it is a bit overused :/
				if ((this.timer - 2) % 40 == 0 && this.random.nextBoolean()) {
					EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(this.level, this.getX(), this.getY(), this.getZ(), true, false, 0.34F, 0.08F, 0.43F, 0.4F);
					lightning.setPos(this.getX(), this.getY(), this.getZ());
					this.level.addFreshEntity(lightning);
				}

				if (this.timer >= CALAMITY_SPAWN_DURATION) {
					if (this.timer == CALAMITY_SPAWN_DURATION && !this.level.isClientSide) {
						// DONE: SPawn ender calamity
						this.spawnCalamity();
					}
					this.remove();
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
						Vector3d vector = new Vector3d(radius, 0, 0);
						final int lines = 5;
						final int rotationDegree = 360 / lines;
						vector = VectorUtil.rotateVectorAroundY(vector, 4 * rotationDegree * percentage);
						for (int i = 0; i < lines; i++) {
							Vector3d particlePosition = this.position().add(vector);

							this.spawnFirework(particlePosition.x, particlePosition.y + 1.0, particlePosition.z, FIREWORK_PURPLE_SPARK);

							vector = VectorUtil.rotateVectorAroundY(vector, rotationDegree);
						}
					}
				}
			}
			super.baseTick();
		}
	}

	private void spawnFirework(double x, double y, double z, ItemStack stack) {
		FireworkRocketEntity firework = new FireworkRocketEntity(this.level, x, y, z, FIREWORK_PURPLE_SPARK);
		firework.lifetime = 1;

		firework.setInvisible(true);
		firework.setSilent(true);

		this.level.addFreshEntity(firework);
	}

	private Vector3d getRandomPositionAroundPosition() {
		Vector3d v = new Vector3d(EntityCQREnderCalamity.getArenaRadius() * this.random.nextDouble(), 0, 0);
		v = VectorUtil.rotateVectorAroundY(v, DungeonGenUtils.randomBetween(0, 360, this.random));

		return v.add(this.position());
	}

	private void spawnCalamity() {
		EntityCQREnderCalamity calamity = new EntityCQREnderCalamity(this.level);
		calamity.setFaction(this.faction, false);
		calamity.setHomePositionCQR(this.blockPosition());
		calamity.setPos(calamity.getHomePositionCQR().getX(), calamity.getHomePositionCQR().getY(), calamity.getHomePositionCQR().getZ());

		this.level.addFreshEntity(calamity);

		EntityUtil.addEntityToAllRegionsAt(this.blockPosition(), calamity);
		EntityUtil.removeEntityFromAllRegionsAt(this.blockPosition(), this);
	}

	// Spawns some firework and flame particles and plays a scary sound
	protected void spawnScaryEffect(int count) {
		this.playSound(SoundEvents.ENDERMAN_SCREAM, 1.0F, 0.5F + 0.5F * this.random.nextFloat());
	}

	// Spawns some fireworks and a small enderman too
	protected void spawnFireworks(int count) {
		for (int i = 0; i < count; i++) {
			Vector3d v = this.getRandomPositionAroundPosition();
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
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.timer = compound.getInt("entityTimer");
		this.setFaction(compound.getString("faction"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putInt("entityTimer", this.timer);
		compound.putString("faction", this.getFaction());
	}

	public String getFaction() {
		return this.faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	static {
		CompoundNBT compound = FIREWORK_PURPLE_SPARK.getTag();
		if (compound == null) {
			compound = new CompoundNBT();
		}
		CompoundNBT fwCompound = new CompoundNBT();
		ListNBT explosionCompoundList = new ListNBT();

		CompoundNBT explosionCompound = new CompoundNBT();
		explosionCompound.putInt("Type", 4);
		explosionCompound.putIntArray("Colors", new int[] { 0x7B2FBE });
		explosionCompound.putIntArray("FadeColors", new int[] { 0x253192, 0x6689D3, 0xC354CD });

		explosionCompoundList.add(explosionCompound);
		fwCompound.put("Explosions", explosionCompoundList);
		compound.put("Fireworks", fwCompound);
		FIREWORK_PURPLE_SPARK.setTag(compound);
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
