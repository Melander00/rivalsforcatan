package samuel.network;

import com.fasterxml.jackson.core.type.TypeReference;
import samuel.Message;

import java.util.function.Consumer;

public interface NetworkClient {

    void sendData(Message message);

    <T> T requestData(Message message, Class<T> clazz);

    <T> T requestData(Message message, TypeReference<T> typeReference);

    void addListener(Consumer<Message> listener);

}
