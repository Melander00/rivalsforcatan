
import { randomUUID } from "crypto";
import { Socket } from "net";
import { Message, MessageType } from "./types/network/message";

const client = new Socket();

export async function connect(address: string, port: number) {
    return new Promise<void>((resolve) => {
        client.connect(port, address, () => {
            resolve()
        })
    })
}

export function getClient() {
    return client;
}

type Listener = (data: any) => void

const listeners: {
    [messageType: string]: Listener[]
} = {}

const requestListeners: {
    [id: string]: Listener
} = {}

export function listenToMessage(messageType: MessageType, listener: Listener) {
    if(!listeners[messageType]) {
        listeners[messageType] = []
    }

    listeners[messageType].push(listener)
}

export async function requestData<T>(type: MessageType) {
    return new Promise<T>((resolve) => {
        const requestId = randomUUID();
        requestListeners[requestId] = (data) => { resolve(data) }
        sendMessage({
            type,
            requestId,
            data: null
        })
    })

}

function sendMessage(msg: Message) {
    console.log("Sending Message")
    client.write(JSON.stringify(msg) + "\n")
}

let c = 0;

client.on("data", buf => {

    console.log("Received data")

    const data = buf.toString();

    const parts = data.split("\n").filter(e => e);

    for(const part of parts) {
        // console.log(++c, part)

        const json: Message = JSON.parse(part)

        const funcs = listeners[json.type]
        if(funcs) {
            funcs.forEach(list => list(json.data))
        }

        const idListeners = requestListeners[json.requestId]
        if(idListeners) {
            idListeners(json.data)
        }
    }


})

