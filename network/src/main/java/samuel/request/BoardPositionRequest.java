package samuel.request;

import java.util.Optional;

public record BoardPositionRequest(Object positions, Object metadata) {

    public BoardPositionRequest(Object positions) {
        this(positions, null);
    }

}
