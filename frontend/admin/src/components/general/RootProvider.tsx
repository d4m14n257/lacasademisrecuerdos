"use client"

import { SessionProvider } from "next-auth/react";
import { Session } from "next-auth";
import Layout from "./Layout";
import { AdviceProvider } from "@/contexts/AdviceProvider";

export default function RootProvider ({
    children,
    session
} : Readonly<{children: React.ReactNode, session: Session | null}>
) {
    return (
        <SessionProvider session={session}>
            <AdviceProvider>
                <Layout>
                    {children}
                </Layout>
            </AdviceProvider>
        </SessionProvider>
    );
}