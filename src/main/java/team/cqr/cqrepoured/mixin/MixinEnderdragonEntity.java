package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(EnderDragonEntity.class)
public abstract class MixinEnderdragonEntity extends MobEntity {

	protected MixinEnderdragonEntity(EntityType<? extends MobEntity> p_i48576_1_, World p_i48576_2_) {
		super(p_i48576_1_, p_i48576_2_);
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
