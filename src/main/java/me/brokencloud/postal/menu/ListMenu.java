package me.brokencloud.postal.menu;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.model.Package;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.brokencloud.postal.Postal.getInstance;

@Menu(
        title = "List of package"
)
public final class ListMenu implements PlayerMenuProvider {
    @Override
    public void onLoad(@Nonnull Player player, @Nonnull MenuContents menuContents) {
        List<Package> packages = getInstance().mongoDBManager.listPackages(player);

        int length = Math.min(packages.size(), 18);
        for (int i = 0; i < length; i++) {
            int finalI = i;
            menuContents.setClickable(i, packageSkull(packages.get(i)), inventoryClickEvent -> {
                List<ItemStack> itemStacks =  Postal.getInstance().mongoDBManager.unwrapPackage(packages.get(finalI));
                for (ItemStack itemStack : itemStacks) {
                    player.getInventory().addItem(itemStack);
                }
                menuContents.closeInventory(player, PlaceableItemsCloseAction.REMOVE);
            });
        }
    }

    private static ItemStack packageSkull(Package pack) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(URI.create("https://textures.minecraft.net/texture/824f769c9450f22e484e089ca02e324fe37b18f4c18ef296021871ba4ad0c396").toURL());
        } catch (MalformedURLException exception) {
            System.out.println("Impossible");
        }
        profile.setTextures(textures);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        assert meta != null;
        meta.setOwnerProfile(profile);
        meta.setDisplayName("Package from " + Objects.requireNonNull(Bukkit.getPlayer(pack.getSenderId())).getName());
        meta.setLore(List.of("Contents: " + pack.getContents().size() + " item(s)", "Sent: " + sdf.format(pack.getCreatedAt())));
        head.setItemMeta(meta);
        return head;
    }
}
