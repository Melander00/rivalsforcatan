import { setRequestListener } from "../network/Socket";
import { Card } from "../types/card/card";
import { MessageType } from "../types/message";
import { ServerRequest } from "../types/request";
import { buildCardsList } from "../ui/Card";
import { ask, getTerminalProperties, print } from "../ui/Console";
import { getCauseDescription } from "./Description";


type CardRequest = {
    cards: Card[],
}


export default function initCardRequestHandler() {
    setRequestListener(MessageType.REQUEST_CARD, async ({cause, data}: ServerRequest<CardRequest>) => {
        const { cards } = data

        const causeDetails = getCauseDescription(cause)

        const question = `${causeDetails} | Choose a card from:\n${buildCardsList(cards, getTerminalProperties().width)}\nWhich card do you want? `

        async function q() {
            const answer = await ask(question)
            const index = parseInt(answer)
            if(isNaN(index)) {
                print("Invalid index")
                return q();
            }

            if(index < 0 || index >= cards.length) {
                print("Number not in range")
                return q();
            }

            return index;
        }

        const index = await q();
    
        if(cards.length > index) {
            return cards[index].uuid
        }

        return null;
    })
}