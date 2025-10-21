import { stdin, stdout } from "node:process";
import readline, { createInterface } from "node:readline";
import { Command, getCommands, handleCommands } from "../commands/Command";
import { Color } from "./Color";

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

        // print("")
    })
}

const commands: Command[] = getCommands();

let isPredictionEnabled = false;
let isPredictionOnHold = false;

function handlePrediction(e: any, key: any) {
    if(!isPredictionEnabled) return;
    if(isPredictionOnHold) return;

    if(key.name === "tab") {
        rl.write(null, {name: "backspace"})
    }


    const line = rl.line;
    const cursorPos = rl.cursor;

    const [cmd, arg] = line.split(" ");
    const predictions = commands.filter(c => c.getName().startsWith(cmd));

    if (predictions.length === 0) {
        // redraw without suggestion
        readline.cursorTo(process.stdout, 0);
        readline.clearLine(process.stdout, 0);
        process.stdout.write(`> ${line}`);
        readline.cursorTo(process.stdout, 2 + cursorPos);
        return;
    }

    const command = predictions[0];
    const fullCommand = command.getName();
    
    let suggestion = "";
    
    if (!cmd.startsWith(fullCommand)) {
        // still typing command name
        suggestion = fullCommand.slice(cmd.length);
    } else {
        // either "view" or "view " or "view boa"
        const predictedArgs = command.getArguments().filter(a => a.startsWith(arg ?? "")); // array of argument names
        if(predictedArgs.length === 0) {
            suggestion = "";
        } else {
            const suggestedArg = predictedArgs[0]

            if(arg === undefined || arg === null) {
                suggestion = " " + suggestedArg
            } else {
                suggestion = suggestedArg.slice(arg.length);
            }
        }
    }

    if(key.name === "tab") {
        rl.write(suggestion)
        return;
    }

    // redraw line with dim suggestion
    readline.cursorTo(process.stdout, 0);
    readline.clearLine(process.stdout, 0);
    process.stdout.write(`> ${line}${Color.gray(suggestion)}`);
    readline.cursorTo(process.stdout, 2 + cursorPos);
}

export function initCommandPrediction() {
    if (!isTTY) return false;

    process.stdin.on("keypress", handlePrediction);

    isPredictionOnHold = false;
    isPredictionEnabled = true;

    return true;
}

function resumeCommandPrediction() {
    isPredictionOnHold = false;
}

function holdCommandPrediction() {
    isPredictionOnHold = true;
}

export function stopCommandPrediction() {
    isPredictionEnabled = false;
}

export function restartCommandPrediction() {
    isPredictionEnabled = true;
}


// export function initCommandPrediction() {
//     if (!isTTY) return false;

//     const commands: Command[] = getCommands();

//     process.stdin.on("keypress", (_, key) => {
//         const line = rl.line;
//         const cursorPos = rl.cursor;

//         const [cmd, ...argsTyped] = line.split(" ");
//         const predictions = commands.filter(c => c.getName().startsWith(cmd));

//         if (predictions.length === 0) {
//             // no command match, redraw line without suggestion
//             readline.cursorTo(process.stdout, 0);
//             readline.clearLine(process.stdout, 0);
//             process.stdout.write(`> ${line}`);
//             readline.cursorTo(process.stdout, 2 + cursorPos);
//             return;
//         }

//         const command = predictions[0];
//         const fullCommand = command.getName();
//         let suggestion = "";

//         // Suggest command name if not fully typed
//         if (!cmd.startsWith(fullCommand)) {
//             suggestion = fullCommand.slice(cmd.length);
//         } else {
//             // Command name complete, predict next argument
//             const argIndex = argsTyped.length - 1;
//             const currentArgTyped = argsTyped[argIndex] ?? "";

//             const predictedArgs = command.getArguments().filter(a => a.startsWith(currentArgTyped));
//             if (predictedArgs.length > 0) {
//                 const suggestedArg = predictedArgs[0];
//                 suggestion = currentArgTyped ? suggestedArg.slice(currentArgTyped.length) : " " + suggestedArg;
//             }
//         }

//         // Accept suggestion with Tab
//         if (key && key.name === "tab" && suggestion.length > 0) {
//             rl.write(suggestion); // inserts suggestion into the readline buffer
//             return; // skip manual redraw
//         }

//         // redraw line with dim suggestion
//         readline.cursorTo(process.stdout, 0);
//         readline.clearLine(process.stdout, 0);
//         process.stdout.write(`> ${line}${Color.gray(suggestion)}`);
//         readline.cursorTo(process.stdout, 2 + cursorPos);
//     });

//     return true;
// }






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
    
    if(resetInput) {
        holdCommandPrediction();
        rl.write(null, {ctrl: true, name: "u"})
    }

    let unlocked = false;
    return () => {
        if(!unlocked) {
            unlocked = true;
            rl.prompt(true)

            if(resetInput) {
                resumeCommandPrediction()
            }

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