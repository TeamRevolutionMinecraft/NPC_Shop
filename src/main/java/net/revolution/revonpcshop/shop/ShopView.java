package net.revolution.revonpcshop.shop;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;

public class ShopView {

    private final List<ShopOffer> shopItems;

    public ShopView() {
        this.shopItems = new ArrayList<>();
    }

    public void addOffer(ShopOffer offer) {
        shopItems.add(offer);
    }
    public void addShopItems(Inventory inventory) {
        for (ShopOffer offer : shopItems) {
            inventory.setItem(offer.getInvIndexOffer(), offer.getOfferItem());
            switch (offer.getOrientation()) {
                case DOWN2UP:
                    inventory.setItem(offer.getInvIndexOffer() - 9, offer.getPrice());
                    break;
                case UP2DOWN:
                    inventory.setItem(offer.getInvIndexOffer() + 9, offer.getPrice());
                    break;
                case LEFT2RIGHT:
                    inventory.setItem(offer.getInvIndexOffer() + 1, offer.getPrice());

                    break;
                case RIGHT2LEFT:
                    inventory.setItem(offer.getInvIndexOffer() - 1, offer.getPrice());
                    break;
            }
        }
    }

    public List<ShopOffer> getShopItems() {
        return shopItems;
    }

    public void loadShopView(YamlConfiguration shopConfig) {
        for (String key : shopConfig.getKeys(false)) {
            if (key.equalsIgnoreCase("META")) continue;
            if (key.equalsIgnoreCase("STORAGE")) continue;
            shopItems.add(ShopOffer.buildOffer(shopConfig, Integer.parseInt(key)));
        }
    }

    public ShopOffer getShopOfferAtSlot(int index) {
        for (ShopOffer offer : shopItems) {
            if (offer.getInvIndexOffer() == index || offer.getInvIndexPrice() == index)
                return offer;
        }
        return null;
    }
}
