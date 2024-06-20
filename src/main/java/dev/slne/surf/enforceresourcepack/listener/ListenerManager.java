package dev.slne.surf.enforceresourcepack.listener;

import dev.slne.surf.enforceresourcepack.SurfEnforceResourcepack;
import dev.slne.surf.enforceresourcepack.listener.configuration.PlayerFinishConfigurationListener;

public final class ListenerManager {
  public static final ListenerManager INSTANCE = new ListenerManager();

  private ListenerManager() {
  }

  public void registerListeners() {
    register(PlayerFinishConfigurationListener.INSTANCE);
  }

  private void register(Object listener) {
    SurfEnforceResourcepack.getServer()
        .getEventManager()
        .register(SurfEnforceResourcepack.getInstance(), listener);
  }
}
