"use client"

import { Button, Card, CardActions, CardContent, CardHeader, Grid, Stack, Typography } from "@mui/material";
import PageHeaderRoom from "./PageHeaderRoom";
import { Session } from "next-auth";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ChangeSwitchStatus from "@/components/rooms/ChangeSwitchStatus";
import { RoomCard } from "@/model/types";
import { Fragment, useCallback, useContext, useState } from "react";
import { getData } from "@/api/getData";
import { Advice } from "@/contexts/AdviceProvider";

import { Confirm } from "@/contexts/ConfirmContext";
import { deleteData } from "@/api/deleteData";
import useModal from "@/hooks/useModal";
import { useRouter } from "next/navigation";

import { Loading } from "@/contexts/LoadingProvider";
import '../globals.css'
import { ROOMS_DELETE, ROOMS_GENERAL } from "@/constants/endpoints";

type Props = {
    session: Session | null;
    res : RoomCard[];
}

function useCardRoom ({ res, session } : {res : RoomCard[], session: Session | null}) {
    const [ rooms, setRooms ] = useState<RoomCard[]>(res);
    const { handleOpen, handleAdvice } = useContext(Advice);
    const { handleMessage, confirm } = useContext(Confirm);
    const { handleOpenLoading, handleCloseLoading } = useContext(Loading);
    const router = useRouter();

    const handleReload = useCallback(async () => {
        const res = await getData<RoomCard>(`${ROOMS_GENERAL}`, true, session?.token); 

        if(res.data) {
            setRooms(res.data as RoomCard[]);
        }
        else {
            handleAdvice({
                message: res.err,
                horizontal: 'left',
                vertical: 'bottom',
                status: 500
            });

            handleOpen();
        }
    }, [])

    const handleDelete = useCallback(async (id: string) => {
        try {
            handleMessage({
                title: 'Are you sure you want to delete this room?',
                content: 'Once a room is deleted, it is not possible to recover any data from it. This includes images and information about requests made to that room. It is recommended to hide the room before deleting it'
            })

            await confirm()
                .catch(() => { throw ({canceled: true})})

            handleOpenLoading();

            if(session != null) {
                const res = await deleteData<{id: string}[]>(`${ROOMS_DELETE}`, [{ id: id }], session.token);

                if(res.status >= 200 && res.status <= 299) {
                    await handleReload();

                    handleAdvice({
                        message: res.message,
                        status: res.status
                    })

                    handleOpen();
                }
                else {
                    handleAdvice({
                        message: res.err,
                        status: res.status
                    })

                    handleOpen();
                }
            }
            else {
                handleAdvice({
                    message: 'Unauthorized session',
                    status: 401
                })

                handleOpen();
            }
        }
        catch (err : unknown) {
            if(err instanceof Error) {
                handleAdvice({
                    message: err.message.charAt(0).toUpperCase() + err.message.slice(1),
                    status: 503
                })

                handleOpen();
            }

            return;
        }
        finally {
            handleCloseLoading();
        }
    }, [])

    const handleEdit = useCallback((id : string) => {
        router.push(`/rooms/${id}`)
    }, [])

    return {
        rooms,
        confirm,
        handleOpen,
        handleAdvice,
        handleMessage,
        handleReload,
        handleDelete,
        handleEdit
    };
}

export default function CardRoom (props : Props) {
    const { res, session } = props;
    const { rooms, handleReload, handleDelete, handleEdit } = useCardRoom({ res, session });
    const { open, handleOpen, handleClose } = useModal<RoomCard>();

    return (
        <Fragment>
            <PageHeaderRoom 
                title='Rooms'
                buttonCreate="New room"
                reloadAction={handleReload}
                handleClose={handleClose}
                handleOpen={handleOpen}
                open={open}
            />
            {rooms !== undefined && rooms.length > 0 ? 
                <Grid container spacing={2}>  
                    {rooms.map((room) => (
                            <Grid 
                                key={room.id}
                                item 
                                sm={12} 
                                md={6} 
                                lg={4}
                            >
                                <Card
                                    className="card-content"
                                >
                                    <CardHeader
                                        title={room.name}
                                    />
                                    <img 
                                        src={`data:image/webp;base64,${room.file}`}
                                        alt={room.file_name}
                                        className="card-img-room"
                                    />
                                    <CardContent
                                        className="card-room"
                                    >
                                        <Typography
                                            variant="subtitle1"
                                        >
                                            {room.additional}
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                        >
                                            {room.summary}
                                        </Typography>
                                    </CardContent>
                                    <CardActions className="action-card">
                                        <Stack direction="row" spacing={1.5}>
                                            <Button
                                                color="info"
                                                variant="contained"
                                                endIcon={<EditIcon />}
                                                size="small"
                                                onClick={() => handleEdit(room.id)}
                                            >
                                                Edit
                                            </Button>
                                            <Button
                                                color="error"
                                                variant="contained"
                                                endIcon={<DeleteIcon />}
                                                size="small"
                                                onClick={() => handleDelete(room.id)}
                                            >
                                                Delete
                                            </Button>
                                        </Stack>
                                        <ChangeSwitchStatus 
                                            id={room.id}
                                            status={room.status}
                                            handleReload={handleReload}
                                        />
                                    </CardActions>
                                </Card>
                            </Grid>
                        ))
                    }
                </Grid> : 
                <Typography variant="h2" className="advice-center">No content</Typography>
            }
        </Fragment>
    );
}