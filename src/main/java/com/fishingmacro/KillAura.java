package com.fishingmacro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;

public class KillAura {
	private static long lastAttackTime = 0;
	private static final long ATTACK_DELAY = 100; // ms between attacks
	private static final double KILL_AURA_RANGE = 6.0; // blocks
	private static LivingEntity currentTarget = null;

	public static void tick(MinecraftClient client) {
		if (client.player == null || client.world == null) {
			return;
		}

		// Find nearest hostile entity
		LivingEntity target = findNearestEnemy(client);

		if (target != null) {
			currentTarget = target;
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastAttackTime >= ATTACK_DELAY) {
				attackEntity(client, target);
				lastAttackTime = currentTime;
			}
		} else {
			currentTarget = null;
		}
	}

	private static LivingEntity findNearestEnemy(MinecraftClient client) {
		if (client.world == null || client.player == null) {
			return null;
		}

		return client.world.getEntities().stream()
			.filter(entity -> entity instanceof LivingEntity)
			.filter(entity -> entity != client.player)
			.filter(entity -> !isAlly(entity))
			.map(entity -> (LivingEntity) entity)
			.filter(entity -> !entity.isDead())
			.filter(entity -> client.player.distanceTo(entity) <= KILL_AURA_RANGE)
			.min(Comparator.comparingDouble(entity -> client.player.distanceTo(entity)))
			.orElse(null);
	}

	private static boolean isAlly(Entity entity) {
		// Filter out entities that shouldn't be attacked
		if (entity.hasCustomName()) {
			String name = entity.getCustomName().getString().toLowerCase();
			// Don't attack players or pets (customize as needed)
			if (name.contains("owner") || name.contains("pet")) {
				return true;
			}
		}
		return false;
	}

	private static void attackEntity(MinecraftClient client, LivingEntity target) {
		if (client.interactionManager == null || client.player == null) {
			return;
		}

		// Look at target
		lookAtEntity(client, target);

		// Attack
		client.interactionManager.attackEntity(client.player, target);
		client.player.swingHand(Hand.MAIN_HAND);
	}

	private static void lookAtEntity(MinecraftClient client, LivingEntity target) {
		if (client.player == null) return;

		Vec3d playerEyes = client.player.getEyePos();
		Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);

		double dx = targetPos.x - playerEyes.x;
		double dy = targetPos.y - playerEyes.y;
		double dz = targetPos.z - playerEyes.z;

		double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
		double yaw = Math.toDegrees(Math.atan2(-dx, dz));
		double pitch = Math.toDegrees(-Math.atan2(dy, horizontalDistance));

		client.player.setYaw((float) yaw);
		client.player.setPitch((float) Math.max(-90, Math.min(90, pitch)));
	}

	public static LivingEntity getCurrentTarget() {
		return currentTarget;
	}
}
