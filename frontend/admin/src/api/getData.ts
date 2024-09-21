import { ResponseWithData, ResponseWithInfo } from "@/model/response";
import { Data } from "@/model/types";

export async function getData<T>(endpoint: string, hasFiles: boolean, token?: string) : Promise<Data<T>> {
    console.log(`${process.env.NEXT_PUBLIC_SERVER_HOST}/${endpoint}`)

    try {
        const options = {
            method: 'GET',
            headers: {
                'Content-Type': hasFiles ? 'application/octet-stream' : 'application/json',
                'Authorization': token ? `Bearer ${token}` : ''
            }
        }

        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/${endpoint}`, options);

        if (res.status === 204) {
            return {
                data: [],
                status: 204
            };
        }

        if (res.status === 200) {
            const response: ResponseWithData<T> = await res.json();

            if (Array.isArray(response.data)) {

                return {
                    message: response.message,
                    data: response.data,
                    status: 200
                };
            }
        }

        const err : ResponseWithInfo = await res.json();

        return {
            message: err.message,
            err: err.info,
            status: res.status
        };
    } catch (err: unknown) {
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
