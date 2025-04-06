package me.brokencloud.postal.menu;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.model.ItemStackModel;
import me.brokencloud.postal.model.Package;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Menu(
        title = "Send a package"
)
public final class SendMenu implements PlayerMenuProvider {
    private static final int[] GRIDS = IntStream.range(0, 18).toArray();
    private final Player recipient;

    public SendMenu(Player recipient) {
        this.recipient = recipient;
    }

    @Override
    public void onLoad(@Nonnull Player player, @Nonnull MenuContents menuContents) {
        menuContents.placeableItemsCloseAction(PlaceableItemsCloseAction.REMOVE);
        menuContents.registerPlaceableItemSlots(GRIDS);
        menuContents.events().onClose(() -> {
            List<ItemStackModel> items = new ArrayList<>();
            ItemStack itemStack;
            for (int i = 0; i < GRIDS.length; i++) {
                itemStack = menuContents.getPlaceableItems().get(i);
                if (itemStack == null || itemStack.getType().isAir()) continue;
                items.add(new ItemStackModel(itemStack));
            }
            if (items.isEmpty()) {
                return;
            }
            Package pack = new Package(player.getUniqueId(), this.recipient.getUniqueId(), items);
            Postal.getInstance().mongoDBManager.sendPackage(pack);
            System.out.println("Package sent to " + this.recipient.getDisplayName());
        });
    }
}
