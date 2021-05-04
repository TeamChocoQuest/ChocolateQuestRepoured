package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.EntityUtil;

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
	
	@Override
	public void onEntityUpdate() {
		
		this.timer++;
		
		if(!this.world.isRemote) {
			if(this.timer >= CALAMITY_SPAWN_DURATION) {
				//DONE: SPawn ender calamity
				this.spawnCalamity();
			}
		}
		
		super.onEntityUpdate();
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

	protected void spawnLightnings(int count) {
		for(int i = 0; i < count; i++) {
			
		}
	}
	
	protected void spawnFireworks(int count) {
		for(int i = 0; i < count; i++) {
			
		}
	}
	
	protected void spawnEnderClouds(int count, int minSize, int maxSize) {
		for(int i = 0; i < count; i++) {
			
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
	
}
