import { setRequestListener } from "../network/Socket"
import { GetRequestCause } from "../resources/ResourceHandler"
import { MessageType } from "../types/message"
import { ServerRequest } from "../types/request"
import { ask, print } from "../ui/Console"

type BoolRequest = null

export function initBoolRequestHandler() {
    setRequestListener(MessageType.REQUEST_BOOL, async ({cause}: ServerRequest<BoolRequest>) => {

        const question = `${GetRequestCause(cause)} | Yes or No: `

        async function q() {
            const answer = await ask(question)

            const isTrue = [
                "y",
                "yes",
                "1",
            ].includes(answer.toLowerCase())

            const isFalse = [
                "n",
                "no",
                "0"
            ].includes(answer.toLowerCase())

            if(isTrue) return true;
            if(isFalse) return false;

            print("Invalid answer")

            return await q();
        }
        
        return await q()
    })
}