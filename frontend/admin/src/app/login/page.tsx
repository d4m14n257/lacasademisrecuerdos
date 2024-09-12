import { Paper, Stack, Typography } from "@mui/material";

import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import LoginForm from "@/components/form/LoginForm";
import { AdviceProvider } from "@/contexts/AdviceProvider";
import "./login.css"
import IconMode from "@/components/general/IconMode";

export default async function LoginPage () {
    const session = await getServerSession();
    
    if(session) {
        redirect("/")
    }

    return (

        <AdviceProvider>
            <Paper className="paper_login" elevation={3}>
                <Stack direction='row' justifyContent='space-between' alignItems='center'>
                    <Typography variant="h4">
                        Login
                    </Typography>
                    <IconMode />
                </Stack>
                <LoginForm />
            </Paper>
        </AdviceProvider>
    );
}