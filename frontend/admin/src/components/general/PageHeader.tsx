import { Button, Stack, Typography } from "@mui/material";

type  Props = {
    title: string;
    buttonCreate?: string;
    handleOpen: () => void;
}

export default function PageHeader (props : Props) {
    const { title, buttonCreate, handleOpen } = props;

    return (
        <Stack direction='row' justifyContent='space-between' alignItems='center'>
            <Typography variant="h4">
                {title}
            </Typography>
            <Button onClick={handleOpen} variant="contained" color='success'>
                {buttonCreate}
            </Button>
        </Stack>
    );
}