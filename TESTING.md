# Build and testing

## Windows and Linux

Install a 64-bit JDK 17 and make it available as `JAVA_HOME`. Use the checked-in wrapper from the project directory.

| Operation | Windows PowerShell | Linux |
| --- | --- | --- |
| Compile and package | `.\gradlew.bat clean build` | `./gradlew clean build` |
| Development client | `.\gradlew.bat runClient` | `./gradlew runClient` |
| Dedicated server | `.\gradlew.bat runServer` | `./gradlew runServer` |
| Dedicated GameTests | `.\gradlew.bat runGameTestServer` | `./gradlew runGameTestServer` |

Development runs write to `run/client`, `run/server`, and `run/gametest`. A normal dedicated server requires an accepted Minecraft EULA.

## Release candidate verification

A clean Windows build of `1.20-10-rc.1` from a source-only copy succeeded with Java 17. All eleven GameTests passed on Forge 47.2.0. The reobfuscated JAR also started a standalone Forge 47.4.20 dedicated server with the four required dependencies, generated and saved a disposable world, and shut down cleanly.

## Automated runtime validation

- The Forge 47.2.0 GameTest server passed eleven tests. Coverage includes a working drought chalk ring with real reagent and altar costs, tipped-arrow and lingering-cloud brew duration and identity, player save and death-clone rules, the Power fortune's combat effects, grass and fortress loot, altar drains, familiar persistence, Terraform boat cargo and UUID, and Trinkets recovery. The Trinkets fixture checks exact count and NBT, a full inventory, replay prevention and retention of a missing mod's item data.
- `gradlew validationJar` creates a separate, reobfuscated test companion. Install it only in a disposable test instance alongside the normal JAR. The production JAR excludes the companion and GameTests.
- `-Dbewitchment.validation.world=BewitchmentQA` creates or reopens that disposable world, then records natural blocks before placing test fixtures. The companion logs the resulting recipe counts, player state, guidebook and GameTest results as `BW_VALIDATION`.
- `-Dbewitchment.validation.players=true` enables test-player observations and the dedicated console commands `bwqa <player> seed` and `bwqa <player>`. They set or inspect a vampire, curse, magic and Nazar fixture. Use a loopback-bound server and synthetic `BewitchmentQA` players.

## Standalone multiplayer validation

Two Forge 47.4.20 release clients joined the same disposable dedicated server. With keepInventory enabled, a vampire player retained the transformation, Outrage curse and exactly one equipped Nazar after death and respawn. The same state was present after server restart and reconnect.

## Delayed respawn crash fix

The player clone GameTest reproduced the missing transformation capability exception before the fix. It now checks that the removed player's pose updates tolerate invalidated capabilities and that all three contracts survive cloning exactly once. The full eleven-test suite passed after the fix.

The reobfuscated JAR also ran on standalone Forge 47.4.20 with the four release dependencies. A survival player with three contracts died from demon-attributed damage and remained connected for thirteen seconds with immediate respawn disabled. The server accepted a console command and shut down normally.

## Player skin lookup

In the reported singleplayer comparison, NeoAuth 1.0.3 was present in both launches. The launch with Bewitchment logged an Authlib `AuthenticationUnavailableException` and an HTML “Service unavailable” response during Minecraft's local player skin lookup. The launch with Bewitchment disabled recorded no such exception, and the tester reported that the skin loaded.

Four isolated Forge 47.4.20 singleplayer runs used fresh copies of the same world, the same synthetic profile, NeoAuth 1.0.3 and the four release dependencies. A loopback session service supplied controlled responses. Each client made one profile request and entered the world.

| Session response | Bewitchment enabled | Bewitchment disabled |
| --- | --- | --- |
| Valid profile JSON with a texture property | Loaded the supplied skin from the test texture cache | Loaded the supplied skin from the test texture cache |
| HTTP 503 with an HTML body | Authlib logged the same exception and the test skin did not load | Authlib logged the same exception and the test skin did not load |

This verifies the normal client skin path with a controlled response and a cached texture fixture. The reason the live session response differed between the original launches remains open. The 503 status was used for the fixture. The original log only records the HTML response body.

## Runtime test checklist

1. Launch the Forge client with the required mods and create a disposable world. Check the creative tab, block/item models and textures, sign and chest rendering, sound, particles, armour, all entity renders and the Book of Shadows.
2. Start an independent dedicated server with a fresh world. Check for Mixin injection errors, missing registries or assets and accidental client classloading. Join with two clients.
3. Exercise altar power and glyph rituals with valid and invalid reagents, brewing and tipped arrows, athame stripping and blood collection, incense, sigils, and poppets. Compare effects, timing, costs, drops and recipes with the source.
4. Apply curses, fortunes, contracts, pledges and transformations. Verify ability grants and revocation, hitboxes, polymorph identity, HUD and scaling from both clients. Log out and back in, die and respawn, change dimension and restart the server.
5. Equip each accessory and verify slot restrictions, rendering, modifiers, effect removal, death behaviour and persistence. Bind and release familiars, ride brooms and boats, test demon trades and summon each boss.
6. Generate fresh chunks in eligible and excluded biomes, check deposits and trees, natural spawns, seed drops and Nether fortress loot. Exercise optional AppleSkin, EMI and Origins integrations with and without those mods.
