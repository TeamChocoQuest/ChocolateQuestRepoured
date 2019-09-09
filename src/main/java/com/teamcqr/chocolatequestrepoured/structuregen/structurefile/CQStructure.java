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
	
	public CQStructure(File file, boolean hasShield) {
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
									
									CQStructurePart partStructure = new CQStructurePart();
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

	//DONE?: Split structure into 16x16 grid 
	public void save(World worldIn, BlockPos posStart, BlockPos posEnd, boolean usePartMode, EntityPlayer placer) {
		BlockPos endPos = posEnd;
		BlockPos startPos = posStart;
		
		//Makes sure, that the end positions X and Z component is larger than the ones of the start pos
		if(posEnd.getX() < posStart.getX()) {
			endPos = new BlockPos(posStart.getX(), endPos.getY(), endPos.getZ());
			startPos = new BlockPos(posEnd.getX(), startPos.getY(), startPos.getZ());
		}
		if(posEnd.getZ() < posStart.getZ()) {
			endPos = new BlockPos(endPos.getX(), endPos.getY(), posStart.getZ());
			startPos = new BlockPos(startPos.getX(), startPos.getY(), posEnd.getZ());
		}
		
		this.setSizeX(endPos.getX() != startPos.getX() ? endPos.getX() - startPos.getX() : 1);
		this.setSizeY(endPos.getY() != startPos.getY() ? endPos.getY() - startPos.getY() : 1);
		this.setSizeZ(endPos.getZ() != startPos.getZ() ? endPos.getZ() - startPos.getZ() : 1);
		
		//DONE: make reflection thing faster / do it another time (e.g. when creating the json?) and pass it to a thread
		//Solution: move saving  a w a y  from GUI, move it into the tile entity section
		//Problem was not the reflection thing, it was that minecraft handles the "endPos" as a kind of Offset and not an actual location :D
		
		int distX = endPos.getX() - startPos.getX();
		int distZ = endPos.getZ() - startPos.getZ();
		
		if((Math.abs(distX) > 32 && Math.abs(distZ) > 32) && usePartMode) {
			//Use part mode and cut the structure into multiple smaller 16xHEIGHTx16 cubes
			
			int partIndx = 0;
			
			BlockPos start = new BlockPos(startPos);
			BlockPos end = new BlockPos(start.add(16, this.sizeY, 16));
			BlockPos offset = new BlockPos(0, 0, 0);
			
			int xIterations = this.sizeX / 16;
			int zIterations = this.sizeZ / 16;
			
			for(int iX = 0; iX < xIterations; iX++) {
				for(int iZ = 0; iZ < zIterations; iZ++) {
					start = new BlockPos(startPos.add(16 *iX, 0, 16 *iZ));
					start = start.add(iX != 0 ? 1 : 0, 0,  iZ != 0 ? 1: 0);
					end = new BlockPos(start.add(16, this.sizeY, 16));
					
					if((iX +1) == xIterations || (iZ +1) == zIterations) {
						//This section is for parts standing out of the grid...
						if((iX +1) == xIterations) {
							end = new BlockPos(posEnd.getX(), end.getY(), end.getZ());
						}
						if((iZ +1) == zIterations) {
							end = new BlockPos(end.getX(), end.getY(), posEnd.getZ());
						}
					}
					offset = start.subtract(startPos);
					
					CQStructurePart subPart = new CQStructurePart(partIndx);
					subPart.takeBlocksFromWorld(worldIn, start, end, true, Blocks.STRUCTURE_VOID);
					
					this.structures.put(new BlockPos(offset), subPart);
					partIndx++;
				}
			}
			this.parts = partIndx;
		} else {
			//Do not use the part mode -> Save as one huge block
			this.parts = 1;
			CQStructurePart struct = new CQStructurePart(0);
			struct.takeBlocksFromWorld(worldIn, startPos, endPos, true, Blocks.STRUCTURE_VOID);
			this.structures.put(new BlockPos(0,0,0), struct);
		}	
		writeNBT(placer);
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
