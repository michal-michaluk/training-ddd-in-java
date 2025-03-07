package devices.configuration.device;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OwnershipTest {

    @Test
    void unowned() {
        Ownership ownership = Ownership.unowned();

        assertThat(ownership.isUnowned()).isTrue();
    }

    @Test
    void owned() {
        Ownership ownership = new Ownership("operator", "provider");

        assertThat(ownership.isUnowned()).isFalse();
        assertThat(ownership)
                .extracting(Ownership::operator, Ownership::provider)
                .containsExactly("operator", "provider");
    }

    @Test
    void illegalValueWithMissingProvider() {
        Assertions.assertThatThrownBy(() -> new Ownership("operator", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void illegalValueWithMissingOperator() {
        Assertions.assertThatThrownBy(() -> new Ownership(null, "provider"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
