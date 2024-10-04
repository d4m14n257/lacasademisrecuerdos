"use client"

import { useCallback, useRef, useState } from "react"

export default function useModal<T> () {
    const [open, setOpen] = useState<boolean>(false);
    const data = useRef<T[] | T | null>(null);

    const handleOpen = useCallback((isData?: T[] | T) => {
        if(isData) {
            data.current = isData;
        }

        setOpen(true);
    }, []);

    const handleClose = useCallback(() => {
        setOpen(false);
    }, [])

    return {
        open,
        handleOpen,
        handleClose,
        data: data.current
    }
}