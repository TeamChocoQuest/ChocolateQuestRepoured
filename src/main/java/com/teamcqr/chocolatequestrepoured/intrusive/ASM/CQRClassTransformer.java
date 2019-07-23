package com.teamcqr.chocolatequestrepoured.intrusive.ASM;

import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used by the CQR IntrusiveModificationHelper for ASM-related tasks
 * Should not be interacted with directly unless you 1000% know what you're doing
 * @author jdawg3636
 */
public class CQRClassTransformer implements IClassTransformer {

    // Transform list and relevant data
    private ArrayList<String> fullMCPNamesOfClassesToBeTransformed = new ArrayList<>();

    public ArrayList<String> getListToBeTransformed(){
        return fullMCPNamesOfClassesToBeTransformed;
    }

    // Constructor
    public CQRClassTransformer(){
        fullMCPNamesOfClassesToBeTransformed.add("net.minecraft.block.BlockFire"); //temp until generalization
    }

    /**
     * FML: Part of IClassTransformer - Called for every class?
     *
     * CQR: This is the central method responsible for actually modifying incoming bytecode
     *
     * @param className Name of class in current environment
     * @param mcpClassName MCP Name (May equal className depending on context)
     * @param incomingClassAsBytecode Raw, ones-and-zeros representation of compiled class being supplied by FML
     * @return Post-Transform version of incomingClassAsBytecode
     */
    @Override
    public byte[] transform(String className, String mcpClassName, byte[] incomingClassAsBytecode) {
        if (Arrays.asList(getListToBeTransformed()).indexOf(mcpClassName) != -1) {
            // Class not in list: do not modify
            return incomingClassAsBytecode;
        } else {
            // Class in list: modify
            return incomingClassAsBytecode;
        }
    }

}
