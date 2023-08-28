package team.cqr.cqrepoured.util.registration;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;

public class AbstractRegistratableObject implements RegistrationIDSupplier {
	
	private ResourceLocation id = null; 

	@Nullable
	public ResourceLocation getId() {
		return this.id;
	}
	
	public final void setId(final ResourceLocation id) {
		if (this.id == null) {
			this.id = id;
		}
	}

}
