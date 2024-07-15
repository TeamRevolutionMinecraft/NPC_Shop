package net.revolution.revonpcshop.shop;

import dev.sergiferry.playernpc.api.NPC;
import net.revolution.revonpcshop.RevoNPCShop;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import teamrevolution.serverCore.gui.GuiUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Shop implements InventoryHolder, Runnable {
    private static final int ID_INDEX = 1;
    private static final int OWNER_INDEX = 0;
    private static final int UNLOAD_TIME = 12000;
    private static Path STORAGE_PATH = null;


    private boolean ownerAccess;
    private final UUID shopID;
    private final String shopOwner;
    private boolean adminShop;
    private ShopView shopView;
    private boolean inEditMode;
    private BukkitTask chacheTask;


    public Shop(UUID shopID, String shopOwner, boolean ownerAccess) {
        this.shopID = shopID;
        this.shopOwner = shopOwner;
        this.ownerAccess = ownerAccess;
        this.shopView = new ShopView();
        this.adminShop = false;
        this.inEditMode = false;
        this.chacheTask = null;

        if (shopExists()) {
            loadItems();

        } else {
            ItemStack offerItem =  new ItemStack(Material.MAGENTA_BANNER);
            offerItem.setAmount(13);
            ItemStack priceItem = new ItemStack(Material.SPRUCE_WOOD);
            priceItem.setAmount(64);
            ItemStack vault = priceItem.clone();
            vault.setAmount(1);
            this.shopView = new ShopView();
            ItemStack storage =  new ItemStack(Material.MAGENTA_BANNER);
            storage.setAmount(500);

            ShopOffer offer = new ShopOffer(offerItem, priceItem, ShopViewOrientation.UP2DOWN, 2, storage, vault);
            this.shopView.addOffer(offer);
            saveShop();
        }

    }
    public static void init(@NotNull Plugin plugin) {
        STORAGE_PATH = Paths.get(plugin.getDataFolder().getPath(), "/", "shops");
        try {
            if(!STORAGE_PATH.toFile().mkdirs()) {
                plugin.getLogger().warning("Shop STORAGE_PATH not created (because it probably exists already)");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error while creating character folder: " + e);
        }
    }
    public static Shop openShop(Player player, NPC npc) {


        List<String> shopConfig = Arrays.asList(npc.getSimpleID().split(":"));
        String shopOwner = shopConfig.get(OWNER_INDEX);
        Shop shop = RevoNPCShop.getShopChache().fromChache(shopConfig.get(ID_INDEX));

        if (!shopOwner.equalsIgnoreCase(player.getName())) {
            if (shop == null) {
                shop = new Shop(UUID.fromString(shopConfig.get(ID_INDEX)), shopOwner, false);
                RevoNPCShop.getShopChache().toChache(shop.getShopID().toString(), shop);
            } else {
                shop.setOwnerAccess(false);
            }
        } else {
            if (shop == null) {
                shop = new Shop(UUID.fromString(shopConfig.get(ID_INDEX)), shopOwner, true);
                RevoNPCShop.getShopChache().toChache(shop.getShopID().toString(), shop);
            } else {
                shop.setOwnerAccess(true);
            }
        }
        return shop;
    }


    public void saveShop() {
        File file = getPath().toFile();
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);

        yamlConfig.set("META.OWNER", shopOwner);
        yamlConfig.set("META.ADMINSHOP", adminShop);
        for (ShopOffer offer : shopView.getShopItems()) {
            offer.storeToYaml(yamlConfig);
        }
        try {
            yamlConfig.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean shopExists() {
        return getPath().toFile().exists();
    }
    private Path getPath() {
        return Paths.get(STORAGE_PATH + "/" + shopID + ".yml");
    }

    private void loadItems() {
        File file = getPath().toFile();
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
        shopView.loadShopView(yamlConfig);

    }

    public Inventory buildShopGUI() {
        Inventory inv = GuiUtils.createChestGui(5,
                "Shop",
                ownerAccess ? "Dein Shop " : "Shop von" + shopOwner,
                Material.GLASS_PANE,
                false, this);

        if (ownerAccess) {
            inv.setItem((5*9) -1, new ItemStack(Material.RED_STAINED_GLASS));
        }
        this.shopView.addShopItems(inv);

        return inv;
    }

    public Inventory buildConfigGui() {
        Inventory inv = GuiUtils.createChestGui(5,
                "Shop",
                ownerAccess ? "Dein Shop " : "Shop von" + shopOwner,
                Material.GRANITE_WALL,
                false, this);

        if (ownerAccess) {
            inv.setItem((5*9) -1, new ItemStack(Material.RED_STAINED_GLASS));
        }
        return inv;
    }

    public void startTimer() {
        RevoNPCShop.getInstance().getLogger().info("Timer started for shop " + shopID);

        if (chacheTask != null) {
            chacheTask.cancel();
        }
        chacheTask = RevoNPCShop.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(RevoNPCShop.getInstance(), this, UNLOAD_TIME);

    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }


    @Override
    public void run() {
        RevoNPCShop.getShopChache().unloadShop(shopID.toString());
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopID=" + shopID +
                ", shopOwner='" + shopOwner + '\'' +
                '}';
    }

    public boolean isOwnerAccess() {
        return ownerAccess;
    }

    public UUID getShopID() {
        return shopID;
    }

    public String getShopOwner() {
        return shopOwner;
    }

    public ShopView getShopView() {
        return shopView;
    }

    public void setAdminShop(boolean adminShop) {
        this.adminShop = adminShop;
    }

    public boolean isAdminShop() {
        return adminShop;
    }


    public void setOwnerAccess(boolean ownerAccess) {
        this.ownerAccess = ownerAccess;
    }

    public boolean isInEditMode() {
        return inEditMode;
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
    }
}
