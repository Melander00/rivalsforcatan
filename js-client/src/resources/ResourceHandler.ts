import { CardID } from "../types/card/card"
import actions from "./action/cards.json"
import buildings from "./building/cards.json"
import regions from "./region/cards.json"
import requests from "./request/requests.json"

const mapper: {
    [key: string]: any[]
} = {
    "region": regions,
    "building": buildings,
    "action": actions
}




export function GetCardInfo(id: CardID) {
    const namespace = mapper[id.namespace]
    if(!namespace) return null;
    return namespace.find(e => e.id === id.id)
}


export function GetRequestCause(cause: string): string {
    return requests.find(e => e.cause === cause)?.description ?? cause
}