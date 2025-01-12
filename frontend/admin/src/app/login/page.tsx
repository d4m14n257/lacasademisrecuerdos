import { Paper, Stack, Typography } from "@mui/material";

import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import LoginForm from "@/components/form/LoginForm";
import IconMode from "@/components/general/IconMode";
import "./login.css"

export default async function LoginPage () {
    const session = await getServerSession();
    
    if(session) {
        redirect("/")
    }

    return (
        <Paper className="paper_login" elevation={3}>
            <Stack direction='row' justifyContent='space-between' alignItems='center'>
                <Typography variant="h4">
                    Login
                </Typography>
                <IconMode />
            </Stack>
            <LoginForm />
        </Paper>
    );
}