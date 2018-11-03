package be.isach.ultracosmetics.v1_13_R2.customentities;

import be.isach.ultracosmetics.cosmetics.mounts.IMountCustomEntity;
import be.isach.ultracosmetics.v1_13_R2.EntityBase;
import be.isach.ultracosmetics.v1_13_R2.nms.WrapperEntityHuman;
import be.isach.ultracosmetics.v1_13_R2.nms.WrapperEntityInsentient;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.entity.Entity;

/**
 * @author RadBuilder
 */
public class CustomSlime extends EntitySlime implements IMountCustomEntity, EntityBase {
	
	public CustomSlime(World world) {
		super(world);
	}
	
	@Override
	public void a(float strafe, float vertical, float forward) {
		if (!CustomEntities.customEntities.contains(this)) {
			super.a(strafe, vertical, forward);
			return;
		}
		EntityHuman passenger = null;
		if (!bP().isEmpty()) {
			passenger = (EntityHuman) bP().get(0);
		}
		ride(strafe, vertical, forward, passenger, this);
	}
	
	@Override
	public String getName() {
		return LocaleLanguage.a().a("entity.Slime.name");
	}
	
	
	@Override
	public void g_(float strafe, float vertical, float forward) {
		super.a(strafe, vertical, forward);
	}
	
	@Override
	public float getSpeed() {
		return 1.75f;
	}
	
	@Override
	public boolean canFly() {
		return false;
	}
	
	@Override
	public Entity getEntity() {
		return getBukkitEntity();
	}
	
	static void ride(float strafe, float vertical, float forward, EntityHuman passenger, EntityInsentient entity) {
		if (!(entity instanceof EntityBase))
			throw new IllegalArgumentException("The entity field should implements EntityBase");
		
		EntityBase entityBase = (EntityBase) entity;
		
		WrapperEntityInsentient wEntity = new WrapperEntityInsentient(entity);
		WrapperEntityHuman wPassenger = new WrapperEntityHuman(passenger);
		
		if (passenger != null) {
			entity.lastYaw = entity.yaw = passenger.yaw % 360f;
			entity.pitch = (passenger.pitch * 0.5F) % 360f;
			
			wEntity.setRenderYawOffset(entity.yaw);
			wEntity.setRotationYawHead(entity.yaw);
			
			strafe = wPassenger.getMoveStrafing() * 0.25f;
			forward = wPassenger.getMoveForward() * 0.5f;
			
			if (forward <= 0.0F)
				forward *= 0.25F;
			
			wEntity.setJumping(wPassenger.isJumping());
			
			if (wPassenger.isJumping() && (entity.onGround || entityBase.canFly())) {
				entity.motY = 0.4D;
				
				float f2 = MathHelper.sin(entity.yaw * 0.017453292f);
				float f3 = MathHelper.cos(entity.yaw * 0.017453292f);
				entity.motX += (double) (-0.4f * f2);
				entity.motZ += (double) (0.4f * f3);
			}
			
			wEntity.setStepHeight(1.0f);
			wEntity.setJumpMovementFactor(wEntity.getMoveSpeed() * 0.1f);
			
			wEntity.setRotationYawHead(entity.yaw);
			
			wEntity.setMoveSpeed(0.35f * entityBase.getSpeed());
			entityBase.g_(strafe, vertical, forward);
			
			wEntity.setPrevLimbSwingAmount(wEntity.getLimbSwingAmount());
			
			double dx = entity.locX - entity.lastX;
			double dz = entity.locZ - entity.lastZ;
			
			float f4 = MathHelper.sqrt(dx * dx + dz * dz) * 4;
			
			if (f4 > 1)
				f4 = 1;
			
			wEntity.setLimbSwingAmount(wEntity.getLimbSwingAmount() + (f4 - wEntity.getLimbSwingAmount()) * 0.4f);
			wEntity.setLimbSwing(wEntity.getLimbSwing() + wEntity.getLimbSwingAmount());
		} else {
			wEntity.setStepHeight(0.5f);
			wEntity.setJumpMovementFactor(0.02f);
			
			entityBase.g_(strafe, vertical, forward);
		}
	}
	
	@Override
	public void removeAi() {
//		setNoAI(true);
	}
}
