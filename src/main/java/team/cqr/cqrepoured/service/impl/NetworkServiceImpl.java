package team.cqr.cqrepoured.service.impl;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.services.interfaces.NetworkService;

public class NetworkServiceImpl implements NetworkService {

	private static final String PROTOCOL_VERSION = "1";
	private static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(CQRepoured.MODID, "main-network-channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	@Override
	public SimpleChannel network() {
		return NETWORK;
	}

}
