import { getToken } from "next-auth/jwt";
import { NextRequest, NextResponse } from "next/server";
import { ResponseOnlyMessage } from "./model/response";
import { signOut } from "next-auth/react";

export { default } from "next-auth/middleware";

export async function middleware(req: NextRequest) {
    const token = await getToken({ req });
    const loginUrl = new URL('/login', req.url);

    // Para evitar redirecciones infinitas, revisa si ya tienes una redirección en curso
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
            redirection.cookies.set('is-redirecting', 'true', { maxAge: 10 });

            redirection.cookies.set('logoutMessage', data.message);
            
            signOut();
            return redirection;
        }
    } catch (err: any) {
        if (!isRedirecting) {
            redirection.cookies.set('is-redirecting', 'true', { maxAge: 10 });
            redirection.cookies.set('error-message', 'Unable to connect to the server. Please try again later.');

            return redirection;
        }
    }

    if(!isRedirecting) {
        res.cookies.delete('error-message');
        res.cookies.delete('is-redirecting');
    }

    return res;
}

export const config = {
    matcher: ['/', '/users', '/hotels', '/rooms'],
};
