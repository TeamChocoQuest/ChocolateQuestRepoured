package team.cqr.cqrepoured.client.render.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.ResourceLocation;

public class ResourceSupplier implements Supplier<String> {

	private final ResourceLocation file;

	public ResourceSupplier(ResourceLocation file) {
		this.file = file;
	}

	@Override
	public String get() {
		StringBuilder sb = new StringBuilder();

		try (IResource resource = Minecraft.getInstance().getResourceManager().getResource(this.file)) {
			try (Stream<String> stream = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()) {
				stream.forEach(s -> {
					sb.append(s);
					sb.append('\n');
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}
