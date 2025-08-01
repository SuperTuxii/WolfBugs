### Fixes the Minecraft KillCredit System:

The killer is now the one who did the last hit.
This also applies to none entity damage death causes.
If there are no combat tracker entries because the entity is not in combat, there is no killer.


### Adds a new Command `/combattracker`:

- `/combattracker remove latest <targets> <entityToRemove>` (Removes the latest entry by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker remove all <targets> <entityToRemove>` (Removes all entries by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker clear <targets> `(Clears the combat trackers of the given targets. Returns the amount of combat trackers that got cleared)

### Adds a new Command `/modlist` (requires `MOD` tag):

- `/modlist show <target> [<fullList>]` (Shows the ModList for the given target. If fullList is true, the full ModList will be outputted, otherwise all whitelisted mods will be filtered out (default is `false`))
- `/modlist list [<fullList>]` (Shows the ModList for every online player. The fullList argument has the same function as in `/modlist show <target> [<fullList>]` (default is also `false`))
- `/modlist allow <modId>` (Adds the given modId to the Mod-WhiteList)
- `/modlist deny <modId>` (Adds the given modId to the Mod-BlackList. If a player joins with a blacklisted Mod, any players with the `MOD` tag will be alerted. If non are online, the player and modlist will be saved and displayed, when the next moderator joins)
- `/modlist neutralize <modId>` (Removes the given modId from the Mod-WhiteList and the Mod-BlackList)

**This Command uses the ModList that was sent by the client:**
>WARNING: This list is not authoritative.
A mod missing from the list does not mean the mod isn't there,
and similarly a mod present in the list does not mean it is there.
People using hacked clients WILL hack the mod lists to make them look correct.
Do not use this as an anti-cheat feature!

### Adds a new Command `/morph` & `/unmorph`:

- `/morph <morphPlayer> <morphIntoTarget> [<key>]` (Changes the morphPlayer's skin to the morphIntoTarget's one while not changing it in the PlayerTabOverlay and SocialInteractionPlayerList)
- `/unmorph <morphedPlayer> [<key>]` (Changes the morphedPlayer's skin back to the correct one (same as /morph with morphPlayer, morphIntoTarget and key being the same). Instead of the key `any` can be set, which removes all morphs)

Optionally, a key (number) can be set on both commands,
which allows multiple morphs that are applied again after unmorphing one (Setting no key sets the key -1).
The morph that is applied is always the last one that has been added.
For example (active morphs in order of application → shown one):
- 1, 3, 2 → 2 
- 1, 3 → 3 (`/unmorph <player> 2`)
- 3 → 3 (`/unmorph <player> 1`)

**Beware that morphing the skin is handled on the client and could therefore be exploited by someone who is familiar with modding!**

### Adds a new gamerule `allowChatting`:

- when true, all players can chat and use chatting commands.
- when false, only players with permission level 2+ can chat and use chatting commands. (default)

Chatting commands are `/me`, `/msg`, `/teammsg` and associated commands.

### Fixes Bug with Corpse Mod in 1.19.2:

[Detailed Issue](https://github.com/henkelmax/corpse/issues/186)

### Makes the PlayerTabOverlay ignore the gamemode
