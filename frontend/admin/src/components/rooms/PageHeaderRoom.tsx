"use client"

import { useCallback, useState } from "react";
import PageHeader from "../general/PageHeader";
import CreateRoomModal from "./CreateRoomModal";

type Props = {
    title: string;
    buttonCreate: string;
    reloadAction: () => Promise<void>;
}

export default function PageHeaderRoom (props : Props) {
    const { reloadAction } = props;
    const [ open, setOpen ] = useState<boolean>(false);

    const handleOpen = useCallback(() => {
        setOpen(true)
    }, []);

    const handleClose = useCallback(() => {
        setOpen(false)
    }, []);

    return (
        <>
            <PageHeader 
                {...props}
                handleOpen={handleOpen}
            />
            <CreateRoomModal 
                closeConfirm
                open={open}
                handleClose={handleClose}
                reloadAction={reloadAction}
            />
        </>
    );
}