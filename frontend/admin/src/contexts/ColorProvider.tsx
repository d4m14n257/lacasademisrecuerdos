"use client"

import React, { createContext, useMemo, useState, useEffect, Context } from "react";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';

type Props = {
    children: React.ReactNode
}

export const Color = createContext({
    colorMode: { toggleColorMode: () => {} },
    mode: "dark"
});

export const ColorProvider = (props: Props) => {
    const { children } = props;
    const darkModeMediaQuery = useMediaQuery('(prefers-color-scheme: dark)');

    const [mode, setMode] = useState<"dark" | "light">("dark");

    useEffect(() => {
        const currentTheme = localStorage.getItem('theme');

        if (currentTheme == null) {
            if(document.cookie.includes('preferred_color_mode=dark'))
                setMode("dark");
            else
                setMode('light')
        } else if (currentTheme === "light" || currentTheme === "dark") {
            if (currentTheme) {
                setMode(currentTheme);
            } else {
                setMode(darkModeMediaQuery ? 'dark' : 'light');
            }
        }
    }, [darkModeMediaQuery]);

    useEffect(() => {
        localStorage.setItem('theme', mode);
    }, [mode]);

    const colorMode = useMemo(
        () => ({
            toggleColorMode: () => {
                setMode((prevMode) => (prevMode === "light" ? "dark" : "light"));
            },
        }), []
    );

    const theme = useMemo(
        () =>
            createTheme({
                palette: {
                    mode,
                },
            }),
        [mode]
    );

    return (
        <Color.Provider value={{ colorMode, mode }}>
            <ThemeProvider theme={theme}>
                {children}
            </ThemeProvider>
        </Color.Provider>
    );
}
