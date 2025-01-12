"use client"

import { Fragment } from "react";
import PageHeader from "../general/PageHeader";
import CreateRoomModal from "./modals/CreateRoomModal";
import { RoomCard } from "@/model/types";

type Props = {
    title: string;
    buttonCreate: string;
    reloadAction: () => Promise<void>;
    handleOpen: (room?: RoomCard) => void;
    handleClose: () => void;
    open: boolean;
}

export default function PageHeaderRoom (props : Props) {
    return (
        <Fragment>
            <PageHeader 
                {...props}
            />
            <CreateRoomModal 
                closeConfirm
                {...props}
            />
        </Fragment>
    );
}