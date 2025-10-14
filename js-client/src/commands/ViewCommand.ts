import { requestData } from "../network/Socket";
import { GridBoard } from "../types/board/board";
import { Hand } from "../types/hand/hand";
import { MessageType } from "../types/message";
import { Opponent } from "../types/opponent";
import { Point } from "../types/point/point";
import { buildCardsList } from "../ui/Card";
import { getTerminalProperties, print } from "../ui/Console";
import { buildPrincipality } from "../ui/Grid";
import { buildPoints } from "../ui/Point";
import { buildState } from "../ui/State";
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
            const hand = buildCardsList(data.cards, getTerminalProperties().width);
            print(hand);
            return true;
        } else if(type === "points") {
            const data = await requestData<Point[]>(MessageType.REQUEST_POINTS);
            print(`Your points: ${buildPoints(data)}`)
            return true;

        } else if(type === "opponent") {
            const data = await requestData<Opponent>(MessageType.REQUEST_OPPONENT);
            
            const points = buildPoints(data.points)
            const board = buildPrincipality(data.board)

            print(`Opponent Board:\n${board}\nOpponent Points: ${points}`);

            return true;
        } else if(type === "state") {
            const data = await requestData<any>(MessageType.REQUEST_STATE);
            print(buildState(data));
            return true;
        }

        return false;
    }
    help(): string {
        return "view <board|hand|points|state|opponent>"
    }
    
}
