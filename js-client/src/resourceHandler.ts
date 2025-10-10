import regions from "./resources/region/cards.json"
import { CardID } from "./types/card/card"

const mapper: {
    [key: string]: any[]
} = {
    "region": regions
}

export function getCardInfo(id: CardID) {
    const namespace = mapper[id.namespace]
    if(!namespace) return {name: `${id.namespace}:${id.id}`};
    return namespace.find(e => e.id === id.id)
}