package team.cqr.cqrepoured.client.util;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientWorldUtil {
	
	@Nullable
	public static Entity getEntityByUUID(Level world, UUID uuid) {
		if(world instanceof ClientLevel) {
			ClientLevel cw = (ClientLevel) world;
			for(Entity entity : cw.entitiesById.values()) {
				if(entity.getUUID().equals(uuid)) {
					return entity;
				}
			}
		}
		return null;
	}

}
