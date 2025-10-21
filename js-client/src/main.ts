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
import { Color } from "./ui/Color";
import { initCommandPrediction, listenToCommands, print } from "./ui/Console";
import { initEventListener } from "./ui/Event";


const PORT = parseInt(process.env["HOST_PORT"] || "5000")
const ADDRESS = process.env["HOST_ADDRESS"] || "localhost"


// console.log(handleTemplate({resourceType: "LumberResource"}, {
//     template: "You lost $red{$resource{resourceType}} $green{'literal'} from the Brigand Attack."
// }))

// process.exit()

connect(ADDRESS, PORT);
listenToCommands()
initActionQueue()
if(/^(true|1|yes|on)$/i.test(process.env['PREDICTION'] ?? "")) {
    if(initCommandPrediction()) print(Color.blue("Enabled command prediction."))
    else print(Color.red("Couldnt enable command prediction. Maybe not TTY?"))
}
// stopCommandPrediction()

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


