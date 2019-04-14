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
	
	//TODO: move structure origin to the center of it
	
	//private List<Structure> subStructures = new ArrayList<Structure>();
	private HashMap<BlockPos, Structure> structures = new HashMap<BlockPos, Structure>();
	
	public CQStructure(String name) {
		this.setDataFile(new File(CQRMain.CQ_DUNGEON_FOLDER, name + ".nbt"));
	}
	
	public CQStructure(File file) {
		if(file.isFile() && file.getName().split(".")[1].equalsIgnoreCase("nbt")) {
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
		System.out.println("Generating structure: " + this.dataFile.getName() + "...");
		int partID = 1;
		for(BlockPos offset : this.structures.keySet()) {
			System.out.println("building part " + partID + " of " + this.structures.keySet().size() + "...");
			BlockPos pastePos = pos.add(offset);
			this.structures.get(offset).addBlocksToWorld(worldIn, pastePos, settings);
			partID++;
		}
	}
	
	public void save(World worldIn, BlockPos startPos, BlockPos endPos) {
		int x = startPos.getX();
		int z = startPos.getZ();
		
		this.setSizeX(endPos.getX() - startPos.getX());
		this.setSizeY(endPos.getY() - startPos.getY());
		this.setSizeZ(endPos.getZ() - startPos.getZ());
		
		if(Math.abs(endPos.getX() - x) > 16 || Math.abs(endPos.getZ() - z) > 16) {
			BlockPos start = startPos;
			BlockPos end = start.add(16, 0, 16);
			int i = 0;
			for(int iX = 0; iX < this.getSizeX() / 16; iX++) {
				for(int iZ = 0; iZ < this.getSizeZ() / 16; iZ++) {
					Structure subPart = new Structure(i);
					
					start = startPos.add(iX * 16, 0, iZ *16);
					end = start.add(15, 0, 15);
					
					subPart.takeBlocksFromWorld(worldIn, start, end, true, Blocks.STRUCTURE_VOID);
					this.structures.put(new BlockPos(iX * 16, 0, iZ * 16), subPart);
					
					i++;
				}
			}
			this.parts = i +1;
		} else {
			Structure subPart = new Structure(0);
			subPart.takeBlocksFromWorld(worldIn, startPos, endPos, true, Blocks.STRUCTURE_VOID);
			this.structures.put(new BlockPos(0, 0, 0), subPart);
			this.parts = 1;
		}
		
		writeNBT();
	}
	
	private void writeNBT() {
		NBTTagCompound root = new NBTTagCompound();
		root.setString("type", "CQ_Structure");
		root.setInteger("partcount", this.parts);
		
		NBTTagCompound sizeComp = new NBTTagCompound();
		sizeComp.setInteger("x", this.getSizeX());
		sizeComp.setInteger("y", this.getSizeY());
		sizeComp.setInteger("z", this.getSizeZ());
		
		root.setTag("size", sizeComp);
		
		root.setString("author", this.author);
		
		NBTTagCompound parts = new NBTTagCompound();
		
		for(BlockPos offset : this.structures.keySet()) {
			NBTTagCompound part = new NBTTagCompound();
			this.structures.get(offset).writeToNBT(part);
			NBTTagCompound offsetTag = new NBTTagCompound();
			offsetTag = NBTUtil.BlockPosToNBTTag(offset);
			part.setTag("offset", offsetTag);
			
			parts.setTag("p"+this.structures.get(offset).getPart_id(), part);
		}
		
		root.setTag("parts", parts);
		
		try {
			CompressedStreamTools.write(root, this.dataFile);
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
		this.dataFile = dataFile;
	}

}
