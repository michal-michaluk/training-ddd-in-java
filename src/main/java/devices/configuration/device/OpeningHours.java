package devices.configuration.device;

public record OpeningHours( Boolean alwaysOpen ) {
  public static OpeningHours alwaysOpened(){
      return new OpeningHours(true);
  }
}
