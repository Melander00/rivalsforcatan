type State = {
    yourTurn: boolean,
    phase: string
}

export function buildState(state: State): string {
    const turn = state.yourTurn ? "Your" : "Opponent's"
    return `${turn} turn at ${state.phase}`
}