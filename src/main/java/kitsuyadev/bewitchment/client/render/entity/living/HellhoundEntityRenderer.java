/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render.entity.living;

import kitsuyadev.bewitchment.client.BewitchmentClient;
import kitsuyadev.bewitchment.client.model.entity.living.HellhoundEntityModel;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.living.HellhoundEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HellhoundEntityRenderer extends MobRenderer<HellhoundEntity, HellhoundEntityModel<HellhoundEntity>> {
	private static ResourceLocation[] TEXTURES;

	public HellhoundEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new HellhoundEntityModel<>(context.bakeLayer(BewitchmentClient.HELLHOUND_MODEL_LAYER)), 0.3f);
	}

	@Override
	public ResourceLocation getTextureLocation(HellhoundEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/hellhound/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
