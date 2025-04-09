package me.brokencloud.postal.model;

import com.mongodb.lang.Nullable;
import dev.morphia.annotations.Entity;

import java.util.UUID;

@Entity
public class Recipient {
    private RecipientType recipientType;
    private UUID recipientId;

    public Recipient() {}

    public Recipient(RecipientType recipientType, @Nullable UUID recipientId) {
        this.recipientType = recipientType;
        this.recipientId = recipientId;
    }

    public enum RecipientType {
        All,
        Player,
    }
}
