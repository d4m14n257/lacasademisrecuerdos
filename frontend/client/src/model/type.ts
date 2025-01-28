type Message = {
    message : string
}

export type RoomList = {
    id: string;
    name: string;
}

export type Hotel = {
    id: string;
    hotel_name: string;
    street_name: string;
    neighborhood: string;
    street_number: string;
    postal_code: string;
    phone_number: string;
    email: string;
    latitude: number;
    longitude: number;
    url: string;
    file: string;
}

export type Room = {
    id: string;
    name: string;
    description: string;
    summary: string;
    single_price: number;
    double_price: number| null;
    files: {
        name: string;
        main: boolean;
        file: string;
    }[]
}

export interface RoomCard extends RoomList{
    summary: string;
    file_name: string;
    file: string;
}

export interface Data<T> {
    message?: string;
    data?: T[] | T ;
    err?: any;
    errors?: { [key : string] : string }
    status: number 
}

export interface ResponseWithData<T> extends Message {
    data: T | T[]
}

export interface ResponseWithInfo extends Message {
    message: string,
    info: string
}

export interface ResponseOnlyMessage extends Message {
    message: string
}

export interface ResponseErrorLabel extends ResponseWithInfo {
    errors?: { [key : string] : string }
}