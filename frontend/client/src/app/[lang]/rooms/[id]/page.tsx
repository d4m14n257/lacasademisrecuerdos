import { getData } from "@/api/getData";
import { HandlerError, HandlerMessageError } from "@/components/handler/HandlerError";
import { ROOM_CARD } from "@/constants/endpoints";
import { getDictionary } from "@/language/getDictionary";
import { RoomCard, Room as RoomType } from "@/model/type";
import OtherRoom from "@/components/rooms/OtherRoom";
import Room from "@/components/rooms/Room";

import './roomId.css';

type Dict = {
    price: {
        single: string;
        double: string;
    };
    form: {
        title: string;
        labels: {
            name: string;
            email: string;
            check_in: string;
            check_out: string;
            guests: {
                label: string;
                select: string[];
            };
            comment: string;
            submit: string;
        };
        errors: {
            name: string;
            email: string;
            invalidEmail: string;
        };
    };
    others: string;
}

type Button = {
    button: string;
}

export default async function RoomId ({ params } : { params: { id: string, lang: string }}) {
    const res =  await getData<RoomType>(`${ROOM_CARD}/${params.id}`, params.lang as "es" | "en");
    const rooms = await getData<RoomCard>(`${ROOM_CARD}`, params.lang as "es" | "en");

    
    const dict : Dict = await getDictionary(params.lang, "roomDetails");
    const dictButton : Button = await getDictionary(params.lang, "room");

    return (
        <HandlerError>
            <HandlerError.When hasError={Boolean(res.err)}>
                <>
                    <Room
                        room={res.data as RoomType}
                        dict={dict}
                    />
                    <OtherRoom 
                        id={params.id}
                        rooms={rooms.data as RoomCard[]}
                        dict={dictButton}
                        others={dict.others}
                        lang={params.lang}
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