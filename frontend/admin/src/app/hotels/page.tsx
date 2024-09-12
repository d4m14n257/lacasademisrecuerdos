"use client"

import { useSession } from "next-auth/react";

export default function Hotels () {
    console.log(useSession().data);


    return (
        <h1>
            Hoteles
        </h1>
    );
}
