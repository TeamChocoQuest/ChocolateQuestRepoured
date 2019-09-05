package com.teamcqr.chocolatequestrepoured.util;

import com.teamcqr.chocolatequestrepoured.util.data.ArchiveManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.IO.FileIOUtil;

import java.util.HashMap;

/**
 * Alternative entry point for debugging algorithms
 * Does not launch the game or perform any actions not explicitly defined in this file
 * For debugging only
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 05.09.19
 */
public class DebugEntryPoint {
    public static void main(String[] args) {

        // Example use case for debugging ArchiveManipulationUtil's unzip method

        /*
        HashMap<String, byte[]> unzipped = ArchiveManipulationUtil.unzip(FileIOUtil.loadFromFile("S:\\Libraries\\Documents\\_Project Odin\\newmodworkspace\\_extern\\ChocolateQuestRepoured\\Forge Mapping Files for CQR.zip"));

        for(String fileName : unzipped.keySet()) {
            FileIOUtil.saveToFile("S:\\Libraries\\Documents\\_Project Odin\\newmodworkspace\\_extern\\ChocolateQuestRepoured\\mappings\\" + fileName, unzipped.get(fileName));
        }
        */

    }
}
