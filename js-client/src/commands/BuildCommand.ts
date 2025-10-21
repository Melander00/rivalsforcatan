import { getActionQueue } from "../network/ActionQueue";
import { Action } from "../types/message";
import { print } from "../ui/Console";
import { Command } from "./Command";

const CENTER_CARDS = [
    "road",
    "settlement",
    "city",
]

export class BuildCommand implements Command {
    private readonly name = "build"
    private readonly args: string[] = [
        "road","settlement","city"
    ]


    getName(): string {
        return this.name;
    }

    getArguments(): string[] {
        return this.args
    }

    async handle(args: string[]): Promise<boolean> {

        if(args.length < 1) {
            return false;
        }

        const cardName = args[0]

        if(!CENTER_CARDS.includes(cardName)) return false;

        const action = getActionQueue().firstElement()
        if(action === null) {
            print("Action queue is empty")
            return false;
        }
        
        getActionQueue().removeFirst();

        const data = await action.respond({
            action: Action.BUILD,
            data: cardName
        })

        if(!data) {
            print("\x1B[0;31m Something went wrong! \x1B[0m")
            return false;
        }

        if(data.success) {
            print("Build complete")
            return true;
        }
        
        print("\x1B[0;31m Couldn't build: "+data.code+"\x1B[0m")

        return true;

    }
    

    help(): string {
        return "build <road|settlement|city>"
    }
}