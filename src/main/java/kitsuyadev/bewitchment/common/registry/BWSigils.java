/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.api.registry.Sigil;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.sigil.*;
import net.minecraft.resources.ResourceLocation;
import java.util.LinkedHashMap;
import java.util.Map;

public class BWSigils {
	private static final Map<Sigil, ResourceLocation> SIGILS = new LinkedHashMap<>();

	public static final Sigil MENDING = create("mending", new MendingSigil(true, 32));
	public static final Sigil CLEANSING = create("cleansing", new CleansingSigil(true, 32));
	public static final Sigil JUDGMENT = create("judgment", new JudgmentSigil(true, 32));
	public static final Sigil DECAY = create("decay", new DecaySigil(true, 32));
	public static final Sigil SHRIEKING = create("shrieking", new ShriekingSigil(true, 32));
	public static final Sigil SENTINEL = create("sentinel", new SentinelSigil(true, 32));

	public static final Sigil SLIPPERY = create("slippery", new Sigil(false, 32));
	public static final Sigil SHADOWS = create("shadows", new Sigil(false, 32));
	public static final Sigil EXTENDING = create("extending", new Sigil(false, 32));
	public static final Sigil SMELLY = create("smelly", new SmellySigil(false, 256));
	public static final Sigil RUIN = create("ruin", new Sigil(false, 32));
	public static final Sigil HEAVY = create("heavy", new Sigil(false, 32));

	private static <T extends Sigil> T create(String name, T sigil) {
		SIGILS.put(sigil, Bewitchment.id(name));
		return sigil;
	}

	static void register() {
		SIGILS.forEach((entry, id) -> BWRegistries.SIGIL_ENTRIES.register(id.getPath(), () -> entry));
	}
}
