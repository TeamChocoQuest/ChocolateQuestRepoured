package team.cqr.cqrepoured.client.resources.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.resources.data.IMetadataSection;

public class PartMetadataSection implements IMetadataSection {

	private final Set<String> parts;

	public PartMetadataSection(Collection<String> parts) {
		this.parts = new HashSet<>(parts);
	}

	public Collection<String> getParts() {
		return Collections.unmodifiableCollection(this.parts);
	}

}
