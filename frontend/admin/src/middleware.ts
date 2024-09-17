import { getToken } from "next-auth/jwt";
import { NextRequest, NextResponse } from "next/server";
import { ResponseOnlyMessage } from "./model/response";
import { signOut } from "next-auth/react";

export { default } from "next-auth/middleware";

export async function middleware(req: NextRequest) {
    const token = await getToken({ req });
    const loginUrl = new URL('/login', req.url);

    const isRedirecting = req.cookies.get('is-redirecting');

    if (!token) {
        return NextResponse.redirect(loginUrl);
    }

    const res = NextResponse.next();
    const baseUrl = new URL('/', req.url);
    const redirection = NextResponse.redirect(baseUrl);

    try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/auth/validate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                token: token.token
            })
        });

        const data: ResponseOnlyMessage = await response.json();

        if (response.status === 500) {
            redirection.cookies.set('error-message', data.message);
            return redirection;
        }

        if (response.status === 401) {
            if(!isRedirecting) {
                redirection.cookies.set('is-redirecting', 'true', { maxAge: 10 });
                redirection.cookies.set('logout-message', data.message);

                return redirection;
            }
        }
    } catch (err: any) {
        if (!isRedirecting) {
            redirection.cookies.set('is-redirecting', 'true', { maxAge: 10 });
            redirection.cookies.set('error-message', 'Unable to connect to the server. Please try again later.');

            return redirection;
        }
    }
    
    res.cookies.delete('is-redirecting');

    if(!isRedirecting) {
        if(req.cookies.get('logout-message')){
            signOut();
        }

        res.cookies.delete('error-message');
        res.cookies.delete('logout-message');
    }
        
    return res;
}

export const config = {
    matcher: ['/', '/users', '/hotels', '/rooms'],
};
