package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.function.Supplier;

import net.minecraft.util.DamageSource;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;

public class SubEntityExterminatorBackpack extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private Supplier<Boolean> funcGetAnyEmitterActive;
	
	public SubEntityExterminatorBackpack(EntityCQRExterminator parent, String partName, Supplier<Boolean> funcGetAnyEmitterActive) {
		super(parent, partName, 1.0F, 1.0F);
		
		this.funcGetAnyEmitterActive = funcGetAnyEmitterActive;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		//If at least one emitter emits electricity, we are vulnerable!!
		if(this.funcGetAnyEmitterActive.get()) {
			
		}
		
		return super.attackEntityFrom(source, amount);
	}
		
}
