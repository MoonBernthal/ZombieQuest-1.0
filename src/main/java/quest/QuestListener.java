package quest;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import net.kyori.adventure.text.Component;

public class QuestListener implements Listener {

    private final QuestManager manager;
    public QuestListener(QuestManager manager) {
        this.manager = manager;
    }

    @EventHandler // 이벤트를 처리할 메서드에는 이 어노테이션이 붙음
    public void onZombieDeath(EntityDeathEvent event) { // on~ 이름은 임의로 지정가능
        if (event.getEntity() instanceof Zombie) { // 죽은 엔티티가 좀비라면
            Player killer = event.getEntity().getKiller();  // 좀비를 죽인 플레이어를 killer로 변수 네이밍

            if (killer != null) { // 처치자로 기록된 플레이어가 존재하는 경우
                if (manager.isInProgress(killer.getUniqueId())) {
                    manager.addKill(killer.getUniqueId());

                    int count = manager.getKillCount(killer.getUniqueId());
                    if (count >= 10) {
                        if (manager.giveReward(killer)) {
                            manager.completeQuest(killer.getUniqueId());
                            killer.sendMessage(
                                    Component.text("보상: ", NamedTextColor.GRAY)
                                            .append(Component.text("+1 ", NamedTextColor.WHITE))
                                            .append(Component.text("다이아몬드", NamedTextColor.AQUA)));

                            killer.showTitle(Title.title(
                                    Component.text("사냥 완료", NamedTextColor.GRAY),
                                    Component.text("좀비를 모두 처치하였습니다.", NamedTextColor.GRAY)
                            ));
                            killer.playSound(killer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.8f, 1.0f);
                            killer.sendActionBar(Component.empty());
                        } else {
                            killer.sendMessage(
                                    Component.text("인벤토리가 가득 찼습니다. quest 명령어를 다시 입력 하십시오.", NamedTextColor.RED));
                            killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.2f);
                        }
                    }
                }
            }
       }
   }}