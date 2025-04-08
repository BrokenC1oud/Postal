package me.brokencloud.postal.model;

import dev.morphia.annotations.Entity;

import java.util.Date;
import java.util.UUID;

@Entity
public class Claim {
    private UUID uuid;
    private Date claimedAt;

    public Claim() {}

    public Claim(UUID uuid) {
        this.uuid = uuid;
        this.claimedAt = new Date();
    }
}
