package team.cqr.cqrepoured;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

@Mod(CQRepoured.MODID)
public class CQRepoured {

	public static final String MODID = "cqrepoured";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final boolean isWorkspaceEnvironment = !CQRepoured.class.getResource("")
			.getProtocol()
			.equals("jar");

}
