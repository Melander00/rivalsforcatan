import { print, resumeAsk } from "../ui/Console";
import { Command } from "./Command";

export class ResumeCommand implements Command {

    getName(): string {
        return "resume"
    }

    async handle(args: string[]): Promise<boolean> {

        const success = resumeAsk() 
        if(!success) {
            print("There is no question on hold at the moment.")
        }

        return true;
    }

    help(): string {
        return "resume | Whenever there is a question, type 'hold' to hold it until this command is issued."
    }
    
}