package team.cqr.cqrepoured.mixin;

import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

@Mixin(TemplateManager.class)
public abstract class MixinTemplateManager {

	@Inject(
			at = @At("RETURN"),
			method = "createPathToStructure(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)Ljava/nio/file/Path;"
	)
	private void mixinCreatePathToStructure(ResourceLocation structureTemplateId, String fileExtension, CallbackInfoReturnable<Path> cir) {
		//If this is a CQR structure, then return the path to it, otherwise ignore it
	}
	
	@Inject(
			at =@At("HEAD"),
			cancellable = true,
			method = "readStructure(Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/world/gen/feature/template/Template;"
	)
	private void mixinReadStructure(CompoundNBT structureFileNBTData, CallbackInfoReturnable<Template> cir) {
		//If this is a cqr structure file, then create a new ExtendedStructureTemplate object and return that
	}
	
	
}
