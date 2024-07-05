package tuxi.wolfbugs.mixin;

import de.maxhenkel.corpse.gui.CorpseInventoryContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CorpseInventoryContainer.class)
public class CorpseInventoryContainerMixin {

    @Redirect(method = "transferItems", at = @At(value = "INVOKE", target = "Lde/maxhenkel/corpse/gui/CorpseInventoryContainer;fill(Ljava/util/List;Lnet/minecraft/world/Container;Lnet/minecraft/core/NonNullList;)V", ordinal = 1), remap = false)
    private void preventBindingCurseTransfer(CorpseInventoryContainer instance, List<ItemStack> additionalItems, Container inventory, NonNullList<ItemStack> playerInv) {
        for(int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && !EnchantmentHelper.hasBindingCurse(playerInv.get(i))) {
                ItemStack playerStack = playerInv.get(i);
                if (!playerStack.isEmpty()) {
                    additionalItems.add(playerStack);
                }

                inventory.setItem(i, ItemStack.EMPTY);
                playerInv.set(i, stack);
            }else if (!stack.isEmpty()) {
                additionalItems.add(stack);
            }
        }
    }
}
