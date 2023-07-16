package team.cqr.cqrepoured.client.render.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class ResourceSupplier implements Supplier<String> {

	private final ResourceLocation file;

	public ResourceSupplier(ResourceLocation file) {
		this.file = file;
	}

	@Override
	public String get() {
		StringBuilder sb = new StringBuilder();

		Optional<Resource> optResource = Minecraft.getInstance().getResourceManager().getResource(this.file);
		optResource.ifPresent(resource -> {
			try (Stream<String> stream = new BufferedReader(new InputStreamReader(resource.open())).lines()) {
				stream.forEach(s -> {
					sb.append(s);
					sb.append('\n');
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		return sb.toString();
	}

}
