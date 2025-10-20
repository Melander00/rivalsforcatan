import { GetCardInfo, GetResourceInfo } from "../resources/ResourceHandler";
import { Card } from "../types/card/card";
import { handleTemplate } from "./Template";
import { CalculateWidth } from "./Text";

type CardPrint = {
    width: number;
    rows: string[];
}

export function buildCardsList(cards: Card[], maxWidthPerRow = Number.MAX_VALUE): string {


    const cardsList = cards.map((e, index) => buildCard(e, index))

    if(cardsList.length === 0) return "Empty list"

    // calculate how many cards we can fit on one row
    const cardsPerLine: CardPrint[][] = []
    let allowedCards = 0;
    let currentWidth = 0;
    let currentLine: CardPrint[] = []
    for(let i = 0; i < cardsList.length; i++) {
        const card = cardsList[i]
        let nextWidth = currentWidth + card.width + currentLine.length; // <-- Would this card cause the row to overflow?

        if(nextWidth > maxWidthPerRow && allowedCards > 0) {
            cardsPerLine.push(currentLine)
            currentLine = []
            allowedCards = 0;
            currentWidth = 0;
        }
        
        currentLine.push(card)
        allowedCards++;
        currentWidth += card.width;
    }
    cardsPerLine.push(currentLine)


    return cardsPerLine.map(e => buildRow(e)).join("\n")
    

    // return cards.map((e, index) => buildCard(e, index)).join("\n\n")
}

function buildRow(cards: CardPrint[]) {
    let height = 0;
    cards.forEach(e => height = Math.max(height, e.rows.length))

    /*
    +-------0------+ +-------1------+
    | Name         | | Name         |
    | Description  | | Description  |
    |              | |              |
    | 1 Timber     | | 1 Timber     |
    | 2 Brick      | | 2 Brick      |
    |              | |              |
    | 1 Commerce   | | 1 Commerce   |
    +-------0------+ +-------1------+
    */

    const rows: string[] = []
    for(let r = 0; r < height; r++) {
        const row: string[] = []
        cards.forEach((card) => {
            if(r > card.rows.length-1) {
                // that means there are no more rows for this card
                // push only spaces equal to the width
                row.push((new Array(card.width).fill(0).map(e => " ")).join(""))
            } else {
                row.push(card.rows[r])
            }

        })
        rows.push(row.join(" "))
    }
    return rows.join("\n")
}


export function buildCard(card: Card, index: number): CardPrint {

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
        const length = CalculateWidth(e)
        if(length > biggestWidth) biggestWidth = length;
    })

    // textRows.push("S: " + biggestWidth)

    const inner: string[] = textRows.map(e => getRow(e, biggestWidth))

    let outer = "+-"
    const middle = Math.floor(biggestWidth/2)
    const less = (""+index).length-1;
    for(let i = 0; i < biggestWidth-less; i++) {
        outer += i === middle ? index : "-"
    }
    outer += "-+"

    return {
        width: biggestWidth + 4,
        rows: [outer, ...inner, outer]
    }
    
    


    // return `${index} | ${getName(card)} : ${getPoints(card)} : ${getCost(card)} : ${getDescription(card)}`
}


function getRow(text: string, innerWidth: number) {
    const textLength = CalculateWidth(text);
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
        const type = GetResourceInfo(c.resourceType).name

        if(!cost[type]) cost[type] = 0

        cost[type] += c.amount;
    })

    return cost
}

type Points = {
    [point: string]: number
}

function getPoints(card: Card): Points|null {
    if(!card.points || card.points.length) return null; 
    
    const points: Points = {}

    card.points.forEach((c: {pointType: string, amount: number}) => {
        const type = c.pointType.replace("Point","")

        if(!points[type]) points[type] = 0

        points[type] += c.amount;
    })

    return points
}