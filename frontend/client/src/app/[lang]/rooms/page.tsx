import { getData } from "@/api/getData";
import { HandlerError, HandlerMessageError } from "@/components/handler/HandlerError";
import CardRoom from "@/components/rooms/CardRoom";
import { ROOM_CARD } from "@/constants/endpoints";
import { getDictionary } from "@/language/getDictionary";
import { RoomCard } from "@/model/type";

import "./room.css";
import OtherRoom from "@/components/rooms/OtherRoom";

type Dict = {
    button: string
}

export default async function Rooms ({ params } : { params : { lang: string }}) {
    const dict : Dict = await getDictionary(params.lang, "room");
    const res = await getData<RoomCard>(`${ROOM_CARD}`, params.lang as "es" | "en");

    return (
        <HandlerError>
            <HandlerError.When hasError={Boolean(res.err)}>
                <>
                    <CardRoom 
                        rooms={res.data as RoomCard[]}
                        dict={dict}
                    />
                </>
            </HandlerError.When>
            <HandlerError.Else>
                <HandlerMessageError 
                    message={res.message || res.err}
                    status={res.status}
                />
            </HandlerError.Else>
        </HandlerError>
    );
}