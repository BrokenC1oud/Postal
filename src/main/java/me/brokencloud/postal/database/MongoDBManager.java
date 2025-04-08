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
import me.brokencloud.postal.model.Claim;
import me.brokencloud.postal.model.ItemStackModel;
import me.brokencloud.postal.model.Package;
import me.brokencloud.postal.model.Recipient;
import org.bson.UuidRepresentation;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
            datastore.getMapper().map(ItemStackModel.class, Claim.class, Recipient.class,  Package.class);
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

    public void savePackage(Package pack) {
        datastore.save(pack);
    }

    public void sendPackage(ObjectId packId, Player player) {
        datastore.find(Package.class)
                .filter(Filters.eq("id", packId))
                .update(new UpdateOptions(), UpdateOperators.addToSet(
                        "recipients", new Recipient(Recipient.RecipientType.Player, player.getUniqueId())));
    }

    public List<Package> listPackages(Player player) {
        return datastore.find(Package.class)
                .filter(
                        Filters.or(
                                Filters.and(
                                        Filters.eq("recipients.recipientId", player.getUniqueId()),
                                        Filters.eq("recipients.recipientType", Recipient.RecipientType.Player)
                                ),
                                Filters.eq("recipients.recipientType", Recipient.RecipientType.All)
                        ),
                        Filters.elemMatch("claims", Filters.eq("uuid", player.getUniqueId())).not()
                )
                .iterator().toList();
    }

    public List<ItemStack> claimPackage(Package pack, Player player) {
        if (datastore.find(Package.class)
                .filter(
                        Filters.eq("id", pack.getId()),
                        Filters.elemMatch("claims", Filters.eq("uuid", player.getUniqueId())))
                .count() == 0
        ) {
            datastore.find(Package.class)
                    .filter(Filters.eq("id", pack.getId()))
                    .update(new UpdateOptions(), UpdateOperators.addToSet("claims", new Claim(player.getUniqueId())));
            return pack.getContents().stream().map(ItemStackModel::deserialize).toList();
        } else {
            return List.of();
        }
    }
}
