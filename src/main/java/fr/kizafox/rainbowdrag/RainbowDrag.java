package fr.kizafox.rainbowdrag;

import fr.kizafox.rainbowdrag.listener.PlayerListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class RainbowDrag extends JavaPlugin implements Listener {

    public static RainbowDrag instance;

    public static Logger LOGGER = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfig();

        new PlayerListener(this);

        LOGGER.info("Plugin successfully enabled!");
    }

    public static RainbowDrag get() {
        return instance;
    }

    private void loadConfig(){
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        LOGGER.info("Config.yml successfully loaded!");

    }
}
