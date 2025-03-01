package devices.configuration;

import lombok.Builder;

@Builder
public record Coordinates(double longitude, double latitude)
{

}