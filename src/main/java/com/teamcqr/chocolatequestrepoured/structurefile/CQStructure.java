package com.teamcqr.chocolatequestrepoured.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class CQStructure {

	private File dataFile;
	
	private int sizeX;
	private int sizeY;
	private int sizeZ;
	
	private int parts = 0;
	private String author = "DerToaster98";
	
	//TODO: move structure origin to the center of it -> NOPE
	
	private HashMap<BlockPos, Structure> structures = new HashMap<BlockPos, Structure>();
	
	public CQStructure(String name) {
		this.setDataFile(new File(CQRMain.CQ_EXPORT_FILES_FOLDER, name + ".nbt"));
	}
	
	public CQStructure(File file) {
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
									
									Structure partStructure = new Structure();
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
	
	public void placeBlocksInWorld(World worldIn, BlockPos pos, PlacementSettings settings) {
		if(this.dataFile != null) {
			System.out.println("Generating structure: " + this.dataFile.getName() + "...");
			int partID = 1;
			for(BlockPos offset : this.structures.keySet()) {
				System.out.println("building part " + partID + " of " + this.structures.keySet().size() + "...");
				BlockPos offsetVec = Structure.transformedBlockPos(settings, offset);
				BlockPos pastePos = pos.add(offsetVec);
				this.structures.get(offset).addBlocksToWorld(worldIn, pastePos, settings);
				partID++;
			}
		}
	}
	
	//DONE?: Split structure into 16x16 grid 
	public void save(World worldIn, BlockPos posStart, BlockPos posEnd, boolean usePartMode) {
		BlockPos endPos = posEnd;
		BlockPos startPos = posStart;
		
		//Makes sure, that the end positions X and Z component is larger than the ones of the start pos
		if(posEnd.getX() < posStart.getX()) {
			endPos = new BlockPos(startPos.getX(), endPos.getY(), endPos.getZ());
			startPos = new BlockPos(endPos.getX(), startPos.getY(), startPos.getZ());
		}
		if(posEnd.getZ() < posStart.getZ()) {
			endPos = new BlockPos(endPos.getX(), endPos.getY(), startPos.getZ());
			startPos = new BlockPos(startPos.getX(), startPos.getY(), endPos.getZ());
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
							end = new BlockPos(endPos.getX(), end.getY(), end.getZ());
						}
						if((iZ +1) == zIterations) {
							end = new BlockPos(end.getX(), end.getY(), endPos.getZ());
						}
					}
					offset = start.subtract(startPos);
					
					Structure subPart = new Structure(partIndx);
					subPart.takeBlocksFromWorld(worldIn, start, end, true, Blocks.STRUCTURE_VOID);
					
					this.structures.put(new BlockPos(offset), subPart);
					partIndx++;
				}
			}
			this.parts = partIndx;
		} else {
			//Do not use the part mode -> Save as one huge block
			this.parts = 1;
			Structure struct = new Structure(0);
			struct.takeBlocksFromWorld(worldIn, startPos, endPos, true, Blocks.STRUCTURE_VOID);
			this.structures.put(new BlockPos(0,0,0), struct);
		}	
		writeNBT();
	}
	
	private void writeNBT() {
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
		
		int index = 0;
		for(BlockPos offset : this.structures.keySet()) {
			NBTTagCompound part = new NBTTagCompound();
			part = this.structures.get(offset).writeToNBT(part);
			NBTTagCompound offsetTag = new NBTTagCompound();
			offsetTag = NBTUtil.BlockPosToNBTTag(offset);
			part.setTag("offset", offsetTag);
			
			partsTag.setTag("p"+index, part);
			index++;
		}
		//System.out.println("Finishing NBT Compound...");
		root.setTag("parts", partsTag);
		//System.out.println("Saving to file...");
		//saveToFile(root);
		Thread fileSaveThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				saveToFile(root);
				System.out.println("DONE!");
				System.out.println("Exported file " + dataFile.getName() + " successfully!");
			}
		});
		fileSaveThread.setDaemon(true);
		fileSaveThread.start();
	}
	
	private void saveToFile(NBTTagCompound tag) {
		try {
			CompressedStreamTools.write(tag, this.dataFile);
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

}
