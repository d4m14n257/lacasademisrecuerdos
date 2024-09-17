"use client"

import { useCallback, useState } from "react"
import { Button, Stack, Typography } from "@mui/material";
import CreateRoomModal from "../rooms/CreateRoomModal";

type Props = {
    title: string,
    buttonCreate: string
}

export default function PageHeader (props : Props) {
    const { title, buttonCreate } = props;
    const [ open, setOpen ] = useState<boolean>(false);

    const handleOpen = useCallback(() => {
        setOpen(true)
    }, [])

    const handleClose = useCallback(() => {
        setOpen(false)
    }, [])

    return (
        <>
            <Stack direction='row' justifyContent='space-between' alignItems='center'>
                <Typography variant="h4">
                    {title}
                </Typography>
                <Button onClick={handleOpen} variant="contained" color='success'>
                    {buttonCreate}
                </Button>
            </Stack>
            {open && 
                <CreateRoomModal 
                    open={open}
                    handleClose={handleClose}
                    closeConfirm
                />
            }
        </>
    );
}