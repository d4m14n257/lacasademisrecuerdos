"use client"

import { RoomCard } from "@/model/type";
import { useRouter } from "next/navigation";
import { useCallback } from "react";

type Dict = {
    button: string
}

type Props = {
    id: string;
    rooms: RoomCard[];
    dict: Dict;
    lang: string;
    others: string;
}

export default function OtherRoom (props : Props) {
    const { id, rooms, dict, lang, others } = props;
    const router = useRouter();

    const handleRouterRoom = useCallback((id: string) => {
        router.push(`/${lang}/rooms/${id}`);
    }, [])

    return (
        <>
            <h2 className="others-rooms">{others}</h2>
            <div className="card-container">
                {rooms.map((room) => (
                    room.id != id &&
                        <div key={room.id} className="card-room">
                            <img 
                                src={`data:image/webp;base64,${room.file}`}
                                alt={room.name}
                            />
                            <div className="card-info">
                                <h3>{room.name}</h3>
                                <p>{room.summary}</p>
                                <button
                                    onClick={() => handleRouterRoom(room.id)}
                                >{dict.button}</button>
                            </div>
                        </div>
                ))}
            </div>
        </>
    );
}