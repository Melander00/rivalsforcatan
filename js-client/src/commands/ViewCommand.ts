import { requestData } from "../network/Socket";
import { GridBoard } from "../types/board/board";
import { Hand } from "../types/hand/hand";
import { MessageType } from "../types/message";
import { buildCardsList } from "../ui/Card";
import { print } from "../ui/Console";
import { buildPrincipality } from "../ui/Grid";
import { Command } from "./Command";

export class ViewCommand implements Command {
    private readonly name = "view"

    getName(): string {
        return this.name
    }
    async handle(args: string[]): Promise<boolean> {

        if(args.length < 1) {
            return false;
        }

        const type = args[0]

        if(type === "board") {
            const data = await requestData<GridBoard>(MessageType.REQUEST_BOARD);
            const principality = buildPrincipality(data.boardPositions);
            print(principality);
            return true;
            
        } else if(type === "hand") {
            const data = await requestData<Hand>(MessageType.REQUEST_HAND);
            const hand = buildCardsList(data.cards);
            print(hand);
            return true;
        }

        return false;
    }
    help(): string {
        return "view <board|hand|opponent>"
    }
    
}
