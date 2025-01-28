import { Data, ResponseWithData, ResponseWithInfo } from "@/model/type";

export async function getData<T>(endpoint: string, language?: "es" | "en") : Promise<Data<T>> {
    try {
        const options = {
            method: 'GET',
            headers: {
                'Cache-Control': 'no-cache',
                'Accept-Language': language ? language : 'en',
                'Content-Type': 'application/json',
            }
        }

        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}${endpoint}`, options);

        if (res.status === 204) {
            return {
                data: [],
                status: 204
            };
        }

        if (res.status === 200) {
            const response: ResponseWithData<T> = await res.json();

            return {
                message: response.message,
                data: response.data,
                status: 200
            };
        }

        const err : ResponseWithInfo = await res.json();

        return {
            message: err.message,
            err: err.info,
            status: res.status
        };
    }
    catch (err: unknown) {
        if (err instanceof Error) {
            return {
                err: err.message.charAt(0).toUpperCase() + err.message.slice(1),
                status: 503
            };
        }

        return {
            err: "Unexpected error",
            status: 500
        };
    }
}