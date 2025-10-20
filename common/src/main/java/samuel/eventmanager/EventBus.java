package samuel.eventmanager;

import samuel.event.Event;

/**
 * The event bus which handles registering/unregistering listeners as well as firing events.
 */
public interface EventBus {

    /**
     * Register a new listener. Registers all methods with <code>@Subscribe</code> annotation.
     * @param listener
     */
    void register(Object listener);

    /**
     * Unregisters all methods in listener.
     * @param listener
     */
    void unregister(Object listener);

    /**
     * Calls all the listeners who listen to this event or any of its subclasses.
     * @param event
     */
    void fireEvent(Event event);
}
