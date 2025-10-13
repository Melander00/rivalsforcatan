import { Command } from "./Command";

export class BuildCommand implements Command {
    private readonly name = "build"


    getName(): string {
        return this.name;
    }

    async handle(args: string[]): Promise<boolean> {

        if(args.length < 1) {
            return false;
        }

        

        // get player cards
        // find the card
        // send message to server as a request
        // return if the server accepts it

        return true;

    }
    

    help(): string {
        return "build <road|settlement|city>"
    }
}