package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CQStructure {
	
	public static List<Thread> runningExportThreads = new ArrayList<>();

	private File dataFile;
	
	private int sizeX;
	private int sizeY;
	private int sizeZ;
	
	private int parts = 0;
	private int bossCount = 0;
	private NBTTagCompound bossCompound = null;
	private String author = "DerToaster98";
	
	//DONE: Add methods and fields to replace the old banners
	private EBanners newBannerPattern = EBanners.WALKER_BANNER;
	
	@Nullable
	private BlockPos shieldCorePosition = null;
	
	//DONE: move structure origin to the center of it -> "Placing Config"
	
	private HashMap<BlockPos, CQStructurePart> structures = new HashMap<BlockPos, CQStructurePart>();

	private boolean buildShieldCore;
	
	public CQStructure(String name, boolean hasShield) {
		this.buildShieldCore = hasShield;
		this.setDataFile(new File(CQRMain.CQ_EXPORT_FILES_FOLDER, name + ".nbt"));
	}
	
	public void setNewBannerPattern(EBanners pattern) {
		this.newBannerPattern = pattern;
	}
	
	public CQStructure(File file, @Nullable DungeonBase dungeon, int dunX, int dunZ, boolean hasShield) {
		System.out.println("Dungeon is null: " + (dungeon == null));
		EDungeonMobType mobType = dungeon.getDungeonMob();
		if(dungeon.getDungeonMob().equals(EDungeonMobType.DEFAULT)) {
			mobType = EDungeonMobType.getMobTypeDependingOnDistance(dunX, dunZ);
		}
		this.buildShieldCore = hasShield;
		//System.out.println(file.getName());
		if(file.isFile() && file.getName().contains(".nbt")) {
			//DONE: read nbt file and create the substructures
			boolean failed = true;
			InputStream stream = null;
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if(stream != null) {
				NBTTagCompound root = null;
				try {
					root = CompressedStreamTools.readCompressed(stream);
				} catch(IOException ex) {
					ex.printStackTrace();
					root = null;
				}
				if(root != null) {
					if(root.hasKey("type") && root.hasKey("parts")) {
						if(root.getString("type").equalsIgnoreCase("CQ_Structure")) {
							try {
								this.parts = root.getInteger("partcount");
								if(root.hasKey("bossesCount") && root.hasKey("bosses")) {
									this.bossCount = root.getInteger("bossesCount");
									
									this.bossCompound = root.getCompoundTag("bosses");
								}
								
								NBTTagCompound sizeComp = root.getCompoundTag("size");
								this.setSizeX(sizeComp.getInteger("x"));
								this.setSizeY(sizeComp.getInteger("y"));
								this.setSizeZ(sizeComp.getInteger("z"));
								
								this.setAuthor(root.getString("author"));
								
								NBTTagCompound partsCompound = root.getCompoundTag("parts");
								
								//Now load all the parts...
								for(int i = 0; i < this.parts; i++) {
									NBTTagCompound part = partsCompound.getCompoundTag("p" +i);
									
									BlockPos offsetVector = NBTUtil.BlockPosFromNBT(part.getCompoundTag("offset"));
									
									CQStructurePart partStructure = new CQStructurePart(dungeon, dunX, dunZ);
									partStructure.setDungeonMob(mobType);
									partStructure.setNewBannerPattern(newBannerPattern);
									partStructure.read(part);
									
									this.structures.put(offsetVector, partStructure);
								}
								
								failed = false;
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			if(!failed) {
				this.dataFile = file;
			}
		}
	}
	
	public void placeBlocksInWorld(World world, BlockPos pos, PlacementSettings settings, EPosType posType) {
		//X and Z values are the lower ones of the positions ->
		//N-S ->
		//E-W ->
		//N: -Z
		//E: +X
		//S: +Z
		//W: -X
		BlockPos pastePos = pos;
		switch(posType) {
		case CENTER_XZ_LAYER:
			pastePos = pos.subtract(new Vec3i(sizeX /2, 0, sizeZ /2));
			break;
		case CORNER_NE:
			pastePos = pos.subtract(new Vec3i(sizeX,0,0));
			break;
		case CORNER_SE:
			pastePos = pos.subtract(new Vec3i(sizeX,0,sizeZ));
			break;
		case CORNER_SW:
			pastePos = pos.subtract(new Vec3i(0,0,sizeZ));
			break;
		default:
			break;
		}
		
		placeBlocksInWorld(world, pastePos, settings);
	}
	
	private void placeBlocksInWorld(World worldIn, BlockPos pos, PlacementSettings settings) {
		if(this.dataFile != null) {
			//System.out.println("Generating structure: " + this.dataFile.getName() + "...");
			//int partID = 1;
			List<BlockPos> shieldCorePosList = new ArrayList<BlockPos>();
			for(BlockPos offset : this.structures.keySet()) {
				//System.out.println("building part " + partID + " of " + this.structures.keySet().size() + "...");
				BlockPos offsetVec = CQStructurePart.transformedBlockPos(settings, offset);
				BlockPos pastePos = pos.add(offsetVec);
				CQStructurePart structure = this.structures.get(offset);
				structure.addBlocksToWorld(worldIn, pastePos, settings);
				if(this.buildShieldCore) {
					try {
						if(structure.getFieldCores() != null && !structure.getFieldCores().isEmpty()) {
							for(ForceFieldNexusInfo ffni : structure.getFieldCores()) {
								//fieldCoreMap.put(offsetVec.add(Structure.transformedBlockPos(settings, ffni.getPos())), ffni);
								shieldCorePosList.add(new BlockPos(offsetVec.add(CQStructurePart.transformedBlockPos(settings, ffni.getPos()))));
							}
						}
					} catch(Exception ex) {
						ex.printStackTrace();
						//fieldCoreMap = null;
						shieldCorePosList = null;
					}
					try {
						//if(fieldCoreMap != null && !fieldCoreMap.isEmpty()) {
						if(shieldCorePosList != null && !shieldCorePosList.isEmpty()) {
							//BlockPos key = (BlockPos) fieldCoreMap.keySet().toArray()[new Random().nextInt(fieldCoreMap.keySet().toArray().length)];
							BlockPos key = shieldCorePosList.get(new Random().nextInt(shieldCorePosList.size()));
							//ForceFieldNexusInfo shieldCore = fieldCoreMap.get(key);
							this.shieldCorePosition = key;
							// TODO: Place the block with attached information
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				//partID++;
			}
			
			//Place boss blocks
			placeBossBlocks(worldIn, pos, settings);
		}
	}
	
	private void placeBossBlocks(World worldIn, BlockPos pos, PlacementSettings settings) {
		if(this.bossCount > 0 && this.bossCompound != null) {
			for(int i = 0; i < this.bossCount; i++) {
				try {
					BossInfo boi = new BossInfo(bossCompound.getCompoundTag("boss"+i));
					if(boi != null) {
						BlockPos vecPos = CQStructurePart.transformedBlockPos(settings, boi.getPos());
						vecPos = vecPos.add(pos);
						
						//TODO: Place spawner for right boss
						worldIn.setBlockState(vecPos, ModBlocks.BOSS_BLOCK.getDefaultState());
					}
				} catch(Exception ex) {
					
				}
			}
		}
	}

	// DONE?: Split structure into 16x16 grid
	public void save(World worldIn, BlockPos posStart, BlockPos posEnd, boolean usePartMode, EntityPlayer placer) {
		BlockPos startPos = new BlockPos(Math.min(posStart.getX(), posEnd.getX()),
				Math.min(posStart.getY(), posEnd.getY()), Math.min(posStart.getZ(), posEnd.getZ()));
		BlockPos endPos = new BlockPos(Math.max(posStart.getX(), posEnd.getX()),
				Math.max(posStart.getY(), posEnd.getY()), Math.max(posStart.getZ(), posEnd.getZ()));

		endPos = endPos.add(1, 1, 1);

		this.setSizeX(endPos.getX() - startPos.getX());
		this.setSizeY(endPos.getY() - startPos.getY());
		this.setSizeZ(endPos.getZ() - startPos.getZ());

		if (usePartMode && this.sizeX > 17 && this.sizeZ > 17) {
			// Use part mode and cut the structure into multiple smaller 16xHEIGHTx16 cubes
			int partIndex = 0;
			int xIterations = this.sizeX / 16;
			int zIterations = this.sizeZ / 16;

			for (int iX = 0; iX <= xIterations; iX++) {
				for (int iZ = 0; iZ <= zIterations; iZ++) {
					BlockPos partStartPos = startPos.add(16 * iX, 0, 16 * iZ);
					BlockPos partEndPos = partStartPos.add(16, this.sizeY, 16);

					if (iX == xIterations) {
						partEndPos = new BlockPos(endPos.getX(), partEndPos.getY(), partEndPos.getZ());
					}
					if (iZ == zIterations) {
						partEndPos = new BlockPos(partEndPos.getX(), partEndPos.getY(), endPos.getZ());
					}

					CQStructurePart part = new CQStructurePart(partIndex);
					part.takeBlocksFromWorld(worldIn, partStartPos, partEndPos, true, Blocks.STRUCTURE_VOID);
					this.structures.put(partStartPos.subtract(startPos), part);
					partIndex++;
				}
			}

			this.parts = partIndex;
		} else {
			// Do not use the part mode -> Save as one huge block
			CQStructurePart struct = new CQStructurePart(0);
			struct.takeBlocksFromWorld(worldIn, startPos, endPos, true, Blocks.STRUCTURE_VOID);
			this.structures.put(BlockPos.ORIGIN, struct);
			this.parts = 1;
		}

		this.writeNBT(placer);
	}

	private void writeNBT(EntityPlayer placer) {
		System.out.println("Saving file " + this.dataFile.getName() +"...");
		NBTTagCompound root = new NBTTagCompound();
		root.setString("type", "CQ_Structure");
		root.setInteger("partcount", this.parts);
		
		NBTTagCompound sizeComp = new NBTTagCompound();
		sizeComp.setInteger("x", this.getSizeX());
		sizeComp.setInteger("y", this.getSizeY());
		sizeComp.setInteger("z", this.getSizeZ());
		
		root.setTag("size", sizeComp);
		
		root.setString("author", this.author);
		
		NBTTagCompound partsTag = new NBTTagCompound();
		NBTTagCompound bossesTag = new NBTTagCompound();
		
		int index = 0;
		int bossesCount = 0;
		for(BlockPos offset : this.structures.keySet()) {
			//Boss block code
			if(!this.structures.get(offset).getBosses().isEmpty()) {
				for(BossInfo boi : this.structures.get(offset).getBosses()) {
					boi.addToPos(offset);
					bossesTag.setTag("boss" + bossesCount, boi.getAsNBTTag());
					bossesCount++;
				}
			}
			//
			NBTTagCompound part = new NBTTagCompound();
			part = this.structures.get(offset).writeToNBT(part);
			NBTTagCompound offsetTag = new NBTTagCompound();
			offsetTag = NBTUtil.BlockPosToNBTTag(offset);
			part.setTag("offset", offsetTag);
			
			partsTag.setTag("p"+index, part);
			index++;
		}
		if(bossesCount > 0) {
			root.setInteger("bossesCount", bossesCount);
			root.setTag("bosses", bossesTag);
		}
		
		
		//System.out.println("Finishing NBT Compound...");
		root.setTag("parts", partsTag);
		//System.out.println("Saving to file...");
		//saveToFile(root);
		Thread fileSaveThread = null;
		fileSaveThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				saveToFile(root);
				System.out.println("DONE!");
				System.out.println("Exported file " + dataFile.getName() + " successfully!");
				if(placer != null) {
					placer.sendMessage(new TextComponentString("Exported " + dataFile.getName() + " successfully!"));
				} else {
					for(EntityPlayerMP playerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
						playerMP.sendMessage(new TextComponentString("Exported " + dataFile.getName() + " successfully!"));
					}
				}
				if(CQStructure.runningExportThreads.contains(Thread.currentThread())) {
					CQStructure.runningExportThreads.remove(Thread.currentThread());
				}
			}
		});
		CQStructure.runningExportThreads.add(fileSaveThread);
		fileSaveThread.setDaemon(true);
		fileSaveThread.start();
	}
	
	private void saveToFile(NBTTagCompound tag) {
		try {
			OutputStream outStream = null;
			outStream = new FileOutputStream(this.dataFile);
			CompressedStreamTools.writeCompressed(tag, outStream);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getSizeZ() {
		return sizeZ;
	}

	public void setSizeZ(int sizeZ) {
		this.sizeZ = sizeZ;
	}

	public File getDataFile() {
		return dataFile;
	}
	
	@Nullable
	public BlockPos getShieldCorePosition() {
		return this.shieldCorePosition;
	}

	public void setDataFile(File dataFile) {
		if(!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.dataFile = dataFile;
	}

	public Vec3i getSizeAsVec() {
		return new Vec3i(sizeX, sizeY, sizeZ);
	}

}
