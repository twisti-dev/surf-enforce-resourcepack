package dev.slne.surf.enforceresourcepack.config;

import dev.slne.surf.enforceresourcepack.SurfEnforceResourcepack;
import dev.slne.surf.enforceresourcepack.config.types.ComponentSerializer;
import dev.slne.surf.enforceresourcepack.config.types.OptionalUUID;
import dev.slne.surf.enforceresourcepack.config.types.URISerializer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault.DefaultBoolean;
import space.arim.dazzleconf.annote.ConfDefault.DefaultObject;
import space.arim.dazzleconf.annote.ConfDefault.DefaultString;
import space.arim.dazzleconf.annote.ConfSerialisers;
import space.arim.dazzleconf.annote.SubSection;

@ConfSerialisers(ComponentSerializer.class)
public interface EnforceResourcepackConfig {

  @DefaultString("<red>Download our resource pack to play on this server!")
  @ConfComments("Sets the prompt.")
  Component prompt();

  @DefaultString("<red>You need to download our resource pack to play on this server!")
  @ConfComments("Sets the decline message.")
  Component declineMessage();

  @DefaultBoolean(false)
  @ConfComments("Set whether to replace or add to existing resource packs.")
  boolean replace();

  @ConfComments({
      "Sets the packs to be enforced.",
      "",
      "Example:",
      "packs:",
      "  - url: 'https://download.mc-packs.net/pack/36cf7ebb454167b8629e162f5af2c2b3466742e4.zip'",
      "    hash: '36cf7ebb454167b8629e162f5af2c2b3466742e4'",
      "    uuid: 'empty' or '00000000-0000-0000-0000-000000000000'"
  })
  @DefaultObject("dev.slne.surf.enforceresourcepack.config.EnforceResourcepackConfig.defaultPacks")
  List<@SubSection PackConfig> packs();

  @ConfSerialisers({OptionalUUID.OptionalUUIDSerializer.class, URISerializer.class})
  interface PackConfig {

    URI url();

    String hash();

    OptionalUUID uuid();
  }

  static EnforceResourcepackConfig get() {
    return SurfEnforceResourcepack.getInstance().getConfigManager().getConfigData();
  }

  @SuppressWarnings("unused") // Used by DazzleConf
  static List<PackConfig> defaultPacks() {
    return new ArrayList<>();
  }
}
