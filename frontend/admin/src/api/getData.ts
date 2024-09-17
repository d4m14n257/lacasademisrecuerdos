import { Data } from "@/model/types";

export async function getData<T>(endpoint: string) : Promise<Data<T>> {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/${endpoint}`);

        if (res.status === 204) {
            return {
                data: [],
                status: 204
            };
        }

        if (res.status === 200) {
            const response: { message: string; data: T } = await res.json();

            if (Array.isArray(response.data)) {
                return {
                    message: response.message,
                    data: response.data,
                    status: 200
                };
            }
        }

        return {
            message: `Unexpected response status: ${res.status}`,
            status: res.status
        };
    } catch (err: unknown) {
        if (err instanceof Error) {
            return {
                err: err.message,
                status: 503
            };
        }

        return {
            message: "Unexpected error",
            err: String(err),
            status: 500
        };
    }
}
