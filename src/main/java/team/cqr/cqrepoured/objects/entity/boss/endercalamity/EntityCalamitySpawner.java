package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.VectorUtil;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class EntityCalamitySpawner extends Entity {

	private int timer;
	private String faction;

	private static final int CALAMITY_SPAWN_DURATION = 400;

	public EntityCalamitySpawner(World worldIn) {
		super(worldIn);
		this.setSize(0, 0);
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

	private static ReflectionField<Integer> FW_LIFETIME_FIELD = new ReflectionField<>(EntityFireworkRocket.class, "field_92055_b", "lifetime");

	@Override
	public void onEntityUpdate() {

		this.timer++;

		if (!this.world.isRemote) {
			if (this.timer >= CALAMITY_SPAWN_DURATION) {
				// DONE: SPawn ender calamity
				this.spawnCalamity();
			}
		}
		// DONE: When it is about 40 ticks until it spawns, spawn particles leading to the center every 5 ticks and rotate by 5 degrees every tick
		if (CALAMITY_SPAWN_DURATION - this.timer <= 60) {
			int tmpTimer = CALAMITY_SPAWN_DURATION - this.timer;
			if (tmpTimer % 5 == 0) {
				float percentage = tmpTimer / 60;
				// Percentage defines radius

				double radius = EntityCQREnderCalamity.getArenaRadius();
				radius *= percentage;
				radius += 3;
				Vec3d vector = new Vec3d(radius, 0, 0);
				final int lines = 5;
				final int rotationDegree = 360 / lines;
				vector = VectorUtil.rotateVectorAroundY(vector, 2 * rotationDegree * percentage);
				for (int i = 0; i < lines; i++) {
					Vec3d particlePosition = this.getPositionVector().add(vector);

					this.spawnFirework(particlePosition.x, particlePosition.y, particlePosition.z, FIREWORK_PURPLE_SPARK);

					vector = VectorUtil.rotateVectorAroundY(vector, rotationDegree);
				}
			}
		}
		super.onEntityUpdate();
	}

	private void spawnFirework(double x, double y, double z, ItemStack stack) {
		EntityFireworkRocket firework = new EntityFireworkRocket(world, x, y, z, FIREWORK_PURPLE_SPARK);
		// DONE: Modify the "lifetime" value using reflection
		FW_LIFETIME_FIELD.set(firework, 1);

		this.world.spawnEntity(firework);
	}
	
	private Vec3d getRandomPositionAroundPosition() {
		Vec3d v = new Vec3d(EntityCQREnderCalamity.getArenaRadius() * this.rand.nextDouble(), 0,0);
		v = VectorUtil.rotateVectorAroundY(v, DungeonGenUtils.randomBetween(0, 360, this.rand));
		
		return v;
	}

	private void spawnCalamity() {
		EntityCQREnderCalamity calamity = new EntityCQREnderCalamity(world);
		calamity.setFaction(this.faction, false);
		calamity.setHomePositionCQR(this.getPosition());
		calamity.setPosition(calamity.getHomePositionCQR().getX(), calamity.getHomePositionCQR().getY(), calamity.getHomePositionCQR().getZ());

		calamity.forceTeleport();

		world.spawnEntity(calamity);

		EntityUtil.addEntityToAllRegionsAt(this.getPosition(), calamity);
		EntityUtil.removeEntityFromAllRegionsAt(this.getPosition(), this);
	}

	// Spawns some firework and flame particles and plays a scary sound
	protected void spawnScaryEffect(int count) {
		for (int i = 0; i < count; i++) {
			Vec3d v = this.getRandomPositionAroundPosition();
		}
	}

	// Spawns some fireworks and a small enderman too
	protected void spawnFireworks(int count) {
		for (int i = 0; i < count; i++) {
			Vec3d v = this.getRandomPositionAroundPosition();
		}
	}

	// Spawns a few particles which throw out a lingering potion that places a small cloud later
	protected void spawnEnderClouds(int count, int minSize, int maxSize) {
		for (int i = 0; i < count; i++) {
			Vec3d v = this.getRandomPositionAroundPosition();
		}
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
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	static {
		NBTTagCompound fwCompound = new NBTTagCompound();
		NBTTagCompound explosionCompound = new NBTTagCompound();

		explosionCompound.setInteger("Type", 4);
		explosionCompound.setIntArray("Colors", new int[] { 8073150 });
		explosionCompound.setIntArray("FadeColors", new int[] { 2437522, 6719955, 12801229 });

		fwCompound.setTag("Explosions", explosionCompound);
		FIREWORK_PURPLE_SPARK.setTagInfo("Fireworks", fwCompound);
	}

}
