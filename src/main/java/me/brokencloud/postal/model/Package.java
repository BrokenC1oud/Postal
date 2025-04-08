package me.brokencloud.postal.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Package {
    @Id
    private ObjectId id;
    private UUID sender;
    private List<Recipient> recipients;
    private Date createdAt;
    private List<ItemStackModel> contents;
    private List<Claim> claims;

    /**
     * c2c package constructor
     * @param contents package contents
     * @param sender package sender
     * @param recipient recipient (individual)
     */
    public Package(List<ItemStackModel> contents, Player sender, Player recipient) {
        this.contents = contents;
        this.sender = sender.getUniqueId();
        this.recipients = List.of(new Recipient(Recipient.RecipientType.Player, recipient.getUniqueId()));
        this.createdAt = new Date();
    }

    /**
     * general package constructor
     * @param contents package contents
     */
    public Package(List<ItemStackModel> contents) {
        this.contents = contents;
        this.createdAt = new Date();
    }

    public void addRecipient(Player player) {
        this.recipients.add(new Recipient(Recipient.RecipientType.Player, player.getUniqueId()));
    }

    public Package() {}

    public ObjectId getId() {
        return this.id;
    }

    public List<ItemStackModel> getContents() {
        return this.contents;
    }
}
