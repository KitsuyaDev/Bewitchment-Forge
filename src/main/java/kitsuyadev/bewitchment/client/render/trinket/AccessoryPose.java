/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

final class AccessoryPose {
	private AccessoryPose() {
	}

	static void chest(PoseStack pose, PlayerModel<?> model, LivingEntity entity) {
		if (entity.isCrouching() && !model.riding && !entity.isSwimming()) {
			pose.translate(0, 0.2F, 0);
			pose.mulPose(Axis.XP.rotation(model.body.xRot));
		}
		pose.mulPose(Axis.YP.rotation(model.body.yRot));
		pose.translate(0, 0.4F, -0.16F);
	}

	static void leg(PoseStack pose, PlayerModel<?> model, LivingEntity entity, boolean left) {
		if (entity.isCrouching() && !model.riding && !entity.isSwimming()) {
			pose.translate(0, 0, 0.25F);
		}
		ModelPart leg = left ? model.leftLeg : model.rightLeg;
		pose.translate(left ? 0.125F : -0.125F, 0.75F, 0);
		pose.mulPose(Axis.ZP.rotation(leg.zRot));
		pose.mulPose(Axis.YP.rotation(leg.yRot));
		pose.mulPose(Axis.XP.rotation(leg.xRot));
		pose.translate(0, 0.75F, 0);
	}
}
