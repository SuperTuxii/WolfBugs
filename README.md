### Fixes the Minecraft KillCredit System:

The killer is now the one, who did the last hit. This also applies to none entity damage death causes. If there are no combat tracker entries, because the entity is not in combat, there is no killer.


### Adds a new Command `/combattracker`:

- `/combattracker remove latest <targets> <entityToRemove>` (Removes the latest entry by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker remove all <targets> <entityToRemove>` (Removes all entries by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker clear <targets> `(Clears the combat trackers of the given targets. Returns the amount of combat trackers that got cleared)

### Adds a new Command `/modlist`:

- `/modlist show <target> <fullList>` (Shows the ModList for the given target. If fullList is true, the full ModList will be outputted, otherwise all whitelisted mods will be filtered out)
- `/modlist allow <modId>` (Adds the given modId to the Mod-WhiteList)
- `/modlist deny <modId>` (Adds the given modId to the Mod-BlackList)
- `/modlist neutralize <modId>` (Removes the given modId from the Mod-WhiteList and the Mod-BlackList)

**This Command uses the ModList that was sent by the client:**
>WARNING: This list is not authoritative.
A mod missing from the list does not mean the mod isn't there,
and similarly a mod present in the list does not mean it is there.
People using hacked clients WILL hack the mod lists to make them look correct.
Do not use this as an anti-cheat feature!

### Adds a new Command `/morph` & `/unmorph`:

- `/morph <morphPlayer> <morphIntoTarget>` (Changes the morphPlayer's skin to the morphIntoTarget's one while not changing it in the PlayerTabOverlay and SocialInteractionPlayerList)
- `/unmorph <morphedPlayer>` (Changes the morphedPlayer's skin back to the correct one (same as `/morph` with morphPlayer and morphIntoTarget being the same))

**Beware that morphing the skin is handled on the client and could therefore be exploited by someone who is familiar with modding!**

### Adds a new gamerule `allowChatting`:

- when true, all players can chat and use chatting commands.
- when false, only players with permission level 2+ can chat and use chatting commands. (default)

Chatting commands are `/me`, `/msg`, `/teammsg` and associated commands.

### Fixes Bug with Corpse Mod in 1.19.2:

[Detailed Issue](https://github.com/henkelmax/corpse/issues/186)

### Makes the PlayerTabOverlay ignore the gamemode
