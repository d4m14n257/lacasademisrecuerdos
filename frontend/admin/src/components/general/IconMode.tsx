"use client"

import { useContext } from "react";
import { Color } from "@/contexts/ColorProvider";
import { IconButton } from "@mui/material";
import LightModeIcon from '@mui/icons-material/LightMode';
import ModeNightIcon from '@mui/icons-material/ModeNight';

export default function IconMode () {
    const { mode, colorMode } = useContext(Color);

    return (
        <IconButton 
            onClick={colorMode.toggleColorMode}
            sx={{
                borderRadius: 2,
                border: "1px solid"
            }}
        >
            {mode == 'dark' ? 
                <LightModeIcon /> :
                <ModeNightIcon />
            }
        </IconButton>
    );
}