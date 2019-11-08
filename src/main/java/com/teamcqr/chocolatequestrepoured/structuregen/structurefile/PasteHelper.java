package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class PasteHelper {
	
	public static BlockPos getNewBlockPosForCorrectRotatedPlacement(BlockPos oldCenteredPastePos, CQStructure structure, Rotation rotation, EPosType posType) {
		return getnewBlockPosForCorrectRotatedPlacement(oldCenteredPastePos, structure.getSizeX(), structure.getSizeZ(), rotation, posType);
	}

	public static BlockPos getnewBlockPosForCorrectRotatedPlacement(BlockPos oldCenteredPastePos, int sizeX, int sizeZ, Rotation rotation, EPosType posType) {
		BlockPos pastePos = oldCenteredPastePos;
		//int sizeX = structure.getSizeX();
		//int sizeZ = structure.getSizeZ();
		switch(posType) {
		case CENTER_XZ_LAYER:
			pastePos = oldCenteredPastePos.subtract(new Vec3i(sizeX /2, 0, sizeZ /2));
			break;
		case CORNER_NE:
			pastePos = oldCenteredPastePos.subtract(new Vec3i(sizeX,0,0));
			break;
		case CORNER_SE:
			pastePos = oldCenteredPastePos.subtract(new Vec3i(sizeX,0,sizeZ));
			break;
		case CORNER_SW:
			pastePos = oldCenteredPastePos.subtract(new Vec3i(0,0,sizeZ));
			break;
		default:
			break;
		}
		Vec3i v = new Vec3i(pastePos.getX() - oldCenteredPastePos.getX(), 0, pastePos.getZ() - oldCenteredPastePos.getZ());
		switch(rotation) {
		case CLOCKWISE_180:
			v = VectorUtil.rotateVectorAroundY(v, 180D);
			break;
		case CLOCKWISE_90:
			v = VectorUtil.rotateVectorAroundY(v, 90D);
			break;
		case COUNTERCLOCKWISE_90:
			v = VectorUtil.rotateVectorAroundY(v, 270D);
			break;
		default:
			return oldCenteredPastePos;
		}
		BlockPos newCenteredPastePos = pastePos.add(v);
		Vec3i cppNewToCppOld = new Vec3i(oldCenteredPastePos.getX() - newCenteredPastePos.getX(), 0, oldCenteredPastePos.getZ() - newCenteredPastePos.getZ());
		
		BlockPos translatedPastePos = oldCenteredPastePos.add(cppNewToCppOld);

		return translatedPastePos;
	}

}
