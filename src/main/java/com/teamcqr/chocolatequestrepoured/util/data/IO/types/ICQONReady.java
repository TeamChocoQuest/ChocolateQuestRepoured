package com.teamcqr.chocolatequestrepoured.util.data.IO.types;

/**
 * Allows implementing classes to be serialized to CQON by
 * CQONConversionUtil without the need for a valid LUT entry
 * to be provided for each time the convertObjectToCQON
 * method is called.
 *
 * Simplest implementation that saves every variable:
 * public String[] getCQONFieldNames() {
 *     return IntrusiveModificationHelper.reflectGetAllFieldNames(this);
 * }
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 22.07.19
 */
public interface ICQONReady {

    String[] getCQONFieldNames();

}
