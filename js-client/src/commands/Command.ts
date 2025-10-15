
import { print } from "../ui/Console"
import { BuildCommand } from "./BuildCommand"
import { EndTurnCommand } from "./EndTurnCommand"
import { PlayCommand } from "./PlayCommand"
import { ResumeCommand } from "./ResumeCommand"
import { RollCommand } from "./RollCommand"
import { TradeCommand } from "./TradeCommand"
import { ViewCommand } from "./ViewCommand"

export interface Command {
    
    getName(): string
    handle(args: string[]): Promise<boolean>
    help(): string

}

class HelpCommand implements Command {
    getName(): string {
        return "help"
    }

    async handle(args: string[]): Promise<boolean> {
        print(getCommandHelps())
        return true;
    }
    
    help(): string {
        return "help"
    }
    
}

const commands: Command[] = [
    // Always
    new HelpCommand(),
    new ViewCommand(),
    new ResumeCommand(),

    // Action
    new RollCommand(),
    new PlayCommand(),
    new BuildCommand(),
    new TradeCommand(),
    new EndTurnCommand(),
]

export async function handleCommands(cmd: string, args: string[]): Promise<boolean> {
    const command = commands.find(e => e.getName().toLowerCase() === cmd.toLowerCase())
    if(!command) {
        print("There is no such command")
        return false;
    }
    const worked = await command.handle(args)

    if(!worked) {
        print(command.help())
        return false
    }

    return true;
}

export function getCommandHelps(): string {
    const sb: string[] = commands.map(e => e.help())
    return `Available Commands:\n\t${sb.join("\n\t")}`
}