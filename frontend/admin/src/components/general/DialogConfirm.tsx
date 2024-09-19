import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";

type Props = {
    open: boolean,
    onClose: () => void,
    onCancel: () => void,
    onConfirm: () => void
    value: {
        title: string,
        content: string
    }
}

export default function DialogConfirm (props : Props) {
    const { open, onClose, onCancel, onConfirm, value } = props;

    return (
        <Dialog
            open={open}
            onClose={onClose}
        >
            <DialogTitle>{value.title}</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {value.content}
                </DialogContentText>
                <DialogActions>
                    <Button
                        size="small"
                        onClick={onConfirm}
                        variant="outlined"
                    >
                        Accept
                    </Button>
                    <Button
                        color="error"
                        size="small"
                        onClick={onCancel}
                        variant="outlined"
                    >
                        Cancel
                    </Button>
                </DialogActions>
            </DialogContent>
        </Dialog>
    );
}