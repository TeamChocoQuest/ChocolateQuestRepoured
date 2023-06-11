package team.cqr.cqrepoured.mixin;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(EnderDragonEntity.class)
public abstract class MixinEnderdragonEntity extends MobEntity {

	protected MixinEnderdragonEntity(EntityType<? extends MobEntity> p_i48576_1_, Level p_i48576_2_) {
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
