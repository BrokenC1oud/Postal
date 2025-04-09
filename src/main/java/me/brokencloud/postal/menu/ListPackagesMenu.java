package me.brokencloud.postal.menu;


import me.brokencloud.postal.Postal;
import me.brokencloud.postal.model.Package;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Menu(
        title = "Packages"
)
public class ListPackagesMenu implements PlayerMenuProvider {
    public ListPackagesMenu() {
    }

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents menuContents) {
        fillPackages(player, menuContents);
    }

    private void fillPackages(@NotNull Player player, @NotNull MenuContents menuContents) {
        List<Package> packages = Postal.getInstance().mongoDBManager.listPackages(player);
        for (int i = 0; i < 18; i++) {
            int finalI = i;
            if (i < packages.size()) {
                menuContents.setClickable(i, packageSkull(packages.get(i)), inventoryClickEvent -> {
                    for (ItemStack itemStack : Postal.getInstance().mongoDBManager.claimPackage(packages.get(finalI), player))
                        player.getInventory().addItem(itemStack);
                    fillPackages(player, menuContents);
                });
            } else menuContents.clear(i);
        }
    }

    private static ItemStack packageSkull(Package pack) {
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
        meta.setDisplayName(pack.getDescription());
        head.setItemMeta(meta);
        return head;
    }
}
