import { CardStack, CardStackResponse } from "../types/card/card";
import { buildCard } from "./Card";


const mapper: {
    [key: string]: {name: string, show: boolean}
} = {
    "roadStack": { name: "Roads", show: true },
    "settlementStack": { name: "Settlements", show: true },
    "regionStack": { name: "Regions", show: false },
    "cityStack": { name: "Cities", show: true },
    "eventStack": { name: "Events", show: false },
    "basicStacks": { name: "Stack", show: false },
    "themeStacks": { name: "Theme Stack", show: false },
}



export function buildStacksFromResponse(response: CardStackResponse): string {

    let stacks: string[] = []

    for(const key in response) {
        const stack = response[key]

        const info = mapper[key]
        if(!info) return `[${key}]`

        if(Array.isArray(stack)) {
            let sb: string[] = []
            stack.forEach((e, i) => sb.push(buildStack(e, info.name + " " + i, info.show)))
            stacks.push(sb.join(info.show ? "\n" : " "))
            continue;
        }

        stacks.push(buildStack(stack, info.name, info.show))
    }

    return stacks.join("\n\n")
}



export function buildStack(stack: CardStack, name: string, showFirst = false): string {
    if(showFirst) {
        if(stack.cards.length > 0) {
            const card = stack.cards[0]

            const cardPrint = buildCard(card, 0);
            return `[${name} | ${stack.size} left]\n${cardPrint.rows.join("\n")}`;
        }
    }

    return `[${name} | ${stack.size} left]`
}