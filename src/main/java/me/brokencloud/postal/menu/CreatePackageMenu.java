package me.brokencloud.postal.menu;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.model.ItemStackModel;
import me.brokencloud.postal.model.Package;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.IntStream;

@Menu(
        title = "Send a package"
)
public final class CreatePackageMenu implements PlayerMenuProvider {
    private static final int[] GRIDS = IntStream.range(0, 18).toArray();
    private String description;
    private final Player recipient;

    public CreatePackageMenu(String description) {
        this.description = description;
        this.recipient = null;
    }

    public CreatePackageMenu(Player recipient) {
        this.recipient = recipient;
    }

    @Override
    public void onLoad(@Nonnull Player player, @Nonnull MenuContents menuContents) {
        menuContents.placeableItemsCloseAction(PlaceableItemsCloseAction.REMOVE);
        menuContents.registerPlaceableItemSlots(GRIDS);
        menuContents.events().onClose(() -> {
            List<ItemStackModel> items = menuContents.getPlaceableItems().values().stream()
                    .map(ItemStackModel::new).toList();
            if (items.isEmpty()) return;

            Package pack;
            if (recipient != null) {
                pack = new Package(items, player, recipient);
            } else {
                pack = new Package(items, description);
            }
            Postal.getInstance().mongoDBManager.savePackage(pack);
            if (recipient == null) {
                player.sendMessage(ChatColor.GREEN + "Package created with id " + pack.getId());
            }
        });
    }
}
