export interface Data<T> {
    message?: string;
    data?: T[] | T | [];
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

export type Room = {
    id: string;
    name: string;
    description: string;
    summary: string;
    additional: string;
    single_price: number;
    double_price: number| null;
    created_at: Date;
    status: 'hidden' | 'active';
    files: FilesAdminBytes[]
}

export type FilesAdminBytes = {
    id: string;
    name: string;
    source: string;
    mime: string;
    main: boolean;
    file: string
}

export type Source = {
    id: string;
    source: string;
    main: boolean;
}

export type FileId = {
    id: string;
    file_id: string;
}

type File = {
    id: string;
    name: string;
    source: string;
    mime: string;
    size: number;
    main: boolean;
}