export type ServerRequest<T> = {
    cause: {
        type: string,
        data: unknown
    },
    data: T
}