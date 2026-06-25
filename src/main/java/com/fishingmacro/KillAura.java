package com.fishingmacro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class KillAura {
	private static long lastAttackTime = 0;
	private static final long ATTACK_DELAY = 200; // ms between attacks (slower = less suspicious)
	private static final double KILL_AURA_RANGE = 8.0; // blocks
	private static final double CAMERA_ROTATION_SPEED = 15.0; // degrees per tick (human-like)
	private static LivingEntity currentTarget = null;
	private static int swordSlot = 1; // Default to slot 2 (index 1)
	private static int fishingSlot = 2; // Default to slot 3 (index 2)

	public static void tick(MinecraftClient client) {
		if (client.player == null || client.world == null) {
			return;
		}

		// Find nearest Sea Creature with level marker
		LivingEntity target = findNearestSeaCreature(client);

		if (target != null && isTargetVisible(client, target)) {
			currentTarget = target;
			long currentTime = System.currentTimeMillis();
			
			if (currentTime - lastAttackTime >= ATTACK_DELAY) {
				// Smoothly rotate camera to face target
				rotateTowardsTarget(client, target);
				
				// Attack after a small delay (let camera catch up)
				if (isFacingTarget(client, target)) {
					switchToSwordAndAttack(client);
					lastAttackTime = currentTime;
				}
			}
		} else {
			currentTarget = null;
			// Return to fishing after attack
			if (!client.player.getInventory().getStack(fishingSlot).isEmpty()) {
				client.player.getInventory().selectedSlot = fishingSlot;
			}
		}
	}

	private static LivingEntity findNearestSeaCreature(MinecraftClient client) {
		if (client.world == null || client.player == null) {
			return null;
		}

		return client.world.getEntities().stream()
			.filter(entity -> entity instanceof LivingEntity)
			.filter(entity -> entity != client.player)
			.map(entity -> (LivingEntity) entity)
			.filter(entity -> !entity.isDead())
			.filter(entity -> client.player.distanceTo(entity) <= KILL_AURA_RANGE)
			// KEY: Only attack mobs with level markers like [Lv10], [Lv20], etc.
			.filter(entity -> hasLevelMarker(entity))
			.min(Comparator.comparingDouble(entity -> client.player.distanceTo(entity)))
			.orElse(null);
	}

	private static boolean hasLevelMarker(LivingEntity entity) {
		if (!entity.hasCustomName()) {
			return false;
		}
		
		String name = entity.getCustomName().getString();
		// Check for Hypixel-style level markers: [Lv10], [Lv20], etc.
		return name.matches(".*\\[Lv\\d+\\].*");
	}

	private static boolean isTargetVisible(MinecraftClient client, LivingEntity target) {
		if (client.player == null) return false;
		
		// Simple raycast check - if we can see the target
		Vec3d playerEyes = client.player.getEyePos();
		Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);
		
		// Check if there's a direct line of sight
		HitResult hit = client.world.raycast(new net.minecraft.util.math.RaycastContext(
			playerEyes,
			targetPos,
			net.minecraft.util.math.RaycastContext.ShapeType.COLLIDER,
			net.minecraft.util.math.RaycastContext.FluidHandling.NONE,
			client.player
		));
		
		return hit.getType() == HitResult.Type.MISS || 
			   (hit.getType() == HitResult.Type.ENTITY && 
				((net.minecraft.util.hit.EntityHitResult) hit).getEntity() == target);
	}

	private static void rotateTowardsTarget(MinecraftClient client, LivingEntity target) {
		if (client.player == null) return;

		Vec3d playerEyes = client.player.getEyePos();
		Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);

		double dx = targetPos.x - playerEyes.x;
		double dy = targetPos.y - playerEyes.y;
		double dz = targetPos.z - playerEyes.z;

		double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
		double targetYaw = Math.toDegrees(Math.atan2(-dx, dz));
		double targetPitch = Math.toDegrees(-Math.atan2(dy, horizontalDistance));

		// Smooth rotation - human-like movement
		float currentYaw = client.player.getYaw();
		float currentPitch = client.player.getPitch();

		float yawDiff = normalizeAngle((float) targetYaw - currentYaw);
		float pitchDiff = (float) targetPitch - currentPitch;

		// Limit rotation speed to avoid bans
		float maxRotation = (float) CAMERA_ROTATION_SPEED;
		yawDiff = Math.max(-maxRotation, Math.min(maxRotation, yawDiff));
		pitchDiff = Math.max(-maxRotation, Math.min(maxRotation, pitchDiff));

		client.player.setYaw(currentYaw + yawDiff);
		client.player.setPitch(Math.max(-90, Math.min(90, currentPitch + pitchDiff)));
	}

	private static float normalizeAngle(float angle) {
		while (angle > 180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}

	private static boolean isFacingTarget(MinecraftClient client, LivingEntity target) {
		if (client.player == null) return false;

		Vec3d playerLook = client.player.getRotationVector();
		Vec3d playerEyes = client.player.getEyePos();
		Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);
		Vec3d toTarget = targetPos.subtract(playerEyes).normalize();

		// If we're looking within 30 degrees of the target, we're facing it
		double dotProduct = playerLook.dotProduct(toTarget);
		return dotProduct > 0.866; // cos(30°) ≈ 0.866
	}

	private static void switchToSwordAndAttack(MinecraftClient client) {
		if (client.interactionManager == null || client.player == null) {
			return;
		}

		// Switch to sword slot
		if (!client.player.getInventory().getStack(swordSlot).isEmpty()) {
			client.player.getInventory().selectedSlot = swordSlot;
		}

		// Attack current target
		if (currentTarget != null && !currentTarget.isDead()) {
			client.interactionManager.attackEntity(client.player, currentTarget);
			client.player.swingHand(Hand.MAIN_HAND);
		}
	}

	public static LivingEntity getCurrentTarget() {
		return currentTarget;
	}

	// Allow player to configure sword and fishing slots
	public static void setSwordSlot(int slot) {
		swordSlot = Math.max(0, Math.min(8, slot));
	}

	public static void setFishingSlot(int slot) {
		fishingSlot = Math.max(0, Math.min(8, slot));
	}
}
