import { getActionQueue } from "../network/ActionQueue";
import { Action } from "../types/message";
import { print } from "../ui/Console";
import { Command } from "./Command";

export class TradeCommand implements Command {
    private readonly name = "trade"


    getName(): string {
        return this.name;
    }

    getArguments(): string[] {
        return []
    }

    async handle(args: string[]): Promise<boolean> {

        const action = getActionQueue().firstElement()

        if(action === null) {
            print("Action queue is empty")
            return false;
        }
        
        getActionQueue().removeFirst();

        const data = await action.respond({
            action: Action.TRADE,
            data: null
        })

        if(!data) {
            print("\x1B[0;31m Something went wrong! \x1B[0m")
            return false;
        }

        if(data.success) {
            print("Trade complete")
            return true;
        }
        
        print("\x1B[0;31m Trade went wrong: "+data.code+"\x1B[0m")

        return true;
    }
    

    help(): string {
        return "trade | Initiates the trade interface"
    }
}