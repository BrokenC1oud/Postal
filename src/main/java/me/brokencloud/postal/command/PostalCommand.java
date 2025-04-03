package me.brokencloud.postal.command;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.model.Package;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PostalCommand implements CommandExecutor {
    // TODO: Currently test purpose only, full interface awaiting
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            List<Package> packages = Postal.getInstance().mongoDBManager.listPackages(player);
            Package pack = packages.getFirst();
            for (ItemStack itemStack : Postal.getInstance().mongoDBManager.unwrapPackage(pack)) {
                player.getInventory().addItem(itemStack);
            }
        }
        return true;
    }
}
