import { Typography } from "@mui/material";
import ModalBase from "../general/ModalBase";
import CreateRoomForm from "../form/CreateRoomForm";

import '../globals.css';

type Props = {
    open: boolean,
    handleClose: () => void,
    closeConfirm: boolean
    reloadAction: () => Promise<void>
}

export default function CreateRoomModal (props : Props) {
    const { open, handleClose, closeConfirm, reloadAction } = props;

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
                Create Room
            </Typography>
            <CreateRoomForm 
                handleClose={handleClose}
                reloadAction={reloadAction}
            />
        </ModalBase>
    ); 
}