"use client"

import { Fragment } from "react";
import { Room } from "@/model/types";
import { Box, Card, CardActions, CardContent, CardHeader, Divider, IconButton, Stack, Tooltip, Typography } from "@mui/material";
import ChangeSwitchStatus from "./ChangeSwitchStatus";

import EditIcon from '@mui/icons-material/Edit';
import useModal from "@/hooks/useModal";
import CreateRoomModal from "./modals/CreateRoomModal";
import { getDate } from "@/lib/getDate";

import '../globals.css'

type RoomWithoutFiles = Omit<Room, 'files'>;

interface Props extends RoomWithoutFiles {
    handleReload: () => Promise<void>
}

export default function CardRoomInfo (props : Props) {
    const { id, name, description, summary, additional, single_price, double_price, created_at, status, handleReload} = props;
    const { open, handleOpen, handleClose, data } = useModal<Omit<Room, 'files' | 'created_at' | 'status'>>();

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
                                id, name, description, summary, additional, single_price, double_price
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
                            Description:
                        </Typography>
                        <Typography variant="body2">
                            {description}
                        </Typography>
                    </Box>
                    <Box>
                        <Typography variant="h6">
                            Summary:
                        </Typography>
                        <Typography variant="body2">
                            {summary}
                        </Typography>
                    </Box>
                    <Box>
                        <Typography variant="h6">
                            Additional:
                        </Typography>
                        <Typography variant="body2">
                            {additional}
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
                    />
                </CardActions>
            </Card>
            {open && 
                <CreateRoomModal 
                    open={open}
                    handleClose={handleClose}
                    reloadAction={handleReload}
                    room={data as Omit<Room, 'files' | 'created_at' | 'status'>}
                    closeConfirm
                />
            }
        </Fragment>
    );  
}