import { setRequestListener } from "../network/Socket";
import { GetRequestCauseInfo } from "../resources/ResourceHandler";
import { CardStack } from "../types/card/card";
import { MessageType } from "../types/message";
import { ServerRequest } from "../types/request";
import { ask, print } from "../ui/Console";


type CardStackRequest = {
    cardStacks: CardStack[],
    forbiddenStackIds: string[]
}



export default function initCardStackRequestHandler() {
    setRequestListener(MessageType.REQUEST_CARD_STACK, async ({cause, data}: ServerRequest<CardStackRequest>) => {
        const { cardStacks, forbiddenStackIds } = data

        const sb: string[] = []
        for(let i = 0; i < cardStacks.length; i++) {
            if(
                forbiddenStackIds.includes(cardStacks[i].uuid) ||
                cardStacks[i].size === 0
            ) {
                continue;
            }

            sb.push(`[Stack ${i}]`)
        }

        const question = `${GetRequestCauseInfo(cause.type)?.description ?? cause.type} | Choose a card stack from: ${sb.join(" ")}\nWhich card stack do you want? `

        async function q() {
            const answer = await ask(question)
            const index = parseInt(answer)
            if(isNaN(index)) {
                print("Invalid index")
                return q();
            }

            if(index < 0 || index >= cardStacks.length) {
                print("Number not in range")
                return q();
            }

            return index;
        }

        const index = await q();
    
        if(cardStacks.length > index) {
            return cardStacks[index].uuid
        }

        return null;
    })
}