import { getToken } from "next-auth/jwt";
import { NextRequest, NextResponse } from "next/server";

export { default } from "next-auth/middleware";

export async function middleware(req: NextRequest) {
    const token = await getToken({ req });
    const loginUrl = new URL('/login', req.url);

    if (!token) {
        const res = NextResponse.redirect(loginUrl);
        return res;
    }

    return NextResponse.next();
}

export const config = {
    matcher: [
        '/', 
        '/users', 
        '/hotels', 
        '/rooms', 
        '/rooms/:id*'
    ],
};
