"use client"

import { setData } from "@/api/setData";
import { ROOMS_STATUS } from "@/constants/endpoints";
import { Advice } from "@/contexts/AdviceProvider";
import { Confirm } from "@/contexts/ConfirmContext";
import { Loading } from "@/contexts/LoadingProvider";
import { RoomStatus } from "@/model/types";
import { Stack, Switch, Typography } from "@mui/material";
import { useSession } from "next-auth/react";
import { useContext, useState } from "react";

const useChangeSwitchStatus = ({ id, status, handleReload } : RoomStatus & { handleReload : () => Promise<void> }) => {
    const { data : session } = useSession();
    const { confirm, handleMessage } = useContext(Confirm);
    const { handleOpen, handleAdvice } = useContext(Advice);
    const { handleOpenLoading, handleCloseLoading } = useContext(Loading);

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
                    .catch(() => { throw { canceled: true } });

                handleOpenLoading();

                const res = await setData<RoomStatus>(`${ROOMS_STATUS}`, { id, status }, "PUT", session?.token);

                if(res.status >= 200 && res.status <= 299) {
                    handleAdvice({
                        message: res.message,
                        status: res.status
                    })
    
                    handleOpen();
                    handleReload();
                }
                else {
                    handleAdvice({
                        message: res.err,
                        status: res.status
                    })
    
                    handleOpen();
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
            if(typeof err === 'object' && err !== null && 'canceled' in err) {
                return;
            }
            else {
                handleAdvice({
                    message: 'Unknown error',
                    status: 503
                })
            }

            handleOpen();
        }
        finally {
            handleCloseLoading();
        }
    }

    return {
        handleChangeStatus,
    }
}

export default function ChangeSwitchStatus(props : RoomStatus & { handleReload: () => Promise<void> }) {
    const { handleChangeStatus } = useChangeSwitchStatus(props);

    return (
        <Stack direction='row' spacing={1} sx={{ alignItems: 'center' }}>
            <Typography variant="caption">Hidden</Typography>
            <Switch 
                checked={props.status == 'active' ? true : false}
                size='small'
                onChange={handleChangeStatus}
                value='off'
                aria-hidden={false}
            />
            <Typography variant="caption">Active</Typography>
        </Stack>
    );
}