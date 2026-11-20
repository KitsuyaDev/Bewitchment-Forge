package kitsuyadev.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import kitsuyadev.bewitchment.client.BewitchmentClient;
import kitsuyadev.bewitchment.client.model.equipment.trinket.PricklyBeltModel;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PricklyBeltRenderer implements ICurioRenderer {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/trinket/prickly_belt.png");
	private static Model model;

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slot,
			PoseStack matrices, RenderLayerParent<T, M> parent, MultiBufferSource vertexConsumers, int light,
			float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (!(parent.getModel() instanceof PlayerModel<?> playerModel)) {
			return;
		}
		LivingEntity entity = slot.entity();
		if (model == null) {
			model = new PricklyBeltModel(BewitchmentClient.getPart(BewitchmentClient.PRICKLY_BELT_MODEL_LAYER));
		}
		AccessoryPose.chest(matrices, playerModel, entity);
		model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}
}
