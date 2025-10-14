package samuel.player;

import com.fasterxml.jackson.core.type.TypeReference;
import samuel.DirectMessage;
import samuel.Message;
import samuel.MessageType;
import samuel.action.ActionResponse;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.*;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.event.Event;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.phase.Phase;
import samuel.player.action.PlayerAction;
import samuel.player.request.RequestCause;
import samuel.point.Point;
import samuel.point.PointBundle;
import samuel.point.points.ProgressPoint;
import samuel.request.*;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.util.ResourceHelper;
import samuel.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ServerPlayer implements Player {

    private final Board principality;
    private final SocketClient client;
    private final PlayerHand hand;

    private final List<Effect> effects = new ArrayList<>();


    public ServerPlayer(Board principality, PlayerHand hand, SocketClient client) {
        this.principality = principality;
        this.hand = hand;
        this.client = client;
        client.addListener(this::clientRequestHandler);
    }




    @Override
    public Board getPrincipality() {
        return this.principality;
    }

    @Override
    public void giveEffect(Effect effect) {
        effects.add(effect);
    }

    @Override
    public void removeEffect(Effect effect) {
        effects.remove(effect);
    }

    @Override
    public int requestInt(RequestCause cause) {
        return this.requestInt(Integer.MIN_VALUE, Integer.MAX_VALUE, cause);
    }

    @Override
    public int requestInt(int min, int max, RequestCause cause) {
        try {
            int res = client.requestData(new Message(MessageType.REQUEST_INT, new Request(cause.toString(), new IntRequest(min, max))), Integer.class);
            if(res < min) return min;
            if(res > max) return max;
            return res;
        } catch (IOException | InterruptedException exception) {
            // handle the exception
            return min;
        }
    }

    @Override
    public ResourceBundle requestResource(ResourceBundle bundle, int amount, RequestCause cause) {
        try {
            List<ResourceAmount> res = client.requestData(new Message(
                            MessageType.REQUEST_RESOURCE,
                            new Request(cause.toString(), new ResourceRequest(bundle, amount))),
                            new TypeReference<>() {});

            return ResourceBundle.fromAmounts(res);
        } catch (IOException | InterruptedException exception) {

        }

        return null;
    }




    @Override
    public CardStack<PlayableCard> requestCardStack(List<CardStack<PlayableCard>> cardStacks, List<UUID> unselectableStackIds, RequestCause cause) {

        try {
            UUID uuid = client.requestData(new Message(MessageType.REQUEST_CARD_STACK, new Request(cause.toString(), new CardStackRequest(cardStacks, unselectableStackIds))), UUID.class);
            for(CardStack<PlayableCard> stack : cardStacks) {
                if(stack.getUuid().equals(uuid)) return stack;
            }
        } catch (IOException | InterruptedException exception) {

        }

        return null;
    }



    @Override
    public BoardPosition requestBoardPosition(List<List<BoardPosition>> positions, RequestCause cause) {

        try {
            UUID uuid = client.requestData(new Message(MessageType.REQUEST_BOARD_POSITION, new Request(cause.toString(), new BoardPositionRequest(positions))), UUID.class);
            for(List<BoardPosition> row : positions) {
                for(BoardPosition pos : row) {
                    if(pos.getUuid().equals(uuid)) {
                        return pos;
                    }
                }
            }
        } catch (IOException | InterruptedException exception) {

        }

        return null;
    }

    @Override
    public <T extends Card> T requestCard(List<T> cards, RequestCause cause) {
        try {
            UUID uuid = client.requestData(new Message(
                    MessageType.REQUEST_CARD,
                    new Request(cause.toString(), new CardRequest(cards))),
                    UUID.class);

            for(T card : cards) {
                if(card.getUuid().equals(uuid)) return card;
            }
        } catch (IOException | InterruptedException exception) {

        }

        return null;
    }

    @Override
    public boolean requestBoolean(RequestCause cause) {

        try {
            Boolean bool = client.requestData(new Message(MessageType.REQUEST_BOOL, new Request(cause.toString(), null)), Boolean.class);
            return bool;

        } catch (IOException | InterruptedException exception) {

        }

        return false;
    }

    @Override
    public Pair<PlayerAction, BiConsumer<Boolean, String>> requestAction(Phase phase) {
        UUID uuid = UUID.randomUUID();

        System.out.println("Created REQUEST_ACTION with id " + uuid + " at " + phase.toString());

        try {
            PlayerAction action = client.requestData(new Message(MessageType.REQUEST_ACTION, uuid, phase), PlayerAction.class);

            return new Pair<>(action, (success, code) -> {
                System.out.println("Sending ACTION_RESPONSE with id " + uuid + " of " + success + " " + code);
                try {


                    client.sendData(new Message(MessageType.RESPONSE, uuid, new ActionResponse(success, code)));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Subscribe
    @Override
    public void onEvent(Event event) {
        try {
            client.sendData(new Message(MessageType.EVENT, event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public <T extends Point> int getPoints(Class<T> pointClass) {
        int points = 0;

        for(BoardPosition position : this.principality) {
            PlaceableCard card = position.getCard();
            if(card == null) continue;

            if(card instanceof PointHolder holder) {
                PointBundle bundle = holder.getPoints();
                points += bundle.getAmount(pointClass);
            }
        }
        return points;

    }

    @Override
    public PlayerHand getHand() {
        return hand;
    }


    @Override
    public void removeCardFromHand(PlayableCard card) {
        hand.removeCard(card);
    }

    @Override
    public boolean isHandFull() {
        int points = getPoints(ProgressPoint.class);

        return hand.getSize() >= 3 + points;
    }

    @Override
    public void addCardToHand(PlayableCard card) {
        hand.addCard(card);
    }

    @Override
    public PlayableCard getCardInHandFromUuid(UUID uuid) {
        for(PlayableCard card : hand) {
            if(card.getUuid().equals(uuid)) {
                return card;
            }
        }
        return null;
    }

    @Override
    public void placeCard(PlaceableCard card, BoardPosition position, GameContext context) {
        if(card.validatePlacement(position)) {
            this.principality.place(card, position);
            card.onPlace(this, context);
        }
    }

    @Override
    public void playCard(PlayableCard card, GameContext context) {
        if(card instanceof PriceTag price) {
            removeResources(price.getCost());
        }

        if(card instanceof PlaceableCard placeableCard) {
            BoardPosition position = null;
            boolean isValid = false;
            while(!isValid) {
                position = requestBoardPosition(getPrincipality().getBoardPositions(), RequestCause.PLACE_CARD);
                isValid = placeableCard.validatePlacement(position);
                if(!isValid) {
                    directMessage("Invalid Placement");
                }
            }

            placeCard(placeableCard, position, context);
        }
    }

    @Override
    public void giveResources(ResourceBundle bundle) {
        if(bundle == null) return;

        for(ResourceAmount amount : bundle) {
            for(int i = 0; i < amount.amount(); i++) {
                ResourceHelper.increaseRegionOfChoice(this, amount.resourceType(), 1);
            }
        }
    }

    @Override
    public boolean hasResources(ResourceBundle bundle) {
        ResourceBundle has = new ResourceBundle();
        for(BoardPosition position : principality) {
            if(position.getCard() instanceof ResourceHolder holder) {
                ResourceAmount am = holder.getResourceAmount();
                has.addResource(am.resourceType(), am.amount());
            }
        }

        // check if we actually have enough
        for(ResourceAmount am : bundle) {
            int hasAmount = has.getAmount(am.resourceType());
            if(am.amount() > hasAmount) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removeResources(ResourceBundle bundle) {
        for(ResourceAmount am : bundle) {
            int amount = am.amount();
            while(amount > 0) {
                int res = ResourceHelper.decreaseRegionOfChoice(this, am.resourceType(), 1);
                if(res < amount) {
                    amount--;
                }
                // otherwise the region didn't actually
            }
        }
    }

    @Override
    public void directMessage(String msg) {
        sendMessage(new Message(MessageType.DIRECT_MESSAGE, new DirectMessage("server", msg)));
    }


    private void sendMessage(Message msg) {
        try {
            client.sendData(msg);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void addListener(BiConsumer<Message, Player> listener) {
        Player owner = this;
        this.client.addListener(message -> listener.accept(message, owner));
    }

    private void clientRequestHandler(Message request) {
        Object data = switch(request.getType()) {
            case REQUEST_BOARD -> principality;
            case REQUEST_HAND -> hand;
            default -> null;
        };

        sendMessage(new Message(MessageType.RESPONSE, request.getRequestId(), data));
    }
}
