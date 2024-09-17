import DialogConfirm from "@/components/general/DialogConfirm";
import React, { Context, createContext, useCallback, useReducer, useState } from "react";

type ConfirmContext = {
    confirm: () =>  Promise<void>;
    handleMessage: (value : ReducerConfirm) => void
}

type ReducerConfirm = {
    title: string,
    content: string
}

function reducer (state : ReducerConfirm, action : ReducerConfirm) : ReducerConfirm {
    const { title, content } = action;
    
    return {
        title: title ? title : state.title,
        content: content ? content : state.content
    }
}

export const Confirm : Context<ConfirmContext>= createContext({
    confirm: async () => { },
    handleMessage: (value) => { }
});

export const ConfirmProvider = ({children} : {children: React.ReactNode}) => {
    const [ resolverReject, setResolverReject ] = useState<[() => void, () => void] | []>([]);
    const [ resolve, reject ] = resolverReject;
    const [ message, dispactMessage ] = useReducer(reducer, {
        title: "",
        content: ""
    })

    const confirm = useCallback(() : Promise<void> => {
        return new Promise((resolve, reject) => {
            setResolverReject([resolve, reject]);
        })
    }, []);

    const handleClose = useCallback(() => {
        setResolverReject([]);
    }, [])

    const handleCancel = useCallback(() => {
        if (reject) {
            reject();
            handleClose();
        }
    }, [reject, handleClose]);
    
    const handleConfirm = useCallback(() => {
        if (resolve) {
            resolve();
            handleClose();
        }
    }, [resolve, handleClose]);

    const handleMessage = useCallback((value : ReducerConfirm) => {
        dispactMessage(value)
    }, [])

    return (
        <>
            <Confirm.Provider value={{ confirm, handleMessage }}>
                {children}
            </Confirm.Provider>
            <DialogConfirm 
                open={resolverReject.length === 2}
                onClose={handleCancel}
                onCancel={handleCancel}
                onConfirm={handleConfirm}
                value={message}
            />
        </>
    );
}