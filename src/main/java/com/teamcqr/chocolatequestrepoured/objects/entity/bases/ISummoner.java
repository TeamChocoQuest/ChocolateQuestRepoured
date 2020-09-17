package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

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
