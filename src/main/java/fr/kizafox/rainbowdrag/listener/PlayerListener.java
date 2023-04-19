package fr.kizafox.rainbowdrag.listener;

import fr.kizafox.rainbowdrag.RainbowDrag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Change this line to a short description of the class
 *
 * @author : KIZAFOX
 * @date : 19/04/2023
 * @project : RainbowDrag
 */
public class PlayerListener implements Listener {

    protected final RainbowDrag instance;

    private final List<Material> MATERIALS;
    private final Random RANDOM;

    private final Map<UUID, Map<Material, Location>> OLD_MATERIALS;

    public PlayerListener(final RainbowDrag instance) {
        this.instance = instance;

        this.MATERIALS = new ArrayList<>();
        this.RANDOM = new Random();

        this.OLD_MATERIALS = new HashMap<>();

        for(String materialsName : this.instance.getConfig().getStringList("blocks-name")){
            this.MATERIALS.add(Material.getMaterial(materialsName));
            RainbowDrag.LOGGER.info("Materials successfully added from the config.yml!");
        }

        this.instance.getServer().getPluginManager().registerEvents(this, this.instance);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event){
        final Location from = event.getFrom();
        final Location to = event.getTo();

        assert to != null;
        if(!isPlayerActuallyMoving(from, to)) return;

        final Player player = event.getPlayer();
        final Location blockBelow = player.getLocation().subtract(0, 1, 0);

        if(blockBelow.getBlock().getType().equals(Material.AIR)) return;

        final Material randomMaterial = MATERIALS.get(RANDOM.nextInt(MATERIALS.size()));

        OLD_MATERIALS.put(player.getUniqueId(), new HashMap<>());
        OLD_MATERIALS.get(player.getUniqueId()).put(blockBelow.getBlock().getType(), player.getLocation());

        new BukkitRunnable(){
            @Override
            public void run() {
                blockBelow.getBlock().setType(randomMaterial);

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        OLD_MATERIALS.get(player.getUniqueId()).forEach((key, value) -> blockBelow.getBlock().setType(key));
                    }
                }.runTaskLater(instance, 20);
            }
        }.runTaskLater(this.instance, 1);
    }

    private boolean isPlayerActuallyMoving(Location from, Location to) {
        return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }
}
