package quest;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestManager {


    Map<UUID, QuestStatus> questStatus = new HashMap<>();  // 퀘스트 상태 저장
    Map<UUID, Integer> killCounts = new HashMap<>();

    public boolean isInProgress(UUID uuid) {  // 퀘스트 진행 중
        return questStatus.getOrDefault(uuid, QuestStatus.NOT_STARTED) == QuestStatus.IN_PROGRESS;
    }                         //~인지 아닌지 getOrDefault
                              // 상태가 없으면 NOT_STARTED를 가져온다, 가져온 상태가 IN_PROGRESS와 같은지 비교한다.
                              // 같으면 true, 다르면 false를 반환한다.

    public void startQuest(UUID uuid) {  // 퀘스트 시작
        questStatus.put(uuid, QuestStatus.IN_PROGRESS);
        killCounts.put(uuid, 0);
    }

    public void completeQuest(UUID uuid) {
        questStatus.put(uuid, QuestStatus.COMPLETED);
    }

    public void addKill(UUID uuid) {
        int count = killCounts.getOrDefault(uuid, 0); // getOrDefault: 해당 키에 값이 있으면 가져오고 없으면 지정한 기본값(0)을 반환한다.
        killCounts.put(uuid, count + 1);
    }

    public int getKillCount(UUID uuid) {
        return killCounts.getOrDefault(uuid, 0);
    }

    public boolean giveReward(Player player) { // true: 지급 성공 / false: 지급 실패
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        return leftovers.isEmpty();
    }

    public QuestStatus getQuestStatus(UUID uuid) { // 해당 플레이어의 퀘스트 상태를 반환
        return questStatus.getOrDefault(uuid, QuestStatus.NOT_STARTED); // 퀘스트 상태가 없을 때는 NOT_STARTED를 반환한다.
    }


}