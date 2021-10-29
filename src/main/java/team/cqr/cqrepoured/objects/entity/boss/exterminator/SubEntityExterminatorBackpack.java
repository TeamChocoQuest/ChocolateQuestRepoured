package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;

public class SubEntityExterminatorBackpack extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private Supplier<Boolean> funcGetAnyEmitterActive;
	private EntityCQRExterminator exterminator;
	
	public SubEntityExterminatorBackpack(EntityCQRExterminator parent, String partName, Supplier<Boolean> funcGetAnyEmitterActive) {
		super(parent, partName, 1.0F, 1.0F);
		this.exterminator = parent;
		this.funcGetAnyEmitterActive = funcGetAnyEmitterActive;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		//If at least one emitter emits electricity, we are vulnerable!!
		if(source == DamageSource.DROWN) {
			if(this.funcGetAnyEmitterActive.get()) {
				this.exterminator.setStunned(true, 300);
			}
		}
		
		if(source == DamageSource.LIGHTNING_BOLT) {
			this.exterminator.setStunned(true, 100);
		}
		
		if(!this.exterminator.isStunned() && this.funcGetAnyEmitterActive.get()) {
			return super.attackEntityFrom(DamageSource.DROWN, amount /= 2);
		}
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (this.exterminator == null || this.exterminator.isDead) {
			return false;
		}
		return this.exterminator.processInitialInteract(player, hand);
	}
		
}
