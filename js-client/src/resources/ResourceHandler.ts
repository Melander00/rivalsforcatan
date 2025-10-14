import { CardID } from "../types/card/card"
import actions from "./action/cards.json"
import buildings from "./building/cards.json"
import dice from "./event/dice.json"
import dice_events from "./event/dice_event.json"
import events from "./event/event.json"
import regions from "./region/cards.json"
import requests from "./request/requests.json"

const mapper: {
    [key: string]: any[]
} = {
    "region": regions,
    "building": buildings,
    "action": actions
}

const eventMapper: {
    [key: string]: any[]
} = {
    "dice": dice,
    "event": events,
    "dice_event": dice_events,
}




export function GetCardInfo(id: CardID) {
    const namespace = mapper[id.namespace]
    if(!namespace) return null;
    return namespace.find(e => e.id === id.id)
}


export function GetRequestCause(cause: string): string {
    return requests.find(e => e.cause === cause)?.description ?? cause
}

export function GetEventInfo(id: CardID) {
    const namespace = eventMapper[id.namespace]
    if(!namespace) return null;
    return namespace.find(e => e.id === id.id)
}

export function GetResourceInfo(name: string) {

    const full = name.replace("Resource", "")
    const first = name[0]

    return {
        name: full,
        short: first
    }

}