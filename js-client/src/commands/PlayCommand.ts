
import { getActionQueue } from "../network/ActionQueue";
import { requestData } from "../network/Socket";
import { Hand } from "../types/hand/hand";
import { Action, MessageType } from "../types/message";
import { print } from "../ui/Console";
import { Command } from "./Command";

export class PlayCommand implements Command {
    getName(): string {
        return "play";
    }

    async handle(args: string[]): Promise<boolean> {

        if(args.length < 1) {
            return false;
        }

        const index = parseInt(args[0])

        if(isNaN(index) || index < 0) {
            return false;
        }

        const action = getActionQueue().firstElement()
        if(action === null) {
            print("Action queue is empty")
            return false;
        }

        const hand = await requestData<Hand>(MessageType.REQUEST_HAND);

        if(index > hand.size-1) {
            return false;
        }

        getActionQueue().removeFirst();

        const card = hand.cards[index]

        const data = await action.respond({
            action: Action.PLAY_CARD,
            data: card.uuid
        })

        if(!data) {
            print("\x1B[0;31m Something went wrong! \x1B[0m")
            return false;
        }

        if(data.success) {
            print("Card has been played")
            return true;
        }

        // print("\x1B[33m[PLAY]\x1B[39m", data)

        print("\x1B[0;31m Couldn't play card: "+data.code+"\x1B[0m")
        // get player cards
        // find the card
        // send message to server as a request
        // return if the server accepts it

        return false;
    }

    help(): string {
        return "play <index in hand>"
    }
}