# Bewitchment Forge port progress

## Baseline

- Upstream Bewitchment `1.20-10` from commit `842cd9777b7bde2a0208099d5184baf88f24f63a`.
- Existing Minecraft version 1.20.1 and Java 17. Original build used Yarn `1.20.1+build.10`, Fabric Loader 0.16.7, Fabric API 0.92.2, Loom `1.7-SNAPSHOT` (resolved as 1.7.4), and Gradle 8.10.
- Minecraft remains 1.20.1. The ForgeGradle 6.0.54 development default is Forge 47.2.0 with official Mojang mappings, Java 17, and Gradle 8.8. The packaged runtime metadata accepts Forge versions `[47.2.0,48)`.
- Upstream single project has 407 Java files and one `src/main` source set. It ships authored resources with no dedicated datagen source set, tests, or standalone licence file. The build workflow used Java 21 to invoke Gradle, while compilation targeted Java 17.

## Source-derived feature inventory

| System | Baseline evidence and scope | Forge status |
| --- | --- | --- |
| Build and bootstrap | Forge entry points, capability registration, optional integrations and strict Mixin configuration. | Runtime Tested |
| Physical content | `BWObjects`, `BWMaterials`, `BWBoatTypes`, `BWBlockEntityTypes`. Juniper, cypress, elder and dragon's blood wood families including signs, chests and boats. Four herbs, ores, brambles, glyphs, coloured coffins, candelabras, witch armour, magical tools, poppets and loot. | Compiles |
| Creative inventory, sounds and particles | `BWObjects`, `BWSoundEvents`, `BWParticleTypes`. Creative tab, block and mob sounds, cauldron bubble and incense smoke. | Compiles |
| Magic and altar | `MagicComponent`, `WitchAltarBlockEntity`, `BewitchmentAPI`, `BWUtil`. Altar power generation and distribution, material bonuses, magic expenditure and world indexes. | Compiles |
| Rituals | `GlyphBlockEntity`, `BWRitualFunctions`, `RitualRecipe`, `common/ritualfunction` and ritual recipes. Includes weather and time, enchanting, smelting, growth, teleport, damage prevention, familiar binding and boss summoning. | Compiles |
| Curses, fortunes and contracts | `BWCurses` has 13 entries, `BWFortunes` has 12, `BWContracts` has 11. Includes the crystal ball, demonic contracts and pledges, timed persistence and gameplay hooks. | Compiles |
| Sigils, poppets and incense | `BWSigils` has 12 entries. Includes holders, shelf persistence, bound target behaviour, protections, revival, voodoo and sleep-triggered incense effects. | Compiles |
| Transformation and blood | `BWTransformations` registers human, vampire and werewolf. Includes forced forms, Pehkui dimensions, flight, thirst, drain, undead weaknesses, villager conversions and polymorph identity. | Compiles |
| Creatures and transport | `BWEntityTypes` defines owl, raven, snake, toad, ghost, vampire, werewolf, hellhound, demon, Leonard, Baphomet, Lilith and Herne, four brooms, silver arrow and horned spear. `BWBoatTypes` handles boats separately. Familiar AI, taming, combat, trades, riding and projectiles live under `common/entity` and associated Mixins. | Compiles |
| Equipment and accessories | Six original accessories include Nazar, Specter Bangle, Prickly Belt, Hellish Bauble, Druid Band and Zephyr Harness. Harbinger is a pair of boots. Also the athame, scepter, caduceus, horned spear and Witch armour sets. | Compiles |
| Recipes and brewing | `BWRecipeTypes`, `common/recipe` and brew Mixins. Seven typed recipes cover athame drops and stripping, rituals, oil, cauldron brewing, incense and curses. Three special crafting serializers cover taglocks, scepters and prickly belts. | Compiles |
| Menus and client visuals | Demon and Baphomet trade screens, custom creature and block entity renderers, a contributor layer, armour and accessories, blood HUD, keybindings, particles, block layers and models. | Compiles |
| Commands, events and networking | `BWCommands`, six internal event APIs, three serverbound packets, eight clientbound packets, plus CCA synchronisation. | Compiles |
| Player and world persistence | `BWComponents` defines 19 component keys across players and other entities. `BWWorldState` and `BWUniversalWorldState` store world data. Block entities have their own NBT. | Compiles |
| World generation and loot | `BWWorldGenerators`, `common/world` and the worldgen/loot resources. Four tree families, ore deposits, crop seed and Nether fortress loot additions, and biome-specific creature spawns. | Compiles |
| Integrations | Book of Shadows with custom Patchouli pages, Pehkui scaling, EMI recipe displays, AppleSkin vampire HUD handling, Apoli polymorph item-use interaction and cross-mod tags. | Compiles |
| Assets and data | Textures, models, blockstates, sounds, particles, guidebook entries, recipes, loot tables, advancements and tags. Five language files covering English, French, Russian, Ukrainian and Chinese. | Compiles |
| Client and dedicated test-server startup | Forge recognised Bewitchment on the client. Dedicated GameTest world started and passed four tests. | Runtime Tested |
| Multiplayer and full feature parity | End-to-end client gameplay, multiplayer state changes, optional mods and whole-world migration. | In Progress |

## Build and validation milestones

- Restored upstream history and switched to `forge-1.20.1-port`.
- Remapped Java and Mixin selectors to official names, added Forge bootstrapping, and replaced Fabric runtime dependencies.
- Java compilation and an initial clean, reobfuscated build succeeded. The runtime JAR contains Forge metadata, assets, access transformers, and a Mixin refmap.
- Dedicated boot caught the patched Forge breathing hook and dependency version incompatibilities. The pinned Forge 47.2.0 development client loaded Bewitchment, and the dedicated GameTest server passed all four tests. See [TESTING.md](TESTING.md).

## Remaining validation

- Verify original visual assets and cooperative gameplay in the full modpack. The standalone packaged server has booted, accepted a client, and saved and reopened its disposable world.
- Play through all original mechanics, including transformation flight and scale, multiplayer state synchronisation, and existing saves. Exercise optional integrations individually.

## Runtime validation

- Fixed the production brewing Mixin on Forge 47.4.20 and added data migration for Bewitchment Terraform boats and legacy Trinkets inventories.
- GameTests cover ritual execution, brewing, player death and save rules, and synthetic legacy migrations.
- Two standalone Forge clients joined the same dedicated server. The vampire transformation, Outrage curse and one equipped Nazar survived death and a server restart.
- See [TESTING.md](TESTING.md) for verified results and remaining checks.
