package com.example.chocolatequest.entity.projectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;

public class ProjectileEarthQuake extends Entity {
    public ProjectileEarthQuake(EntityType<?> type, Level level) {
        super(type, level);
    }
    public ProjectileEarthQuake(Level level, Entity entity) {
        super(com.example.chocolatequest.registry.ModEntities.PROJECTILE_CANNON_BALL.get(), level);
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}
    @Override protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(CompoundTag tag) {}
    
    public void shootFromRotation(Entity shooter, int a, float b, float c, float d, float e) {}
    public void setThrowHeight(double d) {}
}
