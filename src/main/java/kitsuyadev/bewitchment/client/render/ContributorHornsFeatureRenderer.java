/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render;

import kitsuyadev.bewitchment.client.BewitchmentClient;
import kitsuyadev.bewitchment.client.model.ContributorHornsModel;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public final class ContributorHornsFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/armor/contributor_horns.png");
	private static final Set<UUID> CONTRIBUTORS = loadContributors();
	private final ContributorHornsModel model;

	public ContributorHornsFeatureRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> context, EntityModelSet loader) {
		super(context);
		model = new ContributorHornsModel(loader.bakeLayer(BewitchmentClient.CONTRIBUTOR_HORNS_MODEL_LAYER));
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (!player.isInvisible() && player.isModelPartShown(PlayerModelPart.HAT) && CONTRIBUTORS.contains(player.getUUID())) {
			matrices.pushPose();
			getParentModel().head.translateAndRotate(matrices);
			model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entitySolid(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			matrices.popPose();
		}
	}

	private static Set<UUID> loadContributors() {
		try (var stream = ContributorHornsFeatureRenderer.class.getResourceAsStream("/META-INF/contributors.properties")) {
			if (stream == null) {
				throw new IOException("Missing bundled contributor list");
			}
			Properties properties = new Properties();
			properties.load(stream);
			Set<UUID> contributors = new HashSet<>();
			for (String key : properties.stringPropertyNames()) {
				contributors.add(UUID.fromString(properties.getProperty(key)));
			}
			return Set.copyOf(contributors);
		} catch (IOException | IllegalArgumentException error) {
			LogUtils.getLogger().warn("Failed to load the bundled Bewitchment contributor list", error);
			return Set.of();
		}
	}
}
