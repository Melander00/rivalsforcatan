package samuel.eventmanager;

import samuel.event.Event;

public interface EventBus {
    void register(Object listener);

    void fireEvent(Event event);
}
