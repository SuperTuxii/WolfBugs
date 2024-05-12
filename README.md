### This is a server-only mod and is not needed on the client


**Fixes the Minecraft KillCredit System:**

The killer is now the one, who did the last hit. This also applies to none entity damage death causes. If there are no combat tracker entries, because the entity is not in combat, there is no killer.


**Adds a new Command `/combattracker`:**

- `/combattracker remove latest <targets> <entityToRemove>` (Removes the latest entry by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker remove all <targets> <entityToRemove>` (Removes all entries by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker clear <targets> `(Clears the combat trackers of the given targets. Returns the amount of combat trackers that got cleared)


**Adds a new gamerule `allowChatting`:**

- when true, all player can chat and use chatting commands.
- when false, only players with permission level 2+ can chat and use chatting commands. (default)

Chatting commands are `/me`, `/msg`, `/teammsg` and associated commands.
