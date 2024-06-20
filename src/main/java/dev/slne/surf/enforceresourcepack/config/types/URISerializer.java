package dev.slne.surf.enforceresourcepack.config.types;

import java.net.URI;
import java.net.URISyntaxException;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class URISerializer implements ValueSerialiser<URI> {

  @Override
  public Class<URI> getTargetClass() {
    return URI.class;
  }

  @Override
  public URI deserialise(FlexibleType flexibleType) throws BadValueException {
    try {
      return new URI(flexibleType.getString());
    } catch (URISyntaxException e) {
      throw new BadValueException.Builder()
          .key(flexibleType.getAssociatedKey())
          .cause(e)
          .message("Failed to parse URI")
          .build();
    }
  }

  @Override
  public Object serialise(URI value, Decomposer decomposer) {
    return value.toString();
  }
}
