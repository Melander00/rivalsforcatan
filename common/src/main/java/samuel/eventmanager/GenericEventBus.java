package samuel.eventmanager;

import samuel.event.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericEventBus implements EventBus {

    private final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    @Override
    public void register(Object listener) {
        for(Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length != 1)
                    throw new IllegalArgumentException("@Subscribe method must take exactly one parameter");

                Class<?> eventType = params[0];
                method.setAccessible(true);

                listeners
                        .computeIfAbsent(eventType, k -> new ArrayList<>())
                        .add(new Listener(listener, method));
            }
        }
    }

    @Override
    public void fireEvent(Event event) {
        List<Listener> listeners = this.listeners.get(event.getClass());
        if(listeners != null) {
            for(Listener listener : listeners) {
                try {
                    listener.method.invoke(listener.target, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private record Listener(Object target, Method method) {}
}
