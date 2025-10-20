package samuel.test.server.introductory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.introductory.IntroductoryGame;
import samuel.player.Player;
import samuel.stack.GenericCardStack;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InitialDrawTest {

    @Mock
    private Player player;

    private IntroductoryGame game;
    private CardStack<PlayableCard> stack1;
    private CardStack<PlayableCard> stack2;
    private CardStack<PlayableCard> stack3;

    private List<CardStack<PlayableCard>> stacks;

    @Mock
    private PlayableCard mockCard;

    @BeforeEach
    void setup() {
        game = new IntroductoryGame();


        stack1 = new GenericCardStack<>();
        stack1.addCardToBottom(mockCard);
        stack1.addCardToBottom(mock(PlayableCard.class));
        stack1.addCardToBottom(mock(PlayableCard.class));

        stack2 = new GenericCardStack<>();
        stack2.addCardToBottom(mock(PlayableCard.class));
        stack2.addCardToBottom(mock(PlayableCard.class));
        stack2.addCardToBottom(mock(PlayableCard.class));

        stack3 = new GenericCardStack<>();
        stack3.addCardToBottom(mock(PlayableCard.class));
        stack3.addCardToBottom(mock(PlayableCard.class));
        stack3.addCardToBottom(mock(PlayableCard.class));

        stacks = List.of(stack1, stack2, stack3);
    }


    @Test
    void testDrawThree() {
        when(player.requestCardStack(any(),any(),any())).thenReturn(stack1);

        game.setupInitialDraw(player, stacks, List.of());

        verify(player, times(3)).addCardToHand(any());
    }

    @Test
    void testChooseSame() {
        Player opponent = mock(Player.class);
        when(player.requestCardStack(any(),any(),any())).thenReturn(stack1);
        when(opponent.requestCardStack(any(), any(), any())).thenReturn(stack1);

        UUID uuid = game.setupInitialDraw(player, stacks, List.of());
        game.setupInitialDraw(opponent, stacks, List.of(uuid));

        verify(player, times(1)).addCardToHand(mockCard);
        verify(opponent, times(0)).addCardToHand(mockCard);
        verify(opponent, times(3)).addCardToHand(any());
    }

}
