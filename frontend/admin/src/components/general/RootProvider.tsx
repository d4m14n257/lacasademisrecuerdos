"use client"

import { SessionProvider } from "next-auth/react";
import { Session } from "next-auth";
import Layout from "./Layout";

export default function RootProvider ({
    children,
    session
} : Readonly<{children: React.ReactNode, session: Session | null}>
) {
    return (
        <SessionProvider session={session}>
            <Layout>
                {children}
            </Layout>
        </SessionProvider>
    );
}