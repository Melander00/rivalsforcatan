export type ServerRequest<T> = {
    cause: RequestCause,
    data: T
}

export type RequestCause = {
    type: string,
    data: any
}