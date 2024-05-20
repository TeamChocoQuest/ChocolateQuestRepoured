package team.cqr.cqrepoured.common.services;

import java.util.ServiceLoader;

import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.services.interfaces.BlockService;
import team.cqr.cqrepoured.common.services.interfaces.ConfigService;
import team.cqr.cqrepoured.common.services.interfaces.EntityAIService;
import team.cqr.cqrepoured.common.services.interfaces.FactionService;
import team.cqr.cqrepoured.common.services.interfaces.NetworkService;

public class CQRServices {
	
	public static final NetworkService NETWORK = load(NetworkService.class);
	public static final EntityAIService ENTITY_AI = load(EntityAIService.class); 
	public static final FactionService FACTION = load(FactionService.class);
	public static final BlockService BLOCK = load(BlockService.class);
	public static final ConfigService CONFIG = load(ConfigService.class);

	public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        CQRepoured.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
	
}
