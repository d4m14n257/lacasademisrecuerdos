"use client"

import { Backdrop, CircularProgress } from "@mui/material";
import { Context, createContext, Fragment, ReactNode, useCallback, useState } from "react"

export const Loading = createContext({
    handleOpenLoading: () => {},
    handleCloseLoading: () => {}
}); 

export const LoadingProvider = ({ children } : {children: ReactNode}) => {
    const [ isLoading, setIsLoading ] = useState<boolean>(false);

    const handleOpenLoading = useCallback(() => {
        setIsLoading(true);
    }, [])

    const handleCloseLoading = useCallback(() => {
        setIsLoading(false);
    }, [])

    return (
        <Fragment>
            <Loading.Provider value={{ handleOpenLoading, handleCloseLoading }}>
                {children}
            </Loading.Provider>
            <Backdrop
                open={isLoading}
            >
                <CircularProgress disableShrink />
            </Backdrop>
        </Fragment>
    );
}