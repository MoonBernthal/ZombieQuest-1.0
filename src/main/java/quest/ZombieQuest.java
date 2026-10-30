package quest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieQuest extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("ZombieQuest 활성화됨.");

        QuestManager manager = new QuestManager();

        getServer().getScheduler().runTaskTimer(this, () -> {

            for (Player player : getServer().getOnlinePlayers()) {
                if (manager.isInProgress(player.getUniqueId())) {
                    player.sendActionBar(Component.text( "좀비 처치 : ", NamedTextColor.GRAY)
                            .append(Component.text( manager.getKillCount(player.getUniqueId()), NamedTextColor.GREEN)
                            .append(Component.text(" / 10", NamedTextColor.WHITE))));
                }
            }

        }, 0L, 20L);



        getCommand("quest").setExecutor(new QuestCommand(manager));

        getServer().getPluginManager().registerEvents(new QuestListener(manager), this);
    }

    @Override
    public void onDisable() {

    }
}
