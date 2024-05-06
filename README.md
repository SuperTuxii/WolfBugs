### This is a server-only mod and is not needed on the client


**Fixes the Minecraft KillCredit System:**

The killer is now the one, who did the last hit. This also applies to none entity damage death causes. If there are no combat tracker entries, because the entity is not in combat, there is no killer.


**Adds a new Command `/combattracker`:**

- `/combattracker remove latest <targets> <entityToRemove>` (Removes the latest entry by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker remove all <targets> <entityToRemove>` (Removes all entries by the given entity in the combat trackers of the given targets. Returns the amount of entries that got removed)
- `/combattracker clear <targets> `(Clears the combat trackers of the given targets. Returns the amount of combat trackers that got cleared)


**Disables the chat:** (version 1.1)

When trying to send a message without permission level 2+, the server responds with "Cannot send chat message" and cancels the message.  
The Commands `/me`, `/msg`, `/teammsg` and associated commands require permission level 2+ from now on.
