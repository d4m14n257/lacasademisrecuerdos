import CredentialsProvider from "next-auth/providers/credentials";
import { AuthOptions } from "next-auth";
import { LoginResponse } from "@/model/LoginResponse";
import { ResponseOnlyMessage, ResponseWithData, ResponseWithInfo } from "@/model/Response";
import { Session } from "inspector";

export const authOptions : AuthOptions = {
    providers: [
        CredentialsProvider({
            name: 'Credentials',
            credentials: {
                username: { },
                password: { }
            },
            async authorize (credentials) {
                try {
                    if(typeof credentials !== "undefined") {
                        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/auth/login`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({
                                username: credentials.username,
                                password: credentials.password
                            })                        
                        })
                        
                        if(res.status == 401) {
                            const unauthorized : ResponseOnlyMessage = await res.json();
                            throw new Error(unauthorized.message);
                        }

                        if(res.status == 500) {
                            const error : ResponseWithInfo = await res.json();
                            throw new Error(error.message);
                        }
                        
                        const user : ResponseWithData<LoginResponse> = await res.json();
                        if(res.ok && user) {
                            return { ...user.data.user, token: user.data.token, tokenType: user.data.tokenType }
                        }
                        
                        return null;
                    }
                    else {
                        return null;
                    }
                }
                catch (error : unknown) {
                    throw new Error("Failed to login. Please try again more later.")
                }
            },
        }),
    ],
    pages: {
        signIn: "/login"
    },
    session: { strategy: "jwt" },
    callbacks: {
        async jwt({ token, user }) {

            if(user) { 
                token.user = {
                    id: user.id,
                    username: user.username,
                    email: user.email,
                    first_name: user.first_name,
                    last_name: user.last_name
                };
                token.token = user.token;
                token.tokenType = user.tokenType;
            }

            return token;
        },
        async session({ session, token } : { session : any, token : any }) {
            session.user = token.user;

            return session;
        }
    },
}