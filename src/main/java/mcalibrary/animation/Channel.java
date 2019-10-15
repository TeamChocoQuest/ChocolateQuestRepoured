package mcalibrary.animation;

import java.util.HashMap;
import java.util.Map;

public class Channel {
	public static final byte LINEAR = 0;
	public static final byte LOOP = 1;
	public static final byte CYCLE = 2;
	public static final byte CUSTOM = 3;

	public String name;
	/** The speed of the whole channel (frames per second). */
	public float fps;
	/** Number of the frames of this channel. */
	public int totalFrames;
	/** KeyFrames. Key is the position of that keyFrame in the frames list. */
	public HashMap<Integer, KeyFrame> keyFrames = new HashMap<Integer, KeyFrame>();
	/** How this animation should behave: 0 = Normal; 1 = Loop; 2 = Cycle. */
	public byte mode = LINEAR;

	public Channel(String _name)
	{
		this.name = _name;
		this.totalFrames = 0;

		this.initializeAllFrames();
	}

	public Channel(String _name, float _fps, int _totalFrames, byte _mode)
	{
		this(_name);
		this.fps = _fps;
		this.totalFrames = _totalFrames;
		this.mode = _mode;
	}

	/** Create all the frames and add them in the list in the correct order. */
	protected void initializeAllFrames() {
		//Channels must initialize their frames inside this function.
	}

	/**
	 * Return the previous rotation KeyFrame before this frame that uses this box, if it exists. 
	 * If currentFrame is a keyFrame that uses this box, it is returned.
	 */
	public KeyFrame getPreviousRotationKeyFrameForBox(String boxName, float currentFrame)
	{
		int latestFramePosition = -1;
		KeyFrame latestKeyFrame = null;
		for (Map.Entry<Integer, KeyFrame> entry : keyFrames.entrySet()) {
			Integer key = entry.getKey();
			KeyFrame value = entry.getValue();

			if (key <= currentFrame && key > latestFramePosition) {
				if(value.useBoxInRotations(boxName))
				{
					latestFramePosition = key;
					latestKeyFrame = value;
				}
			}
		}

		return latestKeyFrame;
	}

	/**
	 * Return the next rotation KeyFrame before this frame that uses this box, if it exists. 
	 * If currentFrame is a keyFrame that uses this box, it is NOT considered.
	 */
	public KeyFrame getNextRotationKeyFrameForBox(String boxName, float currentFrame)
	{
		int nextFramePosition = -1;
		KeyFrame nextKeyFrame = null;
		for (Map.Entry<Integer, KeyFrame> entry : keyFrames.entrySet()) {
			Integer key = entry.getKey();
			KeyFrame value = entry.getValue();

			if (key > currentFrame && (key < nextFramePosition || nextFramePosition == -1)) {
				if(value.useBoxInRotations(boxName))
				{
					nextFramePosition = key;
					nextKeyFrame = value;
				}
			}
		}

		return nextKeyFrame;
	}

	/**
	 * Return the previous translation KeyFrame before this frame that uses this box, if it exists. 
	 * If curretFrame is a keyFrame that uses this box, it is returned.
	 */
	public KeyFrame getPreviousTranslationKeyFrameForBox(String boxName, float currentFrame)
	{
		int latestFramePosition = -1;
		KeyFrame latestKeyFrame = null;
		for (Map.Entry<Integer, KeyFrame> entry : keyFrames.entrySet()) {
			Integer key = entry.getKey();
			KeyFrame value = entry.getValue();

			if (key <= currentFrame && key > latestFramePosition) {
				if(value.useBoxInTranslations(boxName))
				{
					latestFramePosition = key;
					latestKeyFrame = value;
				}
			}
		}

		return latestKeyFrame;
	}

	/**
	 * Return the next translation KeyFrame before this frame that uses this box, if it exists. 
	 * If currentFrame is a keyFrame that uses this box, it is NOT considered.
	 */
	public KeyFrame getNextTranslationKeyFrameForBox(String boxName, float currentFrame)
	{
		int nextFramePosition = -1;
		KeyFrame nextKeyFrame = null;
		for (Map.Entry<Integer, KeyFrame> entry : keyFrames.entrySet()) {
			Integer key = entry.getKey();
			KeyFrame value = entry.getValue();

			if (key > currentFrame && (key < nextFramePosition || nextFramePosition == -1)) {
				if(value.useBoxInTranslations(boxName))
				{
					nextFramePosition = key;
					nextKeyFrame = value;
				}
			}
		}

		return nextKeyFrame;
	}

	/**
	 * Get the position of the keyframe in this animation, if the keyframe
	 * exists.
	 */
	public int getKeyFramePosition(KeyFrame keyFrame) {
		if (keyFrame != null) {
			for (Map.Entry<Integer, KeyFrame> entry : this.keyFrames.entrySet()) {
				Integer key = entry.getKey();
				KeyFrame keyframe = entry.getValue();

				if (keyframe == keyFrame) {
					return key;
				}
			}
		}
		return -1;
	}

	/** Check if an animation should stop, restart or whatever. */
	public static boolean shouldAnimationStop() {	
		return false;
	}
}