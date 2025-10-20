package samuel.player;

import com.fasterxml.jackson.core.type.TypeReference;
import samuel.DirectMessage;
import samuel.Message;
import samuel.MessageType;
import samuel.action.ActionResponse;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.*;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.event.Event;
import samuel.event.PlayerEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.network.NetworkClient;
import samuel.phase.Phase;
import samuel.player.action.PlayerAction;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.point.Point;
import samuel.point.PointAmount;
import samuel.point.PointBundle;
import samuel.point.points.ProgressPoint;
import samuel.request.*;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.util.ResourceHelper;
import samuel.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ServerPlayer implements Player {

    private final Board principality;
    private final NetworkClient client;
    private final PlayerHand hand;

    private final List<Effect> effects = new ArrayList<>();

    private final UUID uuid = UUID.randomUUID();


    public ServerPlayer(Board principality, PlayerHand hand, NetworkClient client) {
        this.principality = principality;
        this.hand = hand;
        this.client = client;
        client.addListener(this::clientRequestHandler);
    }


    public UUID getUuid() {
        return uuid;
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
        int res = client.requestData(new Message(MessageType.REQUEST_INT, new Request(cause, new IntRequest(min, max))), Integer.class);
        if(res < min) return min;
        if(res > max) return max;
        return res;
    }

    @Override
    public ResourceBundle requestResource(ResourceBundle bundle, int amount, RequestCause cause) {
        List<ResourceAmount> res = client.requestData(new Message(
                        MessageType.REQUEST_RESOURCE,
                        new Request(cause, new ResourceRequest(bundle, amount))),
                        new TypeReference<>() {});

        return ResourceBundle.fromAmounts(res);
    }




    @Override
    public CardStack<PlayableCard> requestCardStack(List<CardStack<PlayableCard>> cardStacks, List<UUID> unselectableStackIds, RequestCause cause) {

        UUID uuid = client.requestData(new Message(MessageType.REQUEST_CARD_STACK, new Request(cause, new CardStackRequest(cardStacks, unselectableStackIds))), UUID.class);
        for(CardStack<PlayableCard> stack : cardStacks) {
            if(stack.getUuid().equals(uuid)) return stack;
        }

        return null;
    }



    @Override
    public BoardPosition requestBoardPosition(List<List<BoardPosition>> positions, RequestCause cause) {

        UUID uuid = client.requestData(new Message(MessageType.REQUEST_BOARD_POSITION, new Request(cause, new BoardPositionRequest(positions))), UUID.class);
        for(List<BoardPosition> row : positions) {
            for(BoardPosition pos : row) {
                if(pos.getUuid().equals(uuid)) {
                    return pos;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends Card> T requestCard(List<T> cards, RequestCause cause) {
        UUID uuid = client.requestData(new Message(
                MessageType.REQUEST_CARD,
                new Request(cause, new CardRequest(cards))),
                UUID.class);

        for(T card : cards) {
            if(card.getUuid().equals(uuid)) return card;
        }

        return null;
    }

    @Override
    public boolean requestBoolean(RequestCause cause) {
        Boolean bool = client.requestData(new Message(MessageType.REQUEST_BOOL, new Request(cause, null)), Boolean.class);
        return bool;
    }

    @Override
    public Pair<PlayerAction, BiConsumer<Boolean, String>> requestAction(Phase phase) {
        UUID uuid = UUID.randomUUID();

        PlayerAction action = client.requestData(new Message(MessageType.REQUEST_ACTION, uuid, phase), PlayerAction.class);

        return new Pair<>(action, (success, code) -> {
            client.sendData(new Message(MessageType.RESPONSE, uuid, new ActionResponse(success, code)));
        });
    }

    @Subscribe
    @Override
    public void onEvent(Event event) {
        if(event instanceof PlayerEvent playerEvent) {
            if(!playerEvent.getPlayer().equals(this)) return;
        }

        client.sendData(new Message(MessageType.EVENT, event));
    }


    @Override
    public <T extends Point> int getPoints(Class<T> pointClass) {
        PointBundle bundle = getPoints();
        return bundle.getAmount(pointClass);
    }

    @Override
    public PointBundle getPoints() {
        PointBundle points = new PointBundle();

        for(BoardPosition position : this.principality) {
            PlaceableCard card = position.getCard();
            if(card == null) continue;

            if(card instanceof PointHolder holder) {
                PointBundle bundle = holder.getPoints();
                for(PointAmount am : bundle) {
                    points.addPoint(am.pointType(), am.amount());
                }
            }
        }

        return points;
    }

    @Override
    public boolean hasPointAdvantage(Class<? extends Point> pointType, GameContext context) {
        boolean hasAdvantage = true;
        for(Player player : context.getPlayers()) {
            if(player.equals(this)) continue;
            if(player.getPoints(pointType) > this.getPoints(pointType)) {
                hasAdvantage = false;
                break;
            }
        }
        return hasAdvantage;
    }

    @Override
    public PlayerHand getHand() {
        return hand;
    }

    @Override
    public int getMaxHandSize() {
        int points = getPoints(ProgressPoint.class);

        return 3 + points;
    }

    @Override
    public void removeCardFromHand(PlayableCard card) {
        hand.removeCard(card);
    }

    @Override
    public boolean isHandFull() {
        int maxSize = getMaxHandSize();

        return hand.getSize() >= maxSize;
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
            card.onPlace(this, context, position);
        }
    }

    @Override
    public void removeCard(PlaceableCard card, BoardPosition position, GameContext context) {
        position.setCard(null);
        card.onRemove(context);
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
                position = requestBoardPosition(getPrincipality().getBoardPositions(), new RequestCause(RequestCauseEnum.PLACE_CARD, card));
                isValid = placeableCard.validatePlacement(position);
                if(!isValid) {
                    directMessage("Invalid Placement");
                }
            }

            placeCard(placeableCard, position, context);
        }

        card.onPlay(this, context);

    }

    @Override
    public void giveResources(ResourceBundle bundle) {
        if(bundle == null) return;

        for(ResourceAmount am : bundle) {
            int amount = am.amount();
            while(amount > 0) {
                int res = ResourceHelper.increaseHolderOfChoice(this, am.resourceType(), 1);

//                deadlock if there is no region without max resources.
//                if(res < amount) {
//                    amount--;
//                } else {
//                    directMessage("That region already has max amount of resources.");
//                }
                // fix by always lowering amount. we let the user make sure that the region isn't full
                amount--;
            }
        }
    }

    @Override
    public boolean hasResources(ResourceBundle bundle) {
        ResourceBundle has = new ResourceBundle();
        for(BoardPosition position : principality) {
            if(position.getCard() instanceof ResourceHolder holder) {
                ResourceBundle b = holder.getResources();
                for(ResourceAmount am : b) {
                    has.addResource(am.resourceType(), am.amount());
                }
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
                int res = ResourceHelper.decreaseHolderOfChoice(this, am.resourceType(), 1);
//                  deadlock
//                if(res < amount) {
//                    amount--;
//                } else {
//                    directMessage("That region has no resources to remove from.");
//                }
                amount--;
            }
        }
    }

    @Override
    public ResourceBundle getResources() {
        ResourceBundle resources = new ResourceBundle();

        for(BoardPosition position : this.principality) {
            PlaceableCard card = position.getCard();
            if(card == null) continue;

            if(card instanceof ResourceHolder holder) {
                ResourceBundle bundle = holder.getResources();
                for(ResourceAmount am : bundle) {
                    resources.addResource(am.resourceType(), am.amount());
                }
            }
        }

        return resources;
    }

    @Override
    public <T extends Resource> int getResources(Class<T> resource) {
        ResourceBundle bundle = getResources();
        return bundle.getAmount(resource);
    }

    @Override
    public void directMessage(String msg) {
        sendMessage(new Message(MessageType.DIRECT_MESSAGE, new DirectMessage("Server", msg)));
    }


    public void sendMessage(Message msg) {
        client.sendData(msg);
    }

    public void addListener(BiConsumer<Message, Player> listener) {
        Player owner = this;
        this.client.addListener(message -> listener.accept(message, owner));
    }

    private void clientRequestHandler(Message request) {
        if(request == null || request.getType() == null) return;

        Object data = switch(request.getType()) {
            case REQUEST_BOARD -> principality;
            case REQUEST_HAND -> hand;
            default -> null;
        };

        if(data == null) return;

        sendMessage(new Message(MessageType.RESPONSE, request.getRequestId(), data));
    }
}
