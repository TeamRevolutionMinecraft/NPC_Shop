package net.revolution.revonpcshop.shop;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import net.revolution.revonpcshop.RevoNPCShop;
import net.revolution.revonpcshop.actions.NPCActions;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ShopNPC {

    private final Location location;
    private final String skinTexture;
    private final String skinSignature;
    private final UUID id;
    private final String owner;
    private final String shopName;
    public ShopNPC(Location location, String skinTexture, String skinSignature, UUID id, String owner, String shopName) {
        this.location = location;
        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;
        this.id = id;
        this.owner = owner;
        this.shopName = shopName;
        save();
    }

    public void save() {
        File file = getPath().toFile();
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
        yamlConfig.set("LOCATION", location.serialize());
        yamlConfig.set("TEXTURE", skinTexture);
        yamlConfig.set("SIGNATURE", skinSignature);
        yamlConfig.set("OWNER", owner);
        yamlConfig.set("SHOPNAME", shopName);  // TODO Where is the name stored??
        try {
            yamlConfig.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * On call of this function all saved NPCs in the npcs folders are spawned into the world
     */
    public static void loadNPCS() {
        File dir = new File(RevoNPCShop.getInstance().getDataFolder().getPath() + "/npcs");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(child);
                Location loc = Location.deserialize(yamlConfiguration.getConfigurationSection("LOCATION").getValues(false));
                String texture = yamlConfiguration.getString("TEXTURE");
                String signature = yamlConfiguration.getString("SIGNATURE");
                String owner = yamlConfiguration.getString("OWNER");
                String name = yamlConfiguration.getString("SHOPNAME");

                NPC.Global npc = NPCLib.getInstance().generateGlobalNPC((JavaPlugin) RevoNPCShop.getInstance(),
                        owner + ":" + child.getName().split("\\.")[0], loc);
                npc.setSkin(texture, signature);
                npc.setText(name);
                npc.addCustomClickAction(NPC.Interact.ClickType.RIGHT_CLICK, NPCActions.OPENSHOP);

            }
        }
    }

    private Path getPath() {
        return Paths.get(RevoNPCShop.getInstance().getDataFolder().getPath(), "/", "npcs", "/", id.toString()+".yml");
    }
}
