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

    private final PlayerNetworkHelper network;

    private final PlayerHand hand;

    private final List<Effect> effects = new ArrayList<>();

    private final UUID uuid = UUID.randomUUID();


    public ServerPlayer(Board principality, PlayerHand hand, PlayerNetworkHelper network) {
        this.principality = principality;
        this.hand = hand;
        this.network = network;
        if(network != null) {
            addListener(this::clientRequestHandler);
        }
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
        return this.network.requestInt(min, max, cause);
    }

    @Override
    public ResourceBundle requestResource(ResourceBundle bundle, int amount, RequestCause cause) {
        return this.network.requestResource(bundle, amount, cause);
    }

    @Override
    public CardStack<PlayableCard> requestCardStack(List<CardStack<PlayableCard>> cardStacks, List<UUID> unselectableStackIds, RequestCause cause) {
        return this.network.requestCardStack(cardStacks, unselectableStackIds, cause);
    }

    @Override
    public BoardPosition requestBoardPosition(List<List<BoardPosition>> positions, RequestCause cause) {
        return this.network.requestBoardPosition(positions, cause);
    }

    @Override
    public <T extends Card> T requestCard(List<T> cards, RequestCause cause) {
        return this.network.requestCard(cards, cause);
    }

    @Override
    public boolean requestBoolean(RequestCause cause) {
        return this.network.requestBoolean(cause);
    }

    @Override
    public Pair<PlayerAction, BiConsumer<Boolean, String>> requestAction(Phase phase) {
        return this.network.requestAction(phase);
    }

    @Override
    public void directMessage(String msg) {
        this.network.directMessage(msg);
    }





    @Subscribe
    public void onEvent(Event event) {
        if(event instanceof PlayerEvent playerEvent) {
            if(!playerEvent.getPlayer().equals(this)) return;
        }

        sendMessage(new Message(MessageType.EVENT, event));
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








    public void sendMessage(Message msg) {
        this.network.sendMessage(msg);
    }

    public void addListener(BiConsumer<Message, Player> listener) {
        this.network.addListener(listener, this);
    }

    private void clientRequestHandler(Message request, Player player) {
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
