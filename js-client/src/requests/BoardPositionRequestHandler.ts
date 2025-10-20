import { setRequestListener } from "../network/Socket"
import { GridPosition } from "../types/board/board"
import { MessageType } from "../types/message"
import { ServerRequest } from "../types/request"
import { ask, print } from "../ui/Console"
import { buildPrincipality } from "../ui/Grid"
import { isNumber } from "../util/Validator"
import { getCauseDescription } from "./Description"

type BoardPositionRequest = {
    positions: GridPosition[][]
}

export function initBoardPositionRequestHandler() {
    setRequestListener(MessageType.REQUEST_BOARD_POSITION, async ({data, cause}: ServerRequest<BoardPositionRequest>) => {

        const causeDetails = getCauseDescription(cause)

        // if(cause.type === "CHOOSE_REGION_TO_INCREASE_RESOURCE" || cause.type === "CHOOSE_REGION_TO_DECREASE_RESOURCE") {
        //     const resourceType = cause.data?.resourceType ?? ""; // containes which resource to change
        //     if(info?.description) {
        //         causeDetails = handleTemplate({resource: resourceType}, info.description)
        //     }
        // }

        const question = `\n${buildPrincipality(data.positions)}\n${causeDetails} | Choose the position as "[Row] [Column]": `

        if(data.positions.length === 0 || data.positions[0].length === 0) return null;

        async function q() {
            const answer = await ask(question)

            // Validation
            const args = answer.split(" ")
            if(args.length !== 2) {
                print("Invalid number of arguments")
                return q();
            }

            const [row, col] = args;
            const rowNr = parseInt(row)
            const colNr = parseInt(col)

            if(
                !isNumber(rowNr, 0, data.positions.length-1) || 
                !isNumber(colNr, 0, data.positions[0].length-1)
            ) {
                print("Invalid row and column numbers.")
                return q();
            }

            return data.positions[rowNr][colNr].uuid
        }

        return await q();
    })
}