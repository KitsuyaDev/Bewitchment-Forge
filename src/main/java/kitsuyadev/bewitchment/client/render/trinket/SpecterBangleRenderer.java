package kitsuyadev.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import kitsuyadev.bewitchment.client.BewitchmentClient;
import kitsuyadev.bewitchment.client.model.equipment.trinket.SpecterBangleModel;
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

public class SpecterBangleRenderer implements ICurioRenderer {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/trinket/specter_bangle.png");
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
			model = new SpecterBangleModel(BewitchmentClient.getPart(BewitchmentClient.SPECTER_BANGLE_MODEL_LAYER));
		}
		AccessoryPose.leg(matrices, playerModel, entity, false);
		model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}
}
