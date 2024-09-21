"use client"

import { setData } from "@/api/setData";
import { Advice } from "@/contexts/AdviceProvider";
import { Confirm } from "@/contexts/ConfirmContext";
import { RoomStatus } from "@/model/types";
import { Stack, Switch, Typography } from "@mui/material";
import { useSession } from "next-auth/react";
import { useContext, useState } from "react";

const useChangeSwitchStatus = ({ id, status } : RoomStatus) => {
    const { data : session } = useSession();
    const [ isActive, setIsActive ] = useState<boolean>(status == 'active' ? true : false)
    const { confirm, handleMessage } = useContext(Confirm);
    const { handleOpen, handleAdvice } = useContext(Advice);

    const handleChangeStatus = async () => {
        try {
            if(session) {
                handleMessage({
                    title: 'Are you sure you want to change the status?',
                    content: status == 'hidden' ?
                        "If you change the status to active this room will be available to be offered to clients and will be displayed on the main page" :
                        "If you change the status to hidden the room will no longer be available to customers on the main page but they will retain the data for future use"
                })

                await confirm()
                    .catch(() => { return });


                const res = await setData<RoomStatus>('room/admin/status', session?.token, 
                    { id, status: isActive ? 'active' : 'hidden' }
                );

                handleAdvice({
                    message: res.message,
                    status: res.status
                })

                handleOpen();

                if(res.status >= 200 && res.status <= 299) {
                    setIsActive(!isActive);
                }
            }
            else {
                throw new Error('Session is not valid')
            }
            
        }
        catch (err : unknown) {
            if(err instanceof Error) {
                handleAdvice({
                    message: err.message,
                    vertical: 'bottom',
                    horizontal: 'left',
                    status: err.message == 'Session is not valid' ? 401 : 500
                })
            }
            else {
                handleAdvice({
                    message: 'Unknown error',
                    status: 503
                })
            }

            handleOpen();
        }
    }

    return {
        handleChangeStatus,
        isActive
    }

}

export default function ChangeSwitchStatus(props : RoomStatus) {
    const { handleChangeStatus, isActive } = useChangeSwitchStatus(props);

    return (
        <Stack direction='row' spacing={1} sx={{ alignItems: 'center' }}>
            <Typography variant="caption">Hidden</Typography>
            <Switch 
                checked={isActive}
                size='small'
                onChange={handleChangeStatus}
                value='off'
            />
            <Typography variant="caption">Active</Typography>
        </Stack>
    );
}