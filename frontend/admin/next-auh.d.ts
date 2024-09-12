import NextAuth from "next-auth";

declare module "next-auth" {
    interface Session {
        user: {
            id: string;
            username: string;
            email: string;
            first_name: string;
            last_name: string;
        };
        exp: number,
        uat: number,
        jti: string
    }
    
    interface User {
        id: string;
        username: string;
        email: string;
        first_name: string;
        last_name: string;
        token: string;
        tokenType: string;
    }
}
