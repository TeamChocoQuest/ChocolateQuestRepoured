package team.cqr.cqrepoured.client.util;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

public class ClientWorldUtil {
	
	@Nullable
	public static Entity getEntityByUUID(World world, UUID uuid) {
		if(world instanceof ClientWorld) {
			ClientWorld cw = (ClientWorld) world;
			for(Entity entity : cw.entitiesById.values()) {
				if(entity.getUUID().equals(uuid)) {
					return entity;
				}
			}
		}
		return null;
	}

}
