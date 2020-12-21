package team.cqr.cqrepoured.client.models;

import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;

public interface IBipedModel {
	
	GeoCube getRightHandCube();
	GeoCube getLeftHandCube();
	GeoBone getRightArm();
	GeoBone getLeftArm();
	GeoBone getBody();
	GeoBone getRightLeg();
	GeoBone getLeftLeg();
	GeoBone getHead();
	GeoBone getCape();

}
