"use client"

import SnackAdvice from "@/components/general/SnackAdvice";
import React, { createContext, useState, useReducer, Context, useCallback } from "react";

type AdviceContext = {
    handleOpen: () => void,
    handleAdvice: (value: Action) => void
}

type ReducerAdvice = {
    message: string,
    vertical: 'top' | 'bottom',
    horizontal: 'right' | 'left',
    status: number
}

type Action = {
    message?: string,
    vertical?: 'top' | 'bottom',
    horizontal?: 'right' | 'left',
    status: number
}

function reducer (state : ReducerAdvice, action : Action) : ReducerAdvice {
    const { message, status, vertical, horizontal } = action;

    return {
        message: message ? message : state.message,
        vertical: vertical ? vertical : state.vertical,
        horizontal: horizontal ? horizontal : state.horizontal,
        status: status
    }
}

export const Advice : Context<AdviceContext> = createContext({
    handleOpen: () => {},
    handleAdvice: (value) => {}
});

export const AdviceProvider = ({children} : {children: React.ReactNode}) => {
    const [open, setOpen] = useState<boolean>(false);
    const [advice, dispatchAdvice] = useReducer(reducer, {
        message: "",
        status: 0,
        vertical: "bottom",
        horizontal: "left"
    });

    const handleAdvice = useCallback((value : Action) : void => {
        dispatchAdvice(value);
    }, [])

    const handleOpen = useCallback(() : void => {
        setOpen(true);
    }, [])

    const handleClose = useCallback(() : void => {
        setOpen(false)
    }, [])

    return (
        <>
            <Advice.Provider value={{ handleOpen, handleAdvice }}>
                {children}
            </Advice.Provider>
            <SnackAdvice 
                message={advice.message}
                status={advice.status}
                open={open}
                onClose={handleClose}
                vertical={advice.vertical}
                horizontal={advice.horizontal}
            />
        </>
    );
}