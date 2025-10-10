package samuel.board;

import java.util.List;

/**
 * Tanken är att vi ska kunna abstrahera bort hur grid:et fungerar
 * Vi ska kunna använda oss av
 *  method(row, column)
 *  method(0 -> n, 0 -> n)
 * som om det vore ett vanligt grid med index 0
 *
 * men i själva verket så ska vi ha en center column som är index 0
 * alla till vänster har negativa index
 * alla till höger har positiva index
 * så när vi lägger till en ny kolumn till vänster så får den ett negativt index
 * vilket gör att vi inte måste gå igenom alla grid positions för att reassigna var de ligger.
 */
public class HorizontalExpandableGrid {

    private List<List<GridPosition>> grid;

}
