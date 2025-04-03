package me.brokencloud.postal;

import me.brokencloud.postal.command.PostalCommand;
import me.brokencloud.postal.database.MongoDBManager;
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

        odalitaMenus = OdalitaMenus.createInstance(this);

        mongoDBManager.connect("mongodb+srv://jgbsxx20130315:xtnSAyhZOFqoVPO0@cluster0.lrxrdho.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0", "postal");
    }

    @Override
    public void onDisable() {
        mongoDBManager.close();
    }
}
