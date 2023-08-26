package team.cqr.cqrepoured.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

@Mixin(PackRepository.class)
public class MixinResourcePackList {

	@Shadow
	@Final
	private Set<RepositorySource> sources;

	@Inject(method = "<init>([Lnet/minecraft/resources/RepositorySource;)V", at = @At("RETURN"))
	private void init(RepositorySource[] packFinders, CallbackInfo info) {
		this.sources.add((packInfoConsumer) -> {
			packInfoConsumer.accept(Pack.create(
					"cqrepoured_config",
					Component.literal("CQ-Repoured config resources"),
					true, 
					(string) -> new ModFolderPack("CQRepoured Config", CQRConstants.MODID, CQRMain.CQ_CONFIG_FOLDER.toPath()), 
					new Pack.Info(Component.literal("Dynamic resources of CQR"), 0, null), //TODO
					PackType.SERVER_DATA,
					Pack.Position.TOP,
					true,
					//TODO: WTF is a packsource?!
					
				));
		});
	}

}
