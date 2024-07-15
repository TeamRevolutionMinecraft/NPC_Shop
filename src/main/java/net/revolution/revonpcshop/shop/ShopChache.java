package net.revolution.revonpcshop.shop;

import net.revolution.revonpcshop.RevoNPCShop;

import java.util.HashMap;
import java.util.Map;

public class ShopChache {
    private final HashMap<String, Shop> chache;


    public ShopChache() {
        this.chache = new HashMap<>();
    }

    public boolean inChache(String uuid) {
        return chache.containsKey(uuid);
    }

    public Shop fromChache(String uuid) {
        return chache.get(uuid);
    }

    public void toChache(String uuid, Shop shop) {
        this.chache.put(uuid, shop);
    }

    public void unloadShop(String uuid) {
        chache.remove(uuid).saveShop();
        RevoNPCShop.getInstance().getLogger().info("SHOP " + uuid + " was unloaded");
    }
    public void unloadShops() {
        for (Map.Entry<String, Shop> entry : chache.entrySet()) {
            entry.getValue().saveShop();
            RevoNPCShop.getInstance().getLogger().info("SHOP " + entry.getValue().getShopID() + " was unloaded");

        }
    }
}

