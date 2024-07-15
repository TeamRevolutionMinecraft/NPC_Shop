package net.revolution.revonpcshop;

import dev.sergiferry.playernpc.api.NPCLib;
import net.revolution.revonpcshop.commands.CMDRemoveAllNPCs;
import net.revolution.revonpcshop.commands.CMDSpawnNPC;
import net.revolution.revonpcshop.shop.Shop;
import net.revolution.revonpcshop.shop.ShopChache;
import net.revolution.revonpcshop.shop.ShopListener;
import net.revolution.revonpcshop.shop.ShopNPC;
import org.bukkit.plugin.java.JavaPlugin;

public final class RevoNPCShop extends JavaPlugin {

    private static RevoNPCShop instance;
    private static ShopChache shopChache;
    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        shopChache = new ShopChache();
        Shop.init(instance);
        NPCLib.getInstance().registerPlugin(this);
        new CMDSpawnNPC(this);
        new CMDRemoveAllNPCs(this);
        new ShopListener(this);
        ShopNPC.loadNPCS();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        shopChache.unloadShops();
    }
    public static RevoNPCShop getInstance() {
        return instance;
    }

    public static ShopChache getShopChache() {
        return shopChache;
    }
}
