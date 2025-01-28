import { ResponseErrorLabel, ResponseOnlyMessage, ResponseWithInfo } from "@/model/type";
import { Data } from "@/model/type";

export async function setData<T>(endpoint: string, data: T, type : "POST" | "PUT", language: "en" | "es") : Promise<Data<T>> {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}${endpoint}`, {
            method: type,
            headers: {
                'Accept-Language': language,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });

        if(res.status >= 200 && res.status <= 299) {
            const response  : ResponseOnlyMessage = await res.json();

            return {
                message: response.message,
                status: res.status
            }
        }

        if(res.status >= 400 && res.status <= 499) {

            if(res.status == 401) {
                return {
                    message: 'Unauthorized session',
                    status: 401
                }
            }

            const warning : ResponseErrorLabel = await res.json();

            if(warning.errors) {
                return {
                    errors: warning.errors,
                    status: res.status
                }
            }
            else {
                return {
                    message: warning.message,
                    err: warning.info,
                    status: res.status
                }
            }
        }

        const err : ResponseWithInfo = await res.json();

        return {
            message: err.message,
            err: err.info,
            status: res.status
        }
    }
    catch (err : unknown) {
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