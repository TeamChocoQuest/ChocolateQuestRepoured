package com.teamcqr.chocolatequestrepoured.factions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.util.Constants;

public class CQRFaction {

	private boolean savedGlobally = true;
	private boolean repuMayChange = true;
	private String name;
	private List<CQRFaction> allies = Collections.synchronizedList(new ArrayList<CQRFaction>());
	private List<CQRFaction> enemies = Collections.synchronizedList(new ArrayList<CQRFaction>());
	private EReputationState defaultRelation;
	
	private int repuChangeOnMemberKill = 5;
	private int repuChangeOnAllyKill = 2;
	private int repuChangeOnEnemyKill = 1;
	
	public CQRFaction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this(name, defaultReputationState, true, canRepuChange, repuChangeOnMemberKill, repuChangeOnAllyKill, repuChangeOnEnemyKill);
	}
	
	public CQRFaction(@Nonnull String name, @Nonnull EReputationState defaultReputationState, boolean saveGlobally, boolean canRepuChange, Optional<Integer> repuChangeOnMemberKill, Optional<Integer> repuChangeOnAllyKill, Optional<Integer> repuChangeOnEnemyKill) {
		this.savedGlobally = saveGlobally;
		this.name = name;
		this.defaultRelation = defaultReputationState;
		this.repuMayChange = canRepuChange;
		this.repuChangeOnMemberKill = repuChangeOnMemberKill.isPresent() ? repuChangeOnMemberKill.get() : 5;
		this.repuChangeOnAllyKill = repuChangeOnAllyKill.isPresent() ? repuChangeOnAllyKill.get() : 2;
		this.repuChangeOnEnemyKill = repuChangeOnEnemyKill.isPresent() ? repuChangeOnEnemyKill.get() : 1;
	}
	
	public int getRepuMemberKill() {return repuChangeOnMemberKill;}
	public int getRepuAllyKill() {return repuChangeOnAllyKill;}
	public int getRepuEnemyKill() {return repuChangeOnEnemyKill;}
	
	public String getName() {
		return this.name;
	}
	public boolean isSavedGlobally() {
		return savedGlobally;
	}
	public EReputationState getDefaultReputation() {
		return defaultRelation;
	}

	public void addAlly(CQRFaction ally) {
		if(ally != null) {
			allies.add(ally);
		}
	}
	
	public void addEnemy(CQRFaction enemy) {
		if(enemy != null) {
			enemies.add(enemy);
		}
	}
	
	//DONE: Special case for player faction!!
	public boolean isEnemy(Entity ent) {
		if(ent.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		if(ent instanceof EntityPlayer) {
			//Special case for player
			return FactionRegistry.instance().getReputationOf(ent.getPersistentID(), this) == EReputationStateRough.ENEMY;
		}
		return isEnemy(FactionRegistry.instance().getFactionOf(ent));
	}
	public boolean isEnemy(AbstractEntityCQR ent) {
		if(ent.getEntityWorld().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			return false;
		}
		return isEnemy(ent.getFaction());
	}
	public boolean isEnemy(CQRFaction faction) {
		if(faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return false;
		}
		if(faction != null) {
			for(CQRFaction str : this.enemies) {
				if(str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	//DONE: Special case for player faction!!
	public boolean isAlly(Entity ent) {
		if(ent instanceof EntityPlayer) {
			//Special case for player
			return FactionRegistry.instance().getReputationOf(ent.getPersistentID(), this) == EReputationStateRough.ALLY;
		}
		return isAlly(FactionRegistry.instance().getFactionOf(ent));
	}
	public boolean isAlly(AbstractEntityCQR ent) {
		return isAlly(ent.getFaction());
	}
	public boolean isAlly(CQRFaction faction) {
		if(faction == this || (faction != null && faction.getName().equalsIgnoreCase("ALL_ALLY"))) {
			return true;
		}
		if(faction != null) {
			for(CQRFaction str : this.allies) {
				if(str != null && faction.getName().equalsIgnoreCase(str.getName())) {
					return true;
				}
			}
		}
		return false;
	}


	public void decrementReputation(EntityPlayer player, int score) {
		if(repuMayChange) {
			FactionRegistry.instance().decrementRepuOf(player, name, score);
		}
	}

	public void incrementReputation(EntityPlayer player, int score) {
		if(repuMayChange) {
			FactionRegistry.instance().incrementRepuOf(player, name, score);
		}
	}
	
	public boolean isRepuStatic() {
		return !repuMayChange;
	}
	
	public void saveToFile(File folder) {
		if(savedGlobally) {
			//DONE: SAVE DATA
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					File file = FileIOUtil.getOrCreateFile(folder.getAbsolutePath(), getName() + ".nbt");
					NBTTagCompound root = FileIOUtil.getRootNBTTagOfFile(file);
					if(root != null) {
						root.setString("type", "faction");
						root.setString("name", name);
						root.setBoolean("staticreputation", isRepuStatic());
						root.setString("defaultrelation", getDefaultReputation().toString());
						root.setInteger("repuchangekillally", getRepuAllyKill());
						root.setInteger("repuchangekillmember", getRepuMemberKill());
						root.setInteger("repuchangekillenemy", getRepuEnemyKill());
						NBTTagCompound relationInfo = new NBTTagCompound();
						if(relationInfo.hasKey("allies")) {
							relationInfo.removeTag("allies");
						}
						NBTTagList allyTag = FileIOUtil.getOrCreateTagList(relationInfo, "allies", Constants.NBT.TAG_STRING);
						for(CQRFaction af : allies) {
							allyTag.appendTag(new NBTTagString(af.getName()));
						}
						if(relationInfo.hasKey("enemies")) {
							relationInfo.removeTag("enemies");
						}
						NBTTagList enemyTag = FileIOUtil.getOrCreateTagList(relationInfo, "enemies", Constants.NBT.TAG_STRING);
						for(CQRFaction ef : enemies) {
							enemyTag.appendTag(new NBTTagString(ef.getName()));
						}
						relationInfo.setTag("allies", allyTag);
						relationInfo.setTag("enemies", enemyTag);
						root.setTag("relations", relationInfo);
						FileIOUtil.saveNBTCompoundToFile(root, file);
					}
				}
			});
			t.start();
		}
	}

}
