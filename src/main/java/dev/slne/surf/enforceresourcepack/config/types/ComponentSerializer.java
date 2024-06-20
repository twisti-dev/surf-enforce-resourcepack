package dev.slne.surf.enforceresourcepack.config.types;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.ParsingException;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class ComponentSerializer implements ValueSerialiser<Component> {

  @Override
  public Class<Component> getTargetClass() {
    return Component.class;
  }

  @Override
  public Component deserialise(FlexibleType flexibleType) throws BadValueException {
    try {
      return MiniMessage.miniMessage().deserialize(flexibleType.getString());
    } catch (ParsingException e) {
      throw new BadValueException.Builder()
          .key(flexibleType.getAssociatedKey())
          .cause(e)
          .message("Failed to parse MiniMessage")
          .build();
    }
  }

  @Override
  public Object serialise(Component value, Decomposer decomposer) {
    return MiniMessage.miniMessage().serialize(value);
  }
}
