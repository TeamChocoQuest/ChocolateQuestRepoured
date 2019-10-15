package mcalibrary.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mcalibrary.IMCAnimatedEntity;
import mcalibrary.client.MCAModelRenderer;
import mcalibrary.math.Quaternion;
import mcalibrary.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AnimationHandler {
	public static AnimTickHandler animTickHandler;
	/** Owner of this handler. */
	private IMCAnimatedEntity animatedEntity;
	/** List of all the activate animations of this Entity. */
	public ArrayList<Channel> animCurrentChannels = new ArrayList<>();
	/** Previous time of every active animation. */
	public HashMap<String, Long> animPrevTime = new HashMap<String, Long>();
	/** Current frame of every active animation. */
	public HashMap<String, Float> animCurrentFrame = new HashMap<String, Float>();
	/** Contains the unique names of the events that have been already fired during each animation.
	 * It becomes empty at the end of every animation. The key is the animation name and the value is the list of already-called events. */
	private HashMap<String, ArrayList<String>> animationEvents = new HashMap<String, ArrayList<String>>();

	public AnimationHandler(IMCAnimatedEntity entity) {
		if(animTickHandler == null) {
			animTickHandler = new AnimTickHandler();
		}
		animTickHandler.addEntity(entity);

		animatedEntity = entity;
	}
	
	public IMCAnimatedEntity getEntity() {
		return animatedEntity;
	}

	public void activateAnimation(HashMap<String, Channel> animChannels, String name, float startingFrame) {
		if(animChannels.get(name) != null)
		{
			Channel selectedChannel = animChannels.get(name);
			int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
			if(indexToRemove != -1)
			{
				animCurrentChannels.remove(indexToRemove);
			}

			animCurrentChannels.add(selectedChannel);
			animPrevTime.put(name, System.nanoTime());
			animCurrentFrame.put(name, startingFrame);
			if(animationEvents.get(name) == null){
				animationEvents.put(name, new ArrayList<String>());
			}
		} else {
			System.out.println("The animation called "+name+" doesn't exist!");
		}
	}

	public abstract void activateAnimation(String name, float startingFrame);

	public void stopAnimation(HashMap<String, Channel> animChannels, String name) {
		Channel selectedChannel = animChannels.get(name);
		if(selectedChannel != null)
		{
			int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
			if(indexToRemove != -1)
			{
				animCurrentChannels.remove(indexToRemove);
				animPrevTime.remove(name);
				animCurrentFrame.remove(name);
				animationEvents.get(name).clear();
			}
		} else {
			System.out.println("The animation called "+name+" doesn't exist!");
		}
	}

	public abstract void stopAnimation(String name);

	public void animationsUpdate() {
		for(Iterator<Channel> it = animCurrentChannels.iterator(); it.hasNext();)
		{
			Channel anim = it.next();
			float prevFrame = animCurrentFrame.get(anim.name);
			boolean animStatus = updateAnimation(animatedEntity, anim, animPrevTime, animCurrentFrame);
			if(animCurrentFrame.get(anim.name) != null)
			{
				fireAnimationEvent(anim, prevFrame, animCurrentFrame.get(anim.name));
			}
			if(!animStatus)
			{
				it.remove();
				animPrevTime.remove(anim.name);
				animCurrentFrame.remove(anim.name);
				animationEvents.get(anim.name).clear();
			}
		}
	}

	public boolean isAnimationActive(String name) {
		boolean animAlreadyUsed = false;
		for(Channel anim : animatedEntity.getAnimationHandler().animCurrentChannels)
		{
			if(anim.name.equals(name))
			{
				animAlreadyUsed = true;
				break;
			}
		}

		return animAlreadyUsed;
	}

	private void fireAnimationEvent(Channel anim, float prevFrame, float frame)
	{
		if(isWorldRemote(animatedEntity))
		{
			fireAnimationEventClientSide(anim, prevFrame, frame);
		} else {
			fireAnimationEventServerSide(anim, prevFrame, frame);
		}
	}

	@SideOnly(Side.CLIENT)
	public abstract void fireAnimationEventClientSide(Channel anim, float prevFrame, float frame);

	public abstract void fireAnimationEventServerSide(Channel anim, float prevFrame, float frame);
	
	/** Check if the animation event has already been called. */
	public boolean alreadyCalledEvent(String animName, String eventName) {
		if(animationEvents.get(animName) == null) {
			System.out.println("Cannot check for event "+eventName+"! Animation "+animName+"does not exist or is not active.");
			return true;
		}
		return animationEvents.get(animName).contains(eventName);
	}
	
	/** Set the animation event as "called", so it won't be fired again. */
	public void setCalledEvent(String animName, String eventName) {
		if(animationEvents.get(animName) != null) {
			animationEvents.get(animName).add(eventName);
		} else {
			System.out.println("Cannot set event "+eventName+"! Animation "+animName+"does not exist or is not active.");
		}
	}

	/** Update animation values. Return false if the animation should stop. */
	public static boolean updateAnimation(IMCAnimatedEntity entity, Channel channel, HashMap<String, Long> prevTimeAnim, HashMap<String, Float> prevFrameAnim)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer() || (FMLCommonHandler.instance().getEffectiveSide().isClient() && !isGamePaused()))
		{
			if(!(channel.mode == Channel.CUSTOM))
			{
				long prevTime = prevTimeAnim.get(channel.name);
				float prevFrame = prevFrameAnim.get(channel.name);

				long currentTime = System.nanoTime();
				double deltaTime = (currentTime-prevTime) / 1000000000.0;
				float numberOfSkippedFrames = (float) (deltaTime * channel.fps);

				float currentFrame = prevFrame + numberOfSkippedFrames;

				if(currentFrame < channel.totalFrames-1) //-1 as the first frame mustn't be "executed" as it is the starting situation
				{
					prevTimeAnim.put(channel.name, currentTime);
					prevFrameAnim.put(channel.name, currentFrame);
					return true;
				} else {
					if(channel.mode == Channel.LOOP)
					{
						prevTimeAnim.put(channel.name, currentTime);
						prevFrameAnim.put(channel.name, 0F);
						return true;
					}
					return false;
				}
			} else {
				return true;
			}
		} else {
			long currentTime = System.nanoTime();
			prevTimeAnim.put(channel.name, currentTime);
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	private static boolean isGamePaused() {
		net.minecraft.client.Minecraft MC = net.minecraft.client.Minecraft.getMinecraft();
		return MC.isSingleplayer() && MC.currentScreen != null && MC.currentScreen.doesGuiPauseGame() && !MC.getIntegratedServer().getPublic();
	}

	/** Apply animations if running or apply initial values.
	 * Must be called only by the model class.
	 */
	@SideOnly(Side.CLIENT)
	public static void performAnimationInModel(HashMap<String, MCAModelRenderer> parts, IMCAnimatedEntity entity)
	{
		for (Map.Entry<String, MCAModelRenderer> entry : parts.entrySet()) {
			String boxName = entry.getKey();
			MCAModelRenderer box = entry.getValue();

			boolean anyRotationApplied = false;
			boolean anyTranslationApplied = false;
			boolean anyCustomAnimationRunning = false;

			for(Channel channel : entity.getAnimationHandler().animCurrentChannels)
			{
				if(channel.mode != Channel.CUSTOM) {
					float currentFrame = entity.getAnimationHandler().animCurrentFrame.get(channel.name);

					//Rotations
					KeyFrame prevRotationKeyFrame = channel.getPreviousRotationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int prevRotationKeyFramePosition = prevRotationKeyFrame != null ? channel.getKeyFramePosition(prevRotationKeyFrame) : 0;

					KeyFrame nextRotationKeyFrame = channel.getNextRotationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int nextRotationKeyFramePosition = nextRotationKeyFrame != null ? channel.getKeyFramePosition(nextRotationKeyFrame) : 0;

					float SLERPProgress = (currentFrame - (float)prevRotationKeyFramePosition) / ((float)(nextRotationKeyFramePosition - prevRotationKeyFramePosition));
					if(SLERPProgress > 1F || SLERPProgress < 0F)
					{
						SLERPProgress = 1F;
					}

					if(prevRotationKeyFramePosition == 0 && prevRotationKeyFrame == null  && !(nextRotationKeyFramePosition == 0))
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.slerp(parts.get(boxName).getDefaultRotationAsQuaternion(), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.getRotationMatrix().set(currentQuat).transpose();

						anyRotationApplied = true;
					} else if(prevRotationKeyFramePosition == 0 && prevRotationKeyFrame != null && !(nextRotationKeyFramePosition == 0))
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.slerp(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.getRotationMatrix().set(currentQuat).transpose();

						anyRotationApplied = true;
					} else if(!(prevRotationKeyFramePosition == 0) && !(nextRotationKeyFramePosition == 0))
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.slerp(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.getRotationMatrix().set(currentQuat).transpose();

						anyRotationApplied = true;
					}


					//Translations
					KeyFrame prevTranslationKeyFrame = channel.getPreviousTranslationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int prevTranslationsKeyFramePosition = prevTranslationKeyFrame != null ? channel.getKeyFramePosition(prevTranslationKeyFrame) : 0;

					KeyFrame nextTranslationKeyFrame = channel.getNextTranslationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int nextTranslationsKeyFramePosition = nextTranslationKeyFrame != null ? channel.getKeyFramePosition(nextTranslationKeyFrame) : 0;

					float LERPProgress = (currentFrame - (float)prevTranslationsKeyFramePosition) / ((float)(nextTranslationsKeyFramePosition - prevTranslationsKeyFramePosition));
					if(LERPProgress > 1F)
					{
						LERPProgress = 1F;
					}

					if(prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame == null && !(nextTranslationsKeyFramePosition == 0))
					{
						Vector3f startPosition = parts.get(boxName).getPositionAsVector();
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

						anyTranslationApplied = true;
					} else if(prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame != null && !(nextTranslationsKeyFramePosition == 0))
					{
						Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);
					} else if(!(prevTranslationsKeyFramePosition == 0) && !(nextTranslationsKeyFramePosition == 0))
					{
						Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

						anyTranslationApplied = true;
					}
				} else {
					anyCustomAnimationRunning = true;

					((CustomChannel)channel).update(parts, entity);
				}
			}

			//Set the initial values for each box if necessary
			if(!anyRotationApplied && !anyCustomAnimationRunning)
			{
				box.resetRotationMatrix();
			}
			if(!anyTranslationApplied && !anyCustomAnimationRunning)
			{
				box.resetRotationPoint();				
			}
		}
	}

	public static boolean isWorldRemote(IMCAnimatedEntity animatedEntity) {
		return ((Entity)animatedEntity).world.isRemote;
	}
}
