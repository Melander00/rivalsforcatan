import { GetCardInfo } from "../resources/ResourceHandler";
import { Card } from "../types/card/card";
import { handleTemplate } from "./Helper";

export function buildCardsList(cards: Card[]): string {

    return cards.map((e, index) => buildCard(e, index)).join("\n\n")
}

function buildCard(card: Card, index: number) {

    const name = getName(card)
    const type = getType(card)
    const desc = getDescription(card)
    const cost = getCost(card)
    const points = getPoints(card)



    const textRows: string[] = []

    function push(text: string) {
        const arr = text.split("\n")
        arr.forEach(e => textRows.push(e))
    }

    push(name + (type ? ` - ${type}` : ""))
    push("")
    push(desc)
    if(cost) {
        push("")
        push("Costs:")
        for(const resource in cost) {
            push(`${cost[resource]} ${resource}`)
        }
    }
    if(points) {
        push("")
        push("Points:")
        for(const point in points) {
            push(`${points[point]} ${point}`)
        }
    }

    let biggestWidth = 10; // minWidth
    textRows.forEach(e => {
        if(e.length > biggestWidth) biggestWidth = e.length;
    })

    const inner: string[] = textRows.map(e => getRow(e, biggestWidth))

    let outer = "+-"
    const middle = Math.floor(biggestWidth/2)
    for(let i = 0; i < biggestWidth; i++) {
        outer += i === middle ? index : "-"
    }
    outer += "-+"

    return [outer, ...inner, outer].join("\n")
    
    /*
    +--------------+
    | Name         |
    | Description  |
    |              |
    | 1 Timber     |
    | 2 Brick      |
    |              |
    | 1 Commerce   |
    +--------------+
    */


    return `${index} | ${getName(card)} : ${getPoints(card)} : ${getCost(card)} : ${getDescription(card)}`
}


function getRow(text: string, innerWidth: number) {
    const textLength = text.length;
    const toPad = innerWidth - textLength;

    let str = "| "
    str += text
    for(let i = 0; i < toPad; i++) { str += " " }
    str += " |"
    return str;
}




function getName(card: Card) {
    const cardInfo = GetCardInfo(card.cardID)
    if(!cardInfo || !cardInfo.name) return `${card.cardID.namespace}:${card.cardID.id}`
    
    return handleTemplate(card, cardInfo.name, `${card.cardID.namespace}:${card.cardID.id}`)
}

function getType(card: Card) {
    const cardInfo = GetCardInfo(card.cardID)
    if(!cardInfo || !cardInfo.type) return ``

    return cardInfo.type
    
    // return handleTemplate(card, cardInfo.name, `${card.cardID.namespace}:${card.cardID.id}`)
}

function getDescription(card: Card) {
    const cardInfo = GetCardInfo(card.cardID)
    if(!cardInfo || !cardInfo.description) return ``
    
    return handleTemplate(card, cardInfo.description, `${card.cardID.namespace}:${card.cardID.id}`)
}


type CardCost = {
    [resouce: string]: number,
}

function getCost(card: Card): CardCost|null {

    if(!card.cost || !card.cost.length) return null; 
    
    const cost: CardCost = {}

    card.cost.forEach((c: {resourceType: string, amount: number}) => {
        const type = c.resourceType.replace("Resource","")

        if(!cost[type]) cost[type] = 0

        cost[type] += c.amount;
    })

    return cost
}

type Points = {
    [point: string]: number
}

function getPoints(card: Card): Points|null {
    if(!card.points || !card.points.length) return null; 
    
    const points: Points = {}

    card.points.forEach((c: {pointType: string, amount: number}) => {
        const type = c.pointType.replace("Point","")

        if(!points[type]) points[type] = 0

        points[type] += c.amount;
    })

    return points
}