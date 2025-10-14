import { GetPhaseInfo } from "../resources/ResourceHandler";
import { handleTemplate } from "./Helper";

type State = {
    yourTurn: boolean,
    phase: string
}

export function buildState(state: State): string {
    const turn = state.yourTurn ? "Your" : "Opponent's"

    const info = GetPhaseInfo(state.phase);
    if(info?.description == null) return `${turn} turn at ${state.phase}`

    return handleTemplate({player: turn}, info.description)
}