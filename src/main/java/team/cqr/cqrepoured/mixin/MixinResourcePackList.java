package team.cqr.cqrepoured.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.resources.ModFolderPack;

@Mixin(ResourcePackList.class)
public class MixinResourcePackList {

	@Shadow
	@Final
	private Set<IPackFinder> sources;

	@Inject(method = "<init>(Lnet/minecraft/resources/ResourcePackInfo$IFactory;[Lnet/minecraft/resources/IPackFinder;)V", at = @At("RETURN"))
	private void init(ResourcePackInfo.IFactory factory, IPackFinder[] packFinders, CallbackInfo info) {
		this.sources.add((packInfoConsumer, packFactory) -> {
			packInfoConsumer.accept(ResourcePackInfo.create("cqrepoured_config", true, () -> new ModFolderPack("CQRepoured Config", CQRMain.MODID, CQRMain.CQ_CONFIG_FOLDER.toPath()), packFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILT_IN));
		});
	}

}
