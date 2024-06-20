package dev.slne.surf.enforceresourcepack.config.types;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public record OptionalUUID(UUID uuid) {
  public OptionalUUID(@Nullable UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public @Nullable UUID uuid() {
    return this.uuid;
  }

  public @NotNull UUID getUuidOrRandom() {
    return this.uuid != null ? this.uuid : UUID.randomUUID();
  }

  public static class OptionalUUIDSerializer implements ValueSerialiser<OptionalUUID> {

    @Override
    public Class<OptionalUUID> getTargetClass() {
      return OptionalUUID.class;
    }

    @Override
    public OptionalUUID deserialise(FlexibleType flexibleType) throws BadValueException {
      final String value = flexibleType.getString();

      if (value.equals("empty")) {
        return new OptionalUUID(null);
      }

      try {
        final UUID parsed = UUID.fromString(value);
        return new OptionalUUID(parsed);
      } catch (IllegalArgumentException e) {
        throw new BadValueException.Builder()
            .key(flexibleType.getAssociatedKey())
            .cause(e)
            .message("Invalid UUID string")
            .build();
      }
    }

    @Override
    public Object serialise(OptionalUUID value, Decomposer decomposer) {
      final UUID uuid = value.uuid();

      if (uuid == null) {
        return "empty";
      }

      return uuid.toString();
    }
  }
}
