import { getActionQueue } from "../network/ActionQueue";
import { Action } from "../types/message";
import { debug, print } from "../ui/Console";
import { Command } from "./Command";

export class RollCommand implements Command {

    getName(): string {
        return "roll"
    }

    async handle(args: string[]): Promise<boolean> {

        const action = getActionQueue().firstElement()
        if(action === null) {
            print("Action queue is empty")
            return false;
        }

        getActionQueue().removeFirst();

        const data = await action.respond({
            action: Action.ROLL_DICE,
            data: null
        })

        debug(data)

        return true;
    }

    help(): string {
        return "roll | Roll the dice"
    }
    
}