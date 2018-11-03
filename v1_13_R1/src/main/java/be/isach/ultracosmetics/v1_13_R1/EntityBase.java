package be.isach.ultracosmetics.v1_13_R1;

/**
 * @author RadBuilder
 */
public interface EntityBase {
	
	void g_(float strafe, float vertical, float forward);
	
	float getSpeed();
	
	boolean canFly();
}
