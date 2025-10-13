import { getActionQueue } from "../network/ActionQueue";
import { Action } from "../types/message";
import { print } from "../ui/Console";
import { Command } from "./Command";

export class EndTurnCommand implements Command {

    getName(): string {
        return "end"
    }

    async handle(args: string[]): Promise<boolean> {

        const action = getActionQueue().firstElement()
        if(action === null) {
            print("Action queue is empty")
            return false;
        }

        getActionQueue().removeFirst();

        const data = await action.respond({
            action: Action.END_TURN,
            data: null
        })

        return true;
    }

    help(): string {
        return "end | Ends the turn"
    }
    
}