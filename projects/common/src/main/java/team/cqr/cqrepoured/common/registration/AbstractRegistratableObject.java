package team.cqr.cqrepoured.common.registration;

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		if (obj instanceof AbstractRegistratableObject aro) {
			return this.getId().equals(aro.getId());
		}
		return false;
	}

}
