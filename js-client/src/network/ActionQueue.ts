import { ActionMessage, ActionResponse, MessageType } from "../types/message";
import { print } from "../ui/Console";
import { buildState } from "../ui/State";
import { setIdListener, setRequestListener } from "./Socket";

type ActionQueueItem = {
    phase: string,
    respond: (data: ActionMessage) => Promise<ActionResponse|null>
}

export class ActionQueue {
    private queue: ActionQueueItem[];

    constructor() {
        this.queue = []
    }

    init() {
        setRequestListener(MessageType.REQUEST_ACTION, (phase, requestId) => {
            return new Promise<any>((respond) => {
                const listener = async (toRespondWith: ActionMessage) => {
                    return new Promise<ActionResponse|null>((actionRespond) => {
                        if(!requestId) {
                            actionRespond(null)
                        } else {   
                            setIdListener(requestId, (response: ActionResponse) => {
                                actionRespond(response)
                            })
                        }
                        respond(toRespondWith)
                    })
                }

                print(buildState({yourTurn: true, phase}))
            
                this.queue.push({phase, respond: listener})
            })
        })
    }

    isEmpty() {
        return this.queue.length === 0
    }

    firstElement() {
        if(this.isEmpty()) return null;
        return this.queue[0]
    }

    removeFirst() {
        this.queue.shift()
    }
}


const actionQueue = new ActionQueue();
export function initActionQueue() {
    actionQueue.init()
}

export function getActionQueue(): ActionQueue {
    return actionQueue
}