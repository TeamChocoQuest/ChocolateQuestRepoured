package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(EndermanEntity.class)
public abstract class MixinEndermanEntity extends MonsterEntity {

	protected MixinEndermanEntity(EntityType<? extends MonsterEntity> p_i48553_1_, World p_i48553_2_) {
		super(p_i48553_1_, p_i48553_2_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	/*@Inject(at = @At("HEAD"), method = "getMobType()Lnet/minecraft/entity/CreatureAttribute;")
	private void mixinGetMobType(CallbackInfoReturnable<CreatureAttribute> cbir) {
		cbir.setReturnValue(CQRCreatureAttributes.VOID);
	}*/
	
}
