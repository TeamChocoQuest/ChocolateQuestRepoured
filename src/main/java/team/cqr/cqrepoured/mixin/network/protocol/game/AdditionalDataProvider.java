package team.cqr.cqrepoured.mixin.network.protocol.game;

import java.util.Optional;

public interface AdditionalDataProvider {

	public Optional<Object> getCustomData();
	
}
