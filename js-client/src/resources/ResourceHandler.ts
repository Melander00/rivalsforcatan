import { CardID } from "../types/card/card"
import actions from "./cards/action/cards.json"
import buildings from "./cards/building/cards.json"
import centers from "./cards/center/cards.json"
import heroes from "./cards/hero/cards.json"
import regions from "./cards/region/cards.json"
import ships from "./cards/ship/cards.json"
import dice from "./event/dice.json"
import dice_events from "./event/dice_event.json"
import events from "./event/event.json"
import player_events from "./event/player.json"
import phases from "./phase/phases.json"
import points from "./point/points.json"
import requests from "./request/requests.json"
import resources from "./resource/resources.json"

const mapper: {
    [key: string]: any[]
} = {
    "action": actions,
    "building": buildings,
    "center": centers,
    "hero": heroes,
    "region": regions,
    "ship": ships,
}

const eventMapper: {
    [key: string]: any[]
} = {
    "dice": dice,
    "event": events,
    "dice_event": dice_events,
    "player": player_events,
}




export function GetCardInfo(id: CardID) {
    const namespace = mapper[id.namespace]
    if(!namespace) return null;
    return namespace.find(e => e.id === id.id)
}


export function GetRequestCauseInfo(cause: string) {
    return requests.find(e => e.cause === cause)
}

export function GetEventInfo(id: CardID) {
    const namespace = eventMapper[id.namespace]
    if(!namespace) return null;
    return namespace.find(e => e.id === id.id)
}

export function GetResourceInfo(id: string) {

    const res = resources.find(e => e.id === id)
    if(res) return res;

    const full = id.replace("Resource", "")
    const first = id[0]

    return {
        id,
        name: full,
        short: first
    }

}

export function GetPointInfo(id: string) {
    const res = points.find(e => e.id === id)
    if(res) return res;

    const full = id.replace("Point", "")
    const first = id[0]

    return {
        id,
        name: full,
        short: first
    }
}


export function GetPhaseInfo(id: string) {
    return phases.find(e => e.id === id)
}