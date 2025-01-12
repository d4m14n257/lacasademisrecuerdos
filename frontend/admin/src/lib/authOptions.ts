import CredentialsProvider from "next-auth/providers/credentials";
import { AuthOptions } from "next-auth";
import { LoginResponse } from "@/model/loginResponse";
import { ResponseOnlyMessage, ResponseWithData, ResponseWithInfo } from "@/model/response";
import { LOGIN } from "@/constants/endpoints";

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
                        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}${LOGIN}`, {
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

                        if(!Array.isArray(user.data)) {
                            if(res.ok && user) {
                                return { ...user.data.user, token: user.data.token, tokenType: user.data.tokenType, maxAge: user.data.maxAge / 1000 }
                            }
                        }
                        
                        return null;
                    }
                    else {
                        return null;
                    }
                }
                catch (error : unknown) {
                    if(error instanceof Error) {
                        if(error.message === 'fetch failed')
                            throw new Error("Failed to login. Please try again more later")

                        throw new Error(error.message);
                    }

                    throw new Error('Unknonw error')
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

                const decodedAccessToken = JSON.parse(Buffer.from(user.token.split(".")[1], "base64").toString());

                if (decodedAccessToken) {
                    token.accessTokenExpires = decodedAccessToken["exp"] * 1000;
                }

            }

            if (token.accessTokenExpires  && (Date.now() < Number(token.accessTokenExpires ))) {
                return token
            }
            
            return {
                ...token,
                error: "AccessTokenError"
            }
        },
        async session({ session, token } : {session : any, token : any}) {
            session.user = token.user;
            session.token = token.token;
            session.error = token.error;

            return session;
        }
    },
}