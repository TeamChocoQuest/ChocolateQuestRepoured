package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityCQRZombie extends EntityZombie
{
    public EntityCQRZombie(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return super.getDeathSound();
    }
}
