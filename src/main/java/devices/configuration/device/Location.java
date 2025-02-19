package devices.configuration.device;

import java.math.BigDecimal;

public record Location(
      String  street,
      String  houseNumber,
      String  city,
      String  postalCode,
      String  country,
      Coordinates coordinates )
{
   public record Coordinates (
       BigDecimal longitude,
       BigDecimal    latitude){}
}
