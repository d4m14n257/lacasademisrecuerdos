"use client"

import { Fragment } from "react";
import { Room, RoomLanguage } from "@/model/types";
import { Box, Card, CardActions, CardContent, CardHeader, Divider, IconButton, Stack, Tooltip, Typography } from "@mui/material";
import ChangeSwitchStatus from "./ChangeSwitchStatus";

import EditIcon from '@mui/icons-material/Edit';
import useModal from "@/hooks/useModal";
import CreateRoomModal from "./modals/CreateRoomModal";
import { getDate } from "@/lib/getDate";

import '../globals.css'

type RoomWithoutFiles = Omit<RoomLanguage, 'files'>;

interface Props extends RoomWithoutFiles {
    handleReload: () => Promise<void>
}

export default function CardRoomInfo (props : Props) {
    const { id, name, description_es, description_en, summary_es, summary_en, single_price, double_price, created_at, status, handleReload} = props;
    const { open, handleOpen, handleClose, data } = useModal<Omit<RoomLanguage, 'files' | 'created_at' | 'status'>>();

    return (
        <Fragment>
            <Card className="card-content">
                <CardHeader 
                    title={name}
                    subheader={`Created: ${getDate(created_at)}`}
                    action={
                        <Tooltip
                            title="Edit room"
                        >
                            <IconButton onClick={() => handleOpen({
                                id, name, description_es, description_en, summary_es, summary_en, single_price, double_price
                            })}>
                                <EditIcon />
                            </IconButton>
                        </Tooltip>
                    }
                />
                <Divider />
                <CardContent
                    className="card-body"
                >
                    <Box>
                        <Typography variant="h6">
                            Description in English:
                        </Typography>
                        <Typography variant="body2">
                            {description_en}
                        </Typography>
                    </Box>
                    <Box>
                        <Typography variant="h6">
                            Summary in English:
                        </Typography>
                        <Typography variant="body2">
                            {summary_en}
                        </Typography>
                    </Box>
                    <Box>
                        <Typography variant="h6">
                            Description in Spanish:
                        </Typography>
                        <Typography variant="body2">
                            {description_es}
                        </Typography>
                    </Box>
                    <Box>
                        <Typography variant="h6">
                            Summary in Spanish:
                        </Typography>
                        <Typography variant="body2">
                            {summary_es}
                        </Typography>
                    </Box>
                </CardContent>
                <Divider />
                <CardActions className="card-action-room">
                    <Stack flexDirection='column'>
                        <Typography variant="subtitle1">
                            Single price: ${single_price}
                        </Typography>
                        <Typography variant="subtitle1">
                            Double price: {double_price ? `$ ${double_price}` : 'Unassigned'}
                        </Typography>
                    </Stack>
                    <ChangeSwitchStatus 
                        id={id}
                        status={status}
                        handleReload={handleReload}
                    />
                </CardActions>
            </Card>
            {open && 
                <CreateRoomModal 
                    open={open}
                    handleClose={handleClose}
                    reloadAction={handleReload}
                    room={data as Omit<RoomLanguage, 'files' | 'created_at' | 'status'>}
                    closeConfirm
                />
            }
        </Fragment>
    );  
}