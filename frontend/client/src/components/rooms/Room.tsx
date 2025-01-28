"use client"

import { Room as RoomType} from "@/model/type";
import Reservation from "../forms/Reservation";
import RoomSlider from "./RoomSlider";

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
}

type Props = {
    room: RoomType;
    dict: Dict;
} 

export default function Room (props : Props) {
    const { room, dict } = props;

    return ( 
        <>  
            <RoomSlider 
                images={room.files}
            />
            <div className="details">
                <div>
                    <h1>{room.name}</h1>
                    <p>{room.description}</p>
                    <p className="summary">{room.summary}</p>
                    <div className="room-price">
                        <p>
                            <span>{dict.price.single}:</span>
                            <span>${room.single_price} MXN</span>
                        </p>
                        {room.double_price && 
                            <p>
                                <span>{dict.price.double}:</span>
                                <span>${room.double_price} MXN</span>
                            </p>
                        }
                    </div>
                </div>
                <div className="form-reservation">
                    <h2>{dict.form.title}</h2>
                    <Reservation 
                        id={room.id}
                        labels={dict.form.labels}
                        errors={dict.form.errors}
                    />
                </div>
            </div>
        </>
    );
}