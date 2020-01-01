package mcalibrary.animation;

import java.util.HashMap;

import mcalibrary.math.Quaternion;
import mcalibrary.math.Vector3f;

public class KeyFrame {
	public HashMap<String, Quaternion> modelRenderersRotations = new HashMap<String, Quaternion>();
	public HashMap<String, Vector3f> modelRenderersTranslations = new HashMap<String, Vector3f>();

	public boolean useBoxInRotations(String boxName) {
		return this.modelRenderersRotations.get(boxName) != null;
	}

	public boolean useBoxInTranslations(String boxName) {
		return this.modelRenderersTranslations.get(boxName) != null;
	}
}