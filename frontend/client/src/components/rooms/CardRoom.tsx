"use client"

import { RoomCard } from "@/model/type";
import { useRouter } from "next/navigation";
import { useCallback } from "react";

type Props = {
    rooms: RoomCard[];
    dict: {
        button: string;
    }
}

export default function CardRoom (props: Props) {
    const { rooms, dict } = props;
    const router = useRouter();

    const handleNavigateRoom = useCallback((id: string) => {
        router.push(`rooms/${id}`)
    }, []);

    return (
        <>
            {rooms.map((room, index) => (
                <div className="card-room" key={room.id} id={index % 2 == 0 ? 'even' : 'odd'}>
                    <img 
                        src={`data:image/webp;base64,${room.file}`}
                        alt={room.file_name}
                    />
                    <div>
                        <h3>{room.name}</h3>
                        <p>{room.summary}</p>
                        <button
                            onClick={() => handleNavigateRoom(room.id)}
                        >
                            {dict.button}
                        </button>
                    </div>
                </div>
            ))}
        </>
    );

}