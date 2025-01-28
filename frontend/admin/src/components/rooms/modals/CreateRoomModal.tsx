import { Typography } from "@mui/material";
import ModalBase from "../../general/ModalBase";
import CreateRoomForm from "../../form/CreateRoomForm";

import '../../globals.css';
import { RoomLanguage } from "@/model/types";

type Props = {
    open: boolean,
    handleClose: () => void,
    closeConfirm: boolean
    reloadAction: () => Promise<void>
    room?: Omit<RoomLanguage, 'files' | 'created_at' | 'status'> | null
}

export default function CreateRoomModal (props : Props) {
    const { open, room, handleClose, closeConfirm, reloadAction } = props;

    return (
        <ModalBase
            open={open}
            handleClose={handleClose}
            closeConfirm={closeConfirm}
        >
            <Typography
                variant="h6"
                className="modal-title"
            >
                {room ? "Create Room" : "Edit room"}
            </Typography>
            <CreateRoomForm 
                handleClose={handleClose}
                reloadAction={reloadAction}
                room={room}
            />
        </ModalBase>
    ); 
}