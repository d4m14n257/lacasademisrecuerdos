type Message = {
    message : string
}

export interface ResponseOnlyMessage extends Message {
    message: string
} 

export interface ResponseWithInfo extends Message {
    message: string,
    info: string
}

export interface ResponseWithData<T> extends Message {
    data : T | T[]
}