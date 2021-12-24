package team.cqr.cqrepoured.client.model;

import java.util.Collection;

public interface IHideable {

	void setupVisibility(Collection<String> visibleParts);

	void resetVisibility();

}
