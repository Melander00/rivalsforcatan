import { setRequestListener } from "../network/Socket"
import { MessageType } from "../types/message"
import { ServerRequest } from "../types/request"
import { ask, print } from "../ui/Console"
import { getCauseDescription } from "./Description"

type BoolRequest = null

export function initBoolRequestHandler() {
    setRequestListener(MessageType.REQUEST_BOOL, async ({cause}: ServerRequest<BoolRequest>) => {

        const causeDetails = getCauseDescription(cause)

        const question = `${causeDetails} | Yes or No: `

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