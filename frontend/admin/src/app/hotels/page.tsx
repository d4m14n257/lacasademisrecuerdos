import { ResponseWithData } from "@/model/response";
import { HotelWitFiles } from "@/model/types";

async function getHotels() {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/hotel`);

        if(res.status == 204) {
            return [];
        }

        if(res.status == 200) {
            const hotels : ResponseWithData<[HotelWitFiles]> = await res.json();
            return hotels.data;
        }

    }
    catch (err : unknown ){
        
    }
}

export default async function Hotels () {
    const hotels = await getHotels();

    return (
        <h1>
            Hoteles
        </h1>
    );
}
