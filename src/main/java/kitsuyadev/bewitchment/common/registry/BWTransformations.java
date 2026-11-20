/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.api.registry.Transformation;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.transformation.VampireTransformation;
import kitsuyadev.bewitchment.common.transformation.WerewolfTransformation;
import net.minecraft.resources.ResourceLocation;
import java.util.LinkedHashMap;
import java.util.Map;

public class BWTransformations {
	private static final Map<Transformation, ResourceLocation> TRANSFORMATIONS = new LinkedHashMap<>();

	public static final Transformation HUMAN = create("human", new Transformation());
	public static final Transformation VAMPIRE = create("vampire", new VampireTransformation());
	public static final Transformation WEREWOLF = create("werewolf", new WerewolfTransformation());

	private static <T extends Transformation> T create(String name, T transformation) {
		TRANSFORMATIONS.put(transformation, Bewitchment.id(name));
		return transformation;
	}

	static void register() {
		TRANSFORMATIONS.forEach((entry, id) -> BWRegistries.TRANSFORMATION_ENTRIES.register(id.getPath(), () -> entry));
	}
}
