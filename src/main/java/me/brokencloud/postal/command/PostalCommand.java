package me.brokencloud.postal.command;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.menu.CreatePackageMenu;
import me.brokencloud.postal.menu.ListPackagesMenu;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PostalCommand implements CommandExecutor {
    // TODO: Currently test purpose only, full interface awaiting
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String[] args
    ) {
        if (sender instanceof Player player) {
            if (args.length != 0) {
                switch (args[0]) {
                    case "create":
                        if (args.length == 2) {
                            Postal.getInstance().getOdalitaMenus().openMenu(new CreatePackageMenu(Bukkit.getPlayer(args[1])), player);
                        } else {
                            Postal.getInstance().getOdalitaMenus().openMenu(new CreatePackageMenu(), player);
                        }
                        break;
                    case "list":
                        Postal.getInstance().getOdalitaMenus().openMenu(new ListPackagesMenu(), player);
                        break;
                    case "send":
                        if (args.length == 3) {
                            Postal.getInstance().mongoDBManager.sendPackage(new ObjectId(args[2]), Bukkit.getPlayer(args[1]));
                        } else {
                            player.sendMessage(ChatColor.RED + "Usage: /postal send <player> <package>");
                        }
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "Invalid Argument");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /postal [create|send|list]");
            }
        } else if (sender instanceof ConsoleCommandSender) {
            // TODO
        }
        return true;
    }
}
