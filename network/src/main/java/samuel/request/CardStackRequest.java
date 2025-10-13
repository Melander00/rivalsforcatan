package samuel.request;

import java.util.List;
import java.util.UUID;

public record CardStackRequest(Object cardStacks, List<UUID> forbiddenStackIds) {
}
