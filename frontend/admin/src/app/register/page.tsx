import { Paper, Stack, Typography } from "@mui/material";
import RegisterForm from "@/components/form/RegisterForm";
import IconMode from "@/components/general/IconMode";
import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import "./register.css"
import { ConfirmProvider } from "@/contexts/ConfirmContext";

export default async function RegisterPage () {
    const session = await getServerSession();

    if(session) {
        redirect("/")
    }

    return (
        <Paper className="paper_register">
            <Stack direction='row' justifyContent='space-between' alignItems='center'>
                <Typography variant="h4">
                    Register
                </Typography>
                <IconMode />
            </Stack>
            <ConfirmProvider>
                <RegisterForm />
            </ConfirmProvider>
        </Paper>
    );
}