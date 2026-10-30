package quest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class QuestCommand implements CommandExecutor {

    private final QuestManager manager;
    public QuestCommand(QuestManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            QuestStatus status = manager.getQuestStatus(player.getUniqueId());

            if (status == QuestStatus.IN_PROGRESS) { // 퀘스트가 진행 중이다?
                    if (manager.getKillCount(player.getUniqueId()) >= 10) {
                        if (manager.giveReward(player)) {
                            manager.completeQuest(player.getUniqueId());
                            player.sendMessage(
                                    Component.text("보상: ", NamedTextColor.GRAY)
                                            .append(Component.text("+1 ", NamedTextColor.WHITE))
                                            .append(Component.text("다이아몬드", NamedTextColor.AQUA)));

                            player.showTitle(Title.title(
                                    Component.text("사냥 완료", NamedTextColor.GRAY),
                                    Component.text("좀비를 모두 처치하였습니다.", NamedTextColor.GRAY)
                            ));
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.8f, 1.0f);
                        } else {
                            player.sendMessage(
                                    Component.text("인벤토리가 가득 찼습니다. quest 명령어를 다시 입력 하십시오.", NamedTextColor.RED));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.2f);
                        }
                    } else {
                        player.sendMessage(
                                Component.text("이미 퀘스트가 진행 중입니다.", NamedTextColor.LIGHT_PURPLE));
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.2f);
                    }

            } else if (status == QuestStatus.COMPLETED) { // 퀘스트가 완료된 상황이다?
                player.sendMessage(
                        Component.text("이미 완료된 퀘스트입니다.", NamedTextColor.LIGHT_PURPLE));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.2f);

            } else {
                manager.startQuest(player.getUniqueId());
                player.showTitle(Title.title(
                        Component.text("사냥 임무", NamedTextColor.YELLOW),
                        Component.text("좀비 10마리를 처치하십시오.", NamedTextColor.GRAY)
                ));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.8f, 1.2f);
            }
        }
        return true;
    }
}
