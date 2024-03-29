package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(EnderMan.class)
public abstract class MixinEndermanEntity extends Monster {

	protected MixinEndermanEntity(EntityType<? extends Monster> p_i48553_1_, Level p_i48553_2_) {
		super(p_i48553_1_, p_i48553_2_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public MobType getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	/*@Inject(at = @At("HEAD"), method = "getMobType()Lnet/minecraft/entity/CreatureAttribute;")
	private void mixinGetMobType(CallbackInfoReturnable<CreatureAttribute> cbir) {
		cbir.setReturnValue(CQRCreatureAttributes.VOID);
	}*/
	
}
