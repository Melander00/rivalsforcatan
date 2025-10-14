import "dotenv/config";
import { initActionQueue } from "./network/ActionQueue";
import { addMessageListener, connect } from "./network/Socket";
import { initBoardPositionRequestHandler } from "./requests/BoardPositionRequestHandler";
import { initBoolRequestHandler } from "./requests/BoolRequestHandler";
import initCardRequestHandler from "./requests/CardRequestHandler";
import initCardStackRequestHandler from "./requests/CardStackRequestHandler";
import { initIntRequestHandler } from "./requests/IntRequestHandler";
import { initResourceRequestHandler } from "./requests/ResourceRequestHandler";
import { MessageType } from "./types/message";
import { listenToCommands, print } from "./ui/Console";
import { initEventListener } from "./ui/Event";

const PORT = parseInt(process.env["HOST_PORT"] || "5000")
const ADDRESS = process.env["HOST_ADDRESS"] || "localhost"

connect(ADDRESS, PORT);
listenToCommands()
initActionQueue()

// -- Init Server Request Handlers --
initIntRequestHandler()
initBoolRequestHandler()
initCardRequestHandler()
initResourceRequestHandler()
initCardStackRequestHandler()
initBoardPositionRequestHandler()

// -- Init Event Printer Listener --
initEventListener()

addMessageListener(MessageType.GENERIC, data => {
    print(JSON.stringify(data, null, 2))
})

addMessageListener(MessageType.DIRECT_MESSAGE, dm => {
    print(`[${dm.sender}]: ${dm.message}`)
})


