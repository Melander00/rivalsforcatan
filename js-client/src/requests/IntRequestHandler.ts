import { setRequestListener } from "../network/Socket"
import { GetRequestCauseInfo } from "../resources/ResourceHandler"
import { MessageType } from "../types/message"
import { ServerRequest } from "../types/request"
import { ask, print } from "../ui/Console"

type IntRequest = {
    min: number,
    max: number
}

export function initIntRequestHandler() {
    setRequestListener(MessageType.REQUEST_INT, async ({data, cause}: ServerRequest<IntRequest>) => {

        const question = `${GetRequestCauseInfo(cause.type)?.description ?? cause.type} | Enter a number between ${data.min} and ${data.max}: `

        async function q() {
            const answer = await ask(question)
            const nr = parseInt(answer)
            if(isNaN(nr)) {
                print("Invalid number")
                return q();
            }
            
            if(nr < data.min || nr > data.max) {
                print("Number is not in the range")
                return q();
            }
            return nr;
        }
        
        return await q()
    })
}