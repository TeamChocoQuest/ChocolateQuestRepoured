package team.cqr.cqrepoured.objects.entity.bases;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.misc.EntityFlyingSkullMinion;

public interface ISummoner {

	CQRFaction getSummonerFaction();

	List<Entity> getSummonedEntities();

	EntityLivingBase getSummoner();

	default void setSummonedEntityFaction(Entity summoned) {
		if (summoned instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) summoned).setLeader(this.getSummoner());
			((AbstractEntityCQR) summoned).setFaction(this.getSummonerFaction().getName());
		}
		if (summoned instanceof EntityFlyingSkullMinion) {
			((EntityFlyingSkullMinion) summoned).setSummoner(this.getSummoner());
		}
	}

	void addSummonedEntityToList(Entity summoned);

}
