package me.brokencloud.postal.command;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.model.ItemStackModel;
import me.brokencloud.postal.model.Package;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostalCommand implements CommandExecutor {
    // TODO: Currently test purpose only, full interface awaiting
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            List<ItemStackModel> items = Arrays.stream(player.getInventory().getContents())
                    .map(ItemStackModel::new)
                    .collect(Collectors.toList());
            Package pack = new Package(player.getUniqueId(), player.getUniqueId(), items);
            Postal.getInstance().mongoDBManager.sendPackage(pack);
        }
        return true;
    }
}
