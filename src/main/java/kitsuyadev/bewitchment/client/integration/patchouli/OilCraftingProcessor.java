/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.integration.patchouli;

import kitsuyadev.bewitchment.common.recipe.OilRecipe;
import kitsuyadev.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class OilCraftingProcessor implements IComponentProcessor {
	protected OilRecipe recipe;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		recipe = (OilRecipe) level.getRecipeManager().byKey(new ResourceLocation(variables.get("recipe").asString())).filter(recipe -> recipe.getType().equals(BWRecipeTypes.OIL_RECIPE_TYPE.get())).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(Level level, String key) {
		if (key.equals("output")) {
			return IVariable.from(recipe.getResultItem(level.registryAccess()));
		} else if (key.equals("header")) {
			return IVariable.from(recipe.getResultItem(level.registryAccess()).getHoverName());
		}
		for (int i = 0; i < recipe.input.size(); i++) {
			if (key.equals("ingredient" + i)) {
				ItemStack[] stack = recipe.input.get(i).getItems();
				return stack.length > 0 ? IVariable.from(stack[0]) : null;
			}

		}
		return null;
	}
}
