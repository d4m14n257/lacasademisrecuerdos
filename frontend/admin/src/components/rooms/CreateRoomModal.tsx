import { Box, Typography } from "@mui/material";
import ModalBase from "../general/ModalBase";
import CreateRoomForm from "../form/CreateRoomForm";

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
            <Box>
                <Typography>
                    Create Room
                </Typography>
            </Box>
            <CreateRoomForm 
                handleClose={handleClose}
            />
        </ModalBase>
    ); 
}