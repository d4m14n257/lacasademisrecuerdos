import { getData } from "@/api/getData";
import { Box } from "@mui/material";
import { HandlerError, HandlerMessageError } from "@/components/handler/HandlerError";
import { ConfirmProvider } from "@/contexts/ConfirmContext";
import { authOptions } from "@/lib/authOptions";
import { RoomLanguage } from "@/model/types";
import { getServerSession } from "next-auth";
import CardByIdRoom from "@/components/rooms/CardByIdRoom";

import '../../additional.css';
import { ROOMS_GENERAL } from "@/constants/endpoints";

export default async function RoomId ({ params } : { params: { id : string }}) {
    const session = await getServerSession(authOptions);
    const res = await getData<RoomLanguage>(`${ROOMS_GENERAL}/${params.id}`, true, session?.token);

    return (
        <HandlerError>
            <HandlerError.When hasError={Boolean(res.err)}>
                <ConfirmProvider>
                    <Box className="content-page">
                        <CardByIdRoom 
                            res={res.data as RoomLanguage}
                            session={session}
                        />
                    </Box>
                </ConfirmProvider>
            </HandlerError.When>
            <HandlerError.Else>
                <HandlerMessageError 
                    message={res.message || res.err}
                    status={res.status}
                />
            </HandlerError.Else>
        </HandlerError>
    );
}