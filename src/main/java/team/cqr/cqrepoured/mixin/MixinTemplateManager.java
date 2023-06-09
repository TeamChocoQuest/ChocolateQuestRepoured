package team.cqr.cqrepoured.mixin;

import java.io.File;
import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.template.ExtendedStructureTemplate;

@Mixin(TemplateManager.class)
public abstract class MixinTemplateManager {

	@Inject(
			at = @At("RETURN"),
			method = "createPathToStructure(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)Ljava/nio/file/Path;"
	)
	private void mixinCreatePathToStructure(ResourceLocation structureTemplateId, String fileExtension, CallbackInfoReturnable<Path> cir) {
		//If this is a CQR structure, then return the path to it, otherwise ignore it
		if(structureTemplateId != null && structureTemplateId.getNamespace().equalsIgnoreCase(CQRMain.MODID_STRUCTURES)) {
			File f = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, structureTemplateId.getPath() + "." + fileExtension);
			if(f.exists() && f.canRead()) {
				Path path = f.toPath();
				cir.setReturnValue(path);
			}
		}
	}
	
	@Inject(
			at =@At("HEAD"),
			cancellable = true,
			method = "readStructure(Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/world/gen/feature/template/Template;"
	)
	private void mixinReadStructure(CompoundTag structureFileNBTData, CallbackInfoReturnable<Template> cir) {
		//If this is a cqr structure file, then create a new ExtendedStructureTemplate object and return that
		if(structureFileNBTData != null && structureFileNBTData.contains(ExtendedStructureTemplate.CQR_VERSION_KEY, Constants.NBT.TAG_STRING)) {
			ExtendedStructureTemplate structure = new ExtendedStructureTemplate();
			structure.load(structureFileNBTData);
			cir.setReturnValue(structure);
		}
	}
	
	
}
