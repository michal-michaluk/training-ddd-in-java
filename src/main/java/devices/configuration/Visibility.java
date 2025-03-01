package devices.configuration;

public record Visibility(VisibilityLevel forCustomer, boolean roamingEnabled) {
    public static Visibility calculate(Settings settings, Violations violations) {
        boolean usable = violations.none();
        boolean visible = settings.showOnMap();
        boolean roaming = usable || settings.publicAccess();

        VisibilityLevel level = usable ?
                (visible ? VisibilityLevel.USABLE_AND_VISIBLE_ON_MAP : VisibilityLevel.USABLE_BUT_HIDDEN_ON_MAP)
                : VisibilityLevel.INACCESSIBLE_AND_HIDDEN_ON_MAP;

        return new Visibility(level, roaming);
    }
}