"use client"

import { useCallback, useContext, useEffect, useRef } from "react";
import { Avatar, Box, Button, Paper, Stack, Typography } from "@mui/material";
import { Session } from "next-auth";
import { signOut, useSession } from "next-auth/react";
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import IconMode from "./IconMode";
import "../globals.css"

type Props = {
    open: boolean,
    handleChangeOpen: () => void
}

const useCardSettings = (props : Props)  => {
    const { data : session } : { data : Session | null } = useSession();
    const { open, handleChangeOpen } = props;
    const state = useRef<boolean>(false);
    const userSettingsRef = useRef<HTMLDivElement | null>(null)

    const handleClickOutside = useCallback((event : Event) => {
        if(state.current) {
            if(userSettingsRef.current !== null) {
                const targetElement = event.target as Node;

                if(!userSettingsRef.current.contains(targetElement))
                    handleChangeOpen();
            }
        }
        else 
            state.current = true;
    }, [])

    useEffect(() => {
        window.addEventListener('click', handleClickOutside);

        return () => {
            window.removeEventListener('click', handleClickOutside)
        };
    }, [open])

    const handleLogout = () => { signOut() }

    return {
        session,
        userSettingsRef,
        handleLogout
    };
}

export default function CardSettings (props : Props) {
    const { open } = props;
    const { session, userSettingsRef, handleLogout } = useCardSettings(props);

    return (
        <Paper ref={userSettingsRef} sx={{display: open ? "block" : "none"}} className="card-settings" elevation={4}>
            <Stack direction='row' justifyContent="space-between" alignItems="center">
                <Box className="body-settings">
                    <Avatar sx={{height: 48, width: 48, fontSize: "21px", fontWeight: 500}}>
                        {session?.user.username.charAt(0).toUpperCase()}
                    </Avatar>
                    <Stack direction='column'>
                        <Typography variant="h5">{session?.user.username}</Typography>
                        <Typography variant="subtitle2">{session?.user.email}</Typography>
                    </Stack>
                </Box>
                <IconMode />
            </Stack>
            <Stack direction='column' spacing={1}>
                <Button 
                    variant="outlined" 
                    startIcon={<SettingsIcon />} 
                    sx={{ borderRadius: 8 }}
                >
                    User setting
                </Button>
                <Button 
                    variant="outlined" 
                    startIcon={<LogoutIcon />} 
                    sx={{ borderRadius: 8 }}
                    onClick={handleLogout}
                >
                    Logout
                </Button>
            </Stack>
        </Paper>
    );
}