import Map from "@/components/hotels/Map";
import { HandlerError } from "@/components/handler/HandlerError";
import { ResponseWithData, ResponseWithInfo } from "@/model/response";
import { Data, HotelWitFiles } from "@/model/types";

const columns = {

}

async function getHotels() : Promise<Data<HotelWitFiles>> {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/hotel`);

        if(res.status == 204) {
            return {
                data: [],
                status: 204
            };
        }

        if(res.status == 200) {
            const hotels : ResponseWithData<HotelWitFiles> = await res.json();

            if(Array.isArray(hotels.data)) {
                return {
                    message: hotels.message,
                    data: hotels.data,
                    status: 200
                };
            }
        }

        const err : ResponseWithInfo = await res.json();

        return {
            message: err.message,
            err: err.info,
            status: res.status
        }
    }
    catch (err : unknown ){
        if(err instanceof Error) {
            return {
                err: err.message,
                status: 503
            }
        }

        return {
            message: "Unexpected error",
            err: err,
            status: 500
        }
    }
}

export default async function Hotels () {
    // const data = await getHotels();

    return (
        <HandlerError>
            <HandlerError.When hasError={false}>
                <Map />
            </HandlerError.When>
            <HandlerError.Else>
                <>
                    Algo paso
                </>
            </HandlerError.Else>
        </HandlerError>
    );
}
