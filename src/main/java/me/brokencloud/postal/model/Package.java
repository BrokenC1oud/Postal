package me.brokencloud.postal.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Package {
    @Id
    private ObjectId id;
    private UUID senderId;
    private UUID recipientId;
    private List<ItemStackModel> contents;
    private Date createdAt;
    private Date unwrappedAt;

    public Package(UUID senderId, UUID recipientId, List<ItemStackModel> contents) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.contents = contents;
        this.createdAt = new Date();
    }

    public ObjectId getId() {
        return id;
    }

    public Date getUnwrappedAt() {
        return unwrappedAt;
    }
}
