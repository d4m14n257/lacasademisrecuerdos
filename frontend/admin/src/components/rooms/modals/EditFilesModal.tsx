import ModalBase from "@/components/general/ModalBase";
import FileForm from "@/components/form/FileForm";
import { Typography } from "@mui/material";

import '../../globals.css';

type Props = {
    open: boolean;
    handleClose: () => void;
    handleReload: () => Promise<void>;
    closeConfirm?: boolean;
    roomId: string;
}

export default function EditFilesModal (props : Props) {
    const { open, handleClose, handleReload, closeConfirm = false, roomId } = props;

    return (
        <ModalBase
            open={open}
            handleClose={handleClose}
            closeConfirm={closeConfirm}
        >
            <Typography
                variant="h6"
            >
                Edit files
            </Typography>
            <FileForm 
                roomId={roomId}
                handleClose={handleClose}
                reloadAction={handleReload}
            />
        </ModalBase>
    );
}