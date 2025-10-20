import { setRequestListener } from "../network/Socket"
import { GetResourceInfo } from "../resources/ResourceHandler"
import { MessageType } from "../types/message"
import { ServerRequest } from "../types/request"
import { ask, print } from "../ui/Console"
import { handleTemplate } from "../ui/Template"
import { getCauseDescription } from "./Description"

type ResourceAmount = {
    resourceType: string,
    amount: number
}

type ResourceBundle = ResourceAmount[]

type ResourceRequest = {
    amount: number
    resources: ResourceBundle
}

export function initResourceRequestHandler() {
    setRequestListener(MessageType.REQUEST_RESOURCE, async ({data, cause}: ServerRequest<ResourceRequest>): Promise<ResourceBundle> => {

        const causeDetails = getCauseDescription(cause)

        let arr: ResourceBundle = []

        function getResources() {
            const available: ResourceAmount[] = []

            for(let i = 0; i < data.resources.length; i++) {
                const resAmount = data.resources[i]
                let am = resAmount.amount;
                const curr = arr.find(e => e.resourceType === resAmount.resourceType)
                if(curr) am -= curr.amount;
                if(am > 0) available.push({resourceType: resAmount.resourceType, amount: am})
            }

            return `${available.sort((a, b) => a.resourceType.localeCompare(b.resourceType)).map(e => `[${handleTemplate({}, GetResourceInfo(e.resourceType).name)}]`).join(" ")}`
        }
        
        async function q(): Promise<ResourceAmount> {
            const question = `${causeDetails} | Choose one of the available ${getResources()}: `

            const answer = await ask(question)

            const resources = data.resources.filter(e => e.resourceType.toLowerCase().startsWith(answer.toLowerCase()))

            if(resources.length === 0) {
                print("No resource found")
                return q();
            }

            if(resources.length > 1) {
                print("Too many resource found.")
                return q();
            }

            return {
                resourceType: resources[0].resourceType,
                amount: 1,
            }
        }

        
        for(let i = 0; i < data.amount; i++) {
            const resAmount = await q()
            const curr = arr.find(e => e.resourceType === resAmount.resourceType)
            if(curr) curr.amount += resAmount.amount;
            else arr.push(resAmount)
        }

        return arr
    })
}