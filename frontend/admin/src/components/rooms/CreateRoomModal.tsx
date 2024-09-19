import { Box, Typography } from "@mui/material";
import ModalBase from "../general/ModalBase";
import CreateRoomForm from "../form/CreateRoomForm";

import '../globals.css';

type Props = {
    open: boolean,
    handleClose: () => void,
    closeConfirm?: boolean
}

export default function CreateRoomModal (props : Props) {
    const { open, handleClose, closeConfirm = false } = props;

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
            />
        </ModalBase>
    ); 
}