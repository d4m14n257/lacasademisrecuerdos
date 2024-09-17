export interface Data<T> {
    message?: string;
    data?: T[] | [];
    err?: any;
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
    file_name: string;
    file: any;
}