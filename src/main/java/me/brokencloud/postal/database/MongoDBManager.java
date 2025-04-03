package me.brokencloud.postal.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.UpdateOptions;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperators;
import me.brokencloud.postal.model.ItemStackModel;
import me.brokencloud.postal.model.Package;
import org.bson.UuidRepresentation;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;

public class MongoDBManager {
    private MongoClient mongoClient;
    private Datastore datastore;

    public void connect(String connectionString, String database) {
        try {
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .build();
            mongoClient = MongoClients.create(mongoClientSettings);
            datastore = Morphia.createDatastore(mongoClient, database);

            //noinspection removal
            datastore.getMapper().map(ItemStackModel.class, Package.class);
            System.out.println("Database setup successful!");
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
        }
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Database closed successful!");
        } else {
            System.err.println("Database not initialized!");
        }
    }

    public void sendPackage(Package pack) {
        datastore.save(pack);
    }

    public List<Package> listPackages(Player player) {
        return datastore.find(Package.class)
                .filter(Filters.and(
                        Filters.eq("recipientId", player.getUniqueId()),
                        Filters.eq("unwrappedAt", null)
                ))
                .iterator().toList();
    }

    public List<ItemStack> unwrapPackage(Package pack) {
        datastore.find(Package.class)
                .filter(Filters.eq("id", pack.getId()))
                .update(new UpdateOptions(), UpdateOperators.set("unwrappedAt", new Date()));
        return pack.getContents().stream()
                .map(ItemStackModel::deserialize)
                .toList();
    }
}
