import { randomUUID } from "crypto";
import net from "net";
import { Message, MessageType } from "../types/message";
import { Color } from "../ui/Color";
import { debug, print } from "../ui/Console";

const client = new net.Socket();

export async function connect(address: string, port: number) {
    print(`Trying to connect to ${address}:${port}`)

    return new Promise<void>((resolve) => {
        client.connect(port, address, () => {
            print(Color.green("Successfully connected!"))
            resolve()
        })
    })
}


export type Listener<T> = (data: T, requestId?: string) => void
export type AsyncListener<T> = (data: T, requestId?: string) => Promise<unknown>

const listeners: {
    [type in MessageType]?: Listener<any>[]
} = {}

const requestListeners: {
    [type in MessageType]?: AsyncListener<any>
} = {}

const responseListeners: {
    [id: string]: Listener<any>
} = {}

export function addMessageListener(type: MessageType, listener: Listener<any>) {
    if(!listeners[type]) listeners[type] = [listener]
    else listeners[type].push(listener)
}

export function setRequestListener(type: MessageType, listener: AsyncListener<any>) {
    requestListeners[type] = listener;
}

export async function requestData<T>(type: MessageType) {
    return new Promise<T>((resolve) => {
        const requestId = randomUUID();
        responseListeners[requestId] = (data: T) => { resolve(data) }
        
        sendMessage({
            type,
            requestId,
            data: null
        })
    })
}

export function setIdListener(requestId: string, listener: Listener<any>) {
    responseListeners[requestId] = listener;
}

export function sendMessage(msg: Message) {
    debug("SENDING", msg)
    client.write(JSON.stringify(msg) + "\n")
}


let c = 0;
async function handleMessage(msg: Message) {

    debug(++c, msg)
    
    // First handle if it is a server request
    handleRequestListeners(msg)

    // Then handle all the id:listeners
    handleResponseListeners(msg)
    
    // Lastly handle rest of listeners
    handleListeners(msg)
}

async function handleRequestListeners(msg: Message) {
    const rqListener = requestListeners[msg.type]
    if(rqListener) {
        const data = await rqListener(msg.data, msg.requestId)
        sendMessage({
            type: MessageType.RESPONSE,
            requestId: msg.requestId,
            data: data
        })
    }
}

function handleResponseListeners(msg: Message) {
    const idListener = responseListeners[msg.requestId]
    if(idListener) {
        idListener(msg.data, msg.requestId)
        delete responseListeners[msg.requestId] // Stop memory-leak
    }
}

function handleListeners(msg: Message) {
    const list = listeners[msg.type]
    if(list) {
        list.forEach(l => l(msg.data, msg.requestId))
    }
}



// client.on("data", buf => {
//     const data = buf.toString();

//     const parts = data.split("\n").filter(e => e);

//     for(const part of parts) {
//         const json: Message = JSON.parse(part)

//         handleMessage(json)
//     }
// })


let buffer = "";

client.on("data", buf => {
    buffer += buf.toString();

    let newlineIndex;
    while ((newlineIndex = buffer.indexOf("\n")) >= 0) {
        const line = buffer.slice(0, newlineIndex);
        buffer = buffer.slice(newlineIndex + 1);
        if (line.trim().length === 0) continue;

        try {
            const msg: Message = JSON.parse(line);
            handleMessage(msg);
        } catch (err) {
            console.error("Invalid JSON chunk:", line, err);
        }
    }
});

client.on("error", (err) => {
    print(Color.red(err.message))
})

client.on("close", (hadError) => {
    print(hadError ? 
        Color.red(`An error caused the socket to close. Exiting...`) : 
        Color.blue(`The socket was closd. Exiting...`)
    )
    process.exit()
})