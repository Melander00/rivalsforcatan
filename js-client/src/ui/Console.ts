import { stdin, stdout } from "node:process";
import readline, { createInterface } from "node:readline";
import { handleCommands } from "../commands/Command";

const rl = createInterface({input: stdin, output: stdout, prompt: "> "});

const isTTY = process.stdin.isTTY === true

export function getTerminalProperties() {
    return {
        width: process.stdout.columns,
        height: process.stdout.rows,
        isTTY: isTTY,
    }
}

export async function listenToCommands() {   
    rl.prompt();

    rl.on("line", async line => {
        const input = line.trim();

        const [cmd, ...args] = input.split(" ")

        print("")

        await handleCommands(cmd, args)

        print("")
    })

}

export function print(message?: any, ...optionalParams: any[]) {
    const unlock = lock()
    // await ( () => {return new Promise<void>((resolve) => setTimeout(() => resolve(), 1))} )()
    console.log(message, ...optionalParams)
    unlock()
}

export function debug(message?: any, ...optionalParams: any[]) {
    if(/^(true|1|yes|on)$/i.test(process.env['DEBUG'] ?? "")) print(message, ...optionalParams)
}

let holder: (() => void) | null = null;
export function resumeAsk(): boolean {
    if(holder) {
        holder();
        return true;
    }
    return false;
}

function lock(resetInput = false) {
    const saved = rl.line;
    
    readline.cursorTo(process.stdout, 0);
    readline.clearLine(process.stdout, 0);
    
    if(resetInput)
        rl.write(null, {ctrl: true, name: "u"})
    
    let unlocked = false;
    return () => {
        if(!unlocked) {
            unlocked = true;
            rl.prompt(true)

            if(saved && resetInput) {
                rl.write(saved)
                // process.stdout.write("");
            }
        }
    }
}

export function ask(question: string) {

    let unlock = lock(true)

    return new Promise<string>(async (resolve) => {

        async function askLoop() {

            const answer = await askQuestion(question)

            if(answer === "hold") {
                unlock()
                holder = () => {
                    unlock = lock(true)
                    holder = null;
                    askLoop()
                }
                return;
            }

            unlock()
            resolve(answer)
        }

        askLoop()
    })
}

function askQuestion(q: string) {
    return new Promise<string>((resolve) => {
        rl.question(q, answer => resolve(answer.trim()));
    })
}