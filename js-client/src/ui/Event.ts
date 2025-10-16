import { addMessageListener } from "../network/Socket"
import { GetEventInfo } from "../resources/ResourceHandler"
import { CardID } from "../types/card/card"
import { MessageType } from "../types/message"
import { print } from "./Console"
import { handleTemplate } from "./Template"

type ServerEvent = {
    id: CardID
}

export function initEventListener() {
    addMessageListener(MessageType.EVENT, (event) => {
        handler(event)
    })
}


function handler(event: ServerEvent) {
    const eventInfo = GetEventInfo(event.id)
    if(!eventInfo) {
        print(event)
        return;
    }

    print(
        handleTemplate(event, eventInfo.description, `Event: ${event.id.namespace}:${event.id.id}`)
    )
}