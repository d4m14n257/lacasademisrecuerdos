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
    fileId?: string;
}

export default function EditFilesModal (props : Props) {
    const { open, handleClose, handleReload, closeConfirm = false, roomId, fileId } = props;

    return (
        <ModalBase
            open={open}
            handleClose={handleClose}
            closeConfirm={closeConfirm}
        >
            <Typography
                variant="h6"
            >
                {fileId ? "Change main image" : "Upload image"}
            </Typography>
            <FileForm 
                roomId={roomId}
                handleClose={handleClose}
                reloadAction={handleReload}
                fileId={fileId}
            />
        </ModalBase>
    );
}