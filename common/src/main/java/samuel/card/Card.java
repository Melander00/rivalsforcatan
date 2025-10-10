package samuel.card;

public interface Card {

    /**
     * Returns the CardID of the card.
     * @return
     */
    CardID getCardID();

//    static CardID getCardID() {
//        return null;
//    }

//    /**
//     * Unique identifier
//     * @return
//     */
//    UUID getUuid();
//
//    /**
//     * Unique identifier
//     * @param uuid
//     */
//    void setUuid(UUID uuid);


    // The idea is that the Client is responsible for supplying names and descriptions for the cards
    // That way, we can have localization and/or change names without having to touch the core functionality of the game
    // Also, if the Client wants a graphical ui, it is up to the client to implement how the cards are rendered
    // so it makes sense that the client keeps the names and descriptions.

}
