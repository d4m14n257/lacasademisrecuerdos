"use client";

import { useSession } from "next-auth/react";
import { redirect } from "next/navigation";

const withAuth = (Component: React.FC) => {
    return function AuthenticatedComponent() {
        const { status } = useSession();

        if (status === "unauthenticated") {
            redirect("/login");
        }

        return <Component />;
    };
};

export default withAuth;
