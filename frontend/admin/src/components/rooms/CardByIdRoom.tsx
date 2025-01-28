"use client"

import { Grid } from "@mui/material";
import ListFileRooms from "./ListFileRooms";
import CardRoomInfo from "./CardRoomInfo";
import { RoomLanguage } from "@/model/types";
import { useCallback, useContext, useState } from "react";
import { getData } from "@/api/getData";
import { Session } from "next-auth";
import { Advice } from "@/contexts/AdviceProvider";
import { ROOMS_GENERAL } from "@/constants/endpoints";

type Props = {
    res: RoomLanguage;
    session: Session | null
}

function useCardByIdRoom ({ res, session } : { res: RoomLanguage, session: Session | null }) {
    const [ room, setRoom ] = useState<RoomLanguage>(res);
    const { handleOpen, handleAdvice } = useContext(Advice);

    const handleReload = useCallback(async () => {
        const res = await getData<RoomLanguage>(`${ROOMS_GENERAL}/${room.id}`, false, session?.token);

        if(res.data && !Array.isArray(res.data)) {
            setRoom(res.data);
        }
        else {
            handleAdvice({
                message: res.err,
                horizontal: 'right',
                vertical: 'bottom',
                status: res.status
            });

            handleOpen();
        }

    }, [])

    return {
        room,
        handleReload
    };
}

export default function CardByIdRoom (props : Props) {
    const { room, handleReload } = useCardByIdRoom(props);

    return (
        <Grid
            container
            spacing={2}
        >
            <Grid
                item
                md={6}
                xs={12}
            >
                <CardRoomInfo 
                    {...room}
                    handleReload={handleReload}
                />
            </Grid>
            <Grid
                item
                md={6}
                xs={12}
            >
                <ListFileRooms 
                    roomId={room.id}
                    data={room.files}
                    handleReload={handleReload}
                    session={props.session}
                />
            </Grid>
        </Grid>
    );
}