import { GetPhaseInfo } from "../resources/ResourceHandler";
import { State } from "../types/state";
import { handleTemplate } from "./Helper";



export function buildState(state: State): string {
    const turn = state.yourTurn ? "Your" : "Opponent's"

    const info = GetPhaseInfo(state.phase);
    if(info?.description == null) return `${turn} turn at ${state.phase}`

    return handleTemplate({player: turn}, info.description)
}