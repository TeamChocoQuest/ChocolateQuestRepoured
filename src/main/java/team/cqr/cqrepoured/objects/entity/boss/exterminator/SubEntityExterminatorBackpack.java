package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.function.Function;

import net.minecraft.util.DamageSource;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;

public class SubEntityExterminatorBackpack extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private Function<Object, Boolean> funcGetAnyEmitterActive;
	
	public SubEntityExterminatorBackpack(EntityCQRExterminator parent, String partName, Function<Object, Boolean> funcGetAnyEmitterActive) {
		super(parent, partName, 1.0F, 1.0F);
		
		this.funcGetAnyEmitterActive = funcGetAnyEmitterActive;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		//If at least one emitter emits electricity, we are vulnerable!!
		if(this.funcGetAnyEmitterActive.apply(null)) {
		}
		
		return super.attackEntityFrom(source, amount);
	}
		
}
