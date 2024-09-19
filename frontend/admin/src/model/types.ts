export interface Data<T> {
    message?: string;
    data?: T[] | [];
    err?: any;
    errors?: { [key : string] : string }
    status: number 
}

export type HotelWitFiles = {
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
    filesBytes: any;
};

export type RoomCard = {
    id: string;
    name: string;
    summary: string;
    additional: string;
    source: string;
    status: 'hidden' | 'active'
    file_name: string;
    file: string;
}

export type RoomStatus = {
    id: string;
    status: 'hidden' | 'active'
}