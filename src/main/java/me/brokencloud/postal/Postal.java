package me.brokencloud.postal;

import me.brokencloud.postal.command.PostalCommand;
import me.brokencloud.postal.database.MongoDBManager;
import me.brokencloud.postal.events.PostalListener;
import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Postal extends JavaPlugin {
    private static Postal instance;

    public MongoDBManager mongoDBManager = new MongoDBManager();

    private OdalitaMenus odalitaMenus;

    public static Postal getInstance() {
        return instance;
    }

    public OdalitaMenus getOdalitaMenus() {
        return this.odalitaMenus;
    }

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(getCommand("postal")).setExecutor(new PostalCommand());
        getServer().getPluginManager().registerEvents(new PostalListener(), this);

        odalitaMenus = OdalitaMenus.createInstance(this);

        mongoDBManager.connect("mongodb://root:123456@localhost:27017", "postal");
    }

    @Override
    public void onDisable() {
        mongoDBManager.close();
    }
}
