import { ReactNode, useCallback, useContext } from "react";
import { Modal, Fade, Box, Backdrop } from "@mui/material";
import { Confirm } from "@/contexts/ConfirmContext";

type Props = {
    children: ReactNode
    open: boolean,
    handleClose: () => void,
    closeConfirm?: boolean,
}

export default function ModalBase(props : Props) {
    const { open, handleClose, closeConfirm = false, children } = props
    const { confirm, handleMessage } = useContext(Confirm)

    const handleCloseConfirmModal = useCallback(async () => {
        try {
            handleMessage({
                title: 'Are you sure you want to close the box?', 
                content: 'If you close the box, nothing you have done will be saved.'
            })

            await confirm()
                .catch(() => {throw {canceled: true}});

            handleClose();
        }
        catch (err){
            return;
        }
    }, [])
    
    return (
        <Modal
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            open={open}
            onClose={closeConfirm ? handleCloseConfirmModal : handleClose}
            closeAfterTransition
            slots={{ backdrop: Backdrop }}
            slotProps={{
                backdrop: {
                    timeout: 500,
                },
            }}
            sx={{
                overflowY: "scroll",
            }}
        >
            <Fade in={open}>
                <Box
                    sx={{
                        position: 'absolute',
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        minWidth: 250,
                        maxWidth: 850,
                        top: '50%',
                        left: '50%',
                        transform: 'translate(-50%, max(-50vh, -50%))',
                        bgcolor: 'background.paper',
                        boxShadow: "2em",
                        padding: "2em",
                        borderRadius: "1em",
                    }}
                >
                    <Box>
                        {children}
                    </Box>
                </Box>
            </Fade>
        </Modal>
    );
}