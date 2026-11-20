package kitsuyadev.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HellishBaubleRenderer implements ICurioRenderer {
	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slot,
			PoseStack matrices, RenderLayerParent<T, M> parent, MultiBufferSource vertexConsumers, int light,
			float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (!(parent.getModel() instanceof PlayerModel<?> playerModel)) {
			return;
		}
		LivingEntity entity = slot.entity();
		AccessoryPose.chest(matrices, playerModel, entity);
		matrices.translate(0, -1 / 4.25f, 1 / 48f);
		matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
		matrices.mulPose(Axis.XP.rotationDegrees(180));
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.level(), 0);
	}
}
