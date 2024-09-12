import { Alert, Box, Snackbar } from "@mui/material";
import { HandlerError } from "../handler/HandlerError";

type Props = {
    message?: string,
    status: number,
    open: boolean,
    onClose: () => void,
    vertical?: 'top' | 'bottom'
    horizontal?: 'right' | 'left'
}

export default function SnackAdvice (props : Props) {
    const { message, status, open, onClose, vertical, horizontal } = props;

    return (
        <Snackbar
            anchorOrigin={{ vertical: vertical ? vertical : 'bottom', horizontal: horizontal ? horizontal : 'right' }}
            open={open}
            autoHideDuration={5000}
            onClose={onClose}
        >
            <Box>
                <HandlerError>
                    <HandlerError.When isError={!( status >= 200 && status <= 299 )}>
                        <Alert
                            onClose={onClose}
                            severity="success"
                            variant="filled"
                        >
                            { message }
                        </Alert>
                    </HandlerError.When>
                    <HandlerError.When isError={!( status >= 400 && status <= 499 )}>
                        <Alert
                            onClose={onClose}
                            severity="warning"
                            variant="filled"
                        >
                            { message }
                        </Alert>
                    </HandlerError.When>
                    <HandlerError.Else>
                        <Alert
                            onClose={onClose}
                            severity="error"
                            variant="filled"
                        >
                            { message }
                        </Alert>
                    </HandlerError.Else>
                </HandlerError>
            </Box>
        </Snackbar>
    );
}