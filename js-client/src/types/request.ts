export type ServerRequest<T> = {
    cause: {
        type: string,
        data: any
    },
    data: T
}