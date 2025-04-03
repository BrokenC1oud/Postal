package me.brokencloud.postal.model;

import dev.morphia.annotations.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Entity
public class ItemStackModel {
    private String data;

    public ItemStackModel() {}

    public ItemStackModel(ItemStack itemStack) {
        this.data = this.serializeItemStackToBase64(itemStack);
    }

    public ItemStack deserialize() {
        try {
            return this.deserializeItemStackFromBase64(this.data);
        } catch (IOException | ClassNotFoundException exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
        }
        return null;
    }

    public String serializeItemStackToBase64(ItemStack itemStack) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = null;

        try {
            bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            bukkitObjectOutputStream.writeObject(itemStack);
            bukkitObjectOutputStream.flush();

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            return null;
        } finally {
            if (bukkitObjectOutputStream != null) {
                try {
                    bukkitObjectOutputStream.close();
                } catch (IOException exception) {
                    //noinspection CallToPrintStackTrace
                    exception.printStackTrace();
                }
            }
        }
    }

    public ItemStack deserializeItemStackFromBase64(String base64) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(base64);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
        ItemStack itemStack = (ItemStack) bukkitObjectInputStream.readObject();
        byteArrayInputStream.close();
        return itemStack;
    }
}
