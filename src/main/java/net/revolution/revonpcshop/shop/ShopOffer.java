package net.revolution.revonpcshop.shop;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopOffer {

    private static final String YAML_PATH_PRICE = ".PRICE";
    private static final String YAML_PATH_ORIENTATION = ".ORIENTATION";
    private static final String YAML_PATH_OFFERITEM = ".OFFER";
    private static final String YAML_PATH_STORAGE = ".STORAGE";
    private static final String YAML_PATH_VAULT = ".VAULT";

    private ItemStack offerItem;
    private ItemStack price;
    private ItemStack storage;
    private ItemStack vault;
    private ShopViewOrientation orientation;
    private int invIndexOffer, invIndexPrice;

    public ShopOffer(ItemStack offerItem, ItemStack price, ShopViewOrientation orientation, int invIndexOffer, ItemStack storage, ItemStack vault) {
        this.offerItem = offerItem;
        this.price = price;
        this.orientation = orientation;
        this.invIndexOffer = invIndexOffer;
        this.storage = storage;
        this.vault = vault;
        this.invIndexPrice = switch (orientation) {
            case LEFT2RIGHT -> invIndexOffer  + 1;
            case RIGHT2LEFT -> invIndexOffer - 1;
            case UP2DOWN -> invIndexOffer + 9;
            case DOWN2UP -> invIndexOffer - 9;
        };

    }
    public boolean canBuy(Inventory playerInventory) {
        for (ItemStack item : playerInventory.getContents()) {
            if (item != null &&
                    item.getAmount() == price.getAmount() &&
                    item.getType() == price.getType()
                )
                return true;
        }
        return false;
    }

    public boolean canBuyAndRemove(Inventory playerInventory) {
        for (ItemStack item : playerInventory.getContents()) {
            if (item != null &&
                    item.getAmount() == price.getAmount() &&
                    item.getType() == price.getType()
            ) {
                item.setAmount(item.getAmount() - price.getAmount());
                return true;
            }
        }
        return false;

    }

    public static ShopOffer buildOffer(YamlConfiguration yamlConfiguration, int key) {

        ItemStack offer = ItemStack.deserialize(yamlConfiguration.getConfigurationSection(key+YAML_PATH_OFFERITEM).getValues(false));
        ItemStack price = ItemStack.deserialize(yamlConfiguration.getConfigurationSection(key+YAML_PATH_PRICE).getValues(false));
        ItemStack storage = ItemStack.deserialize(yamlConfiguration.getConfigurationSection(key+YAML_PATH_STORAGE).getValues(false));
        ItemStack vault = ItemStack.deserialize(yamlConfiguration.getConfigurationSection(key+YAML_PATH_VAULT).getValues(false));
        ShopViewOrientation ori = ShopViewOrientation.valueOf(yamlConfiguration.getString(key+YAML_PATH_ORIENTATION));
        return new ShopOffer(offer, price, ori, key, storage, vault);
    }
    public void storeToYaml(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set(invIndexOffer + YAML_PATH_PRICE, price.serialize());
        yamlConfiguration.set(invIndexOffer + YAML_PATH_ORIENTATION, orientation.toString());
        yamlConfiguration.set(invIndexOffer + YAML_PATH_OFFERITEM, offerItem.serialize());
        yamlConfiguration.set(invIndexOffer + YAML_PATH_STORAGE, storage.serialize());
        yamlConfiguration.set(invIndexOffer + YAML_PATH_VAULT, vault.serialize());
    }
    public ItemStack getOfferItem() {
        return offerItem;
    }

    public void setOfferItem(ItemStack offerItem) {
        this.offerItem = offerItem;
    }

    public ItemStack getPrice() {
        return price;
    }

    public void setPrice(ItemStack price) {
        this.price = price;
    }

    public ShopViewOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ShopViewOrientation orientation) {
        this.orientation = orientation;
    }

    public int getInvIndexOffer() {
        return invIndexOffer;
    }

    public void setInvIndexPrice(int invIndexPrice) {
        this.invIndexPrice = invIndexPrice;
    }
    public void setInvIndexOffer(int invIndexOffer) {
        this.invIndexOffer = invIndexOffer;
    }
    public int getInvIndexPrice() {
        return invIndexPrice;
    }

    public ItemStack getStorage() {
        return storage;
    }

    public void setStorage(ItemStack storage) {
        this.storage = storage;
    }

    public ItemStack getVault() {
        return vault;
    }

    public void setVault(ItemStack vault) {
        this.vault = vault;
    }
}
