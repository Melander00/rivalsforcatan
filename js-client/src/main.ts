import "dotenv/config";
import { displayBoard } from "./display";
import { connect, listenToMessage, requestData } from "./network";
import { GridBoard } from "./types/board/board";
import { MessageType } from "./types/network/message";

const PORT = parseInt(process.env["HOST_PORT"] || "5000")
const ADDRESS = process.env["HOST_ADDRESS"] || "localhost"

connect(ADDRESS, PORT);

listenToMessage(MessageType.GENERIC, data => {
    if(data.boardPositions) {
        displayBoard(data)
    } else {
        console.log(JSON.stringify(data, null, 2))
    }
})

setTimeout(async () => {
    console.log("Requesting data")
    const data = await requestData<GridBoard>(MessageType.REQUEST_BOARD)
    displayBoard(data)
}, 2000)