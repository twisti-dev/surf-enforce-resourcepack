package dev.slne.surf.enforceresourcepack.listener.configuration;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerResourcePackStatusEvent;
import com.velocitypowered.api.event.player.configuration.PlayerFinishConfigurationEvent;
import com.velocitypowered.api.proxy.Player;
import dev.slne.surf.enforceresourcepack.config.EnforceResourcepackConfig;
import dev.slne.surf.enforceresourcepack.config.EnforceResourcepackConfig.PackConfig;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.jetbrains.annotations.NotNull;

public class PlayerFinishConfigurationListener {

  public static final PlayerFinishConfigurationListener INSTANCE = new PlayerFinishConfigurationListener();
  private final Map<Player, ResourcePackRequest> pendingRequests = new ConcurrentHashMap<>();
  private final Map<Player, Set<UUID>> finishedPacks = new ConcurrentHashMap<>();

  @Subscribe
  public void onPlayerFinishConfiguration(PlayerFinishConfigurationEvent event,
      Continuation continuation) {
    final EnforceResourcepackConfig config = EnforceResourcepackConfig.get();
    List<? extends ResourcePackInfoLike> packs = getPacks();
    @NotNull ResourcePackRequest packRequest = ResourcePackRequest.resourcePackRequest()
        .prompt(config.prompt())
        .required(true)
        .replace(config.replace())
        .packs(packs)
        .callback((uuid, status, audience) -> {
          switch (status) {
            case DECLINED, DISCARDED, FAILED_DOWNLOAD, FAILED_RELOAD, INVALID_URL -> {
              final Player player = (Player) audience;
              player.disconnect(EnforceResourcepackConfig.get().declineMessage());
              pendingRequests.remove(player);
              finishedPacks.remove(player);
            }
            case ACCEPTED, DOWNLOADED -> {
              // wait until successfully loaded
            }
            case SUCCESSFULLY_LOADED -> {
              final Set<UUID> finished = finishedPacks.computeIfAbsent((Player) audience,
                  player -> new HashSet<>());

              if (finished.add(uuid)) {
                if (finished.size() == packs.size()) {
                  continuation.resume();
                  finishedPacks.remove(audience);
                  pendingRequests.remove(audience);
                }
              }
            }
          }
        }).build();

    final Player player = event.player();
    pendingRequests.put(player, packRequest);
    player.sendResourcePacks(packRequest);
  }

  @Subscribe
  public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
    final Player player = event.getPlayer();
    final UUID packUuid = event.getPackId();
    final ResourcePackRequest request = pendingRequests.get(player);
    if (request == null || packUuid == null) {
      return;
    }

    if (request.packs().stream().noneMatch(pack -> pack.id().equals(packUuid))) {
      return;
    }

    request.callback().packEventReceived(
        packUuid,
        switch (event.getStatus()) {
          case SUCCESSFUL -> ResourcePackStatus.SUCCESSFULLY_LOADED;
          case DECLINED -> ResourcePackStatus.DECLINED;
          case FAILED_DOWNLOAD -> ResourcePackStatus.FAILED_DOWNLOAD;
          case ACCEPTED -> ResourcePackStatus.ACCEPTED;
          case DOWNLOADED -> ResourcePackStatus.DOWNLOADED;
          case INVALID_URL -> ResourcePackStatus.INVALID_URL;
          case FAILED_RELOAD -> ResourcePackStatus.FAILED_RELOAD;
          case DISCARDED -> ResourcePackStatus.DISCARDED;
        },
        player
    );
  }

  private List<? extends ResourcePackInfoLike> getPacks() {
    final List<PackConfig> packs = EnforceResourcepackConfig.get().packs();

    return packs.stream()
        .map(pack -> ResourcePackInfo.resourcePackInfo()
            .id(pack.uuid().getUuidOrRandom())
            .uri(pack.url())
            .hash(pack.hash()))
        .toList();
  }
}
