import { Box, Button, Card, CardActions, CardContent, CardHeader, Grid, Stack, Switch, Typography } from "@mui/material";
import { getServerSession } from "next-auth";
import { getData } from "@/api/getData";
import { HandlerError } from "@/components/handler/HandlerError";
import { ConfirmProvider } from "@/contexts/ConfirmContext";
import { RoomCard } from "@/model/types";
import PageHeader from "@/components/general/PageHeader";
import ChangeSwitchStatus from "@/components/rooms/ChangeSwitchStatus";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

import '../additional.css'
import { authOptions } from "@/lib/authOptions";

export default async function RoomsPage () {
    const session = await getServerSession(authOptions);
    const res = await getData<RoomCard>('room/admin', true, session?.token);

    return (
        <HandlerError>
            <HandlerError.When hasError={Boolean(res.err)}>
                <ConfirmProvider>
                    <Box
                        className='content-page'
                    >
                        <PageHeader 
                            title='Rooms'
                            buttonCreate="New room"
                        />
                        {res.data !== undefined && res.data.length > 0 ? 
                            <Grid container spacing={2}>  
                                {res.data.map((room) => (
                                        <Grid 
                                            key={room.id}
                                            item 
                                            sm={12} 
                                            md={6} 
                                            lg={4}
                                        >
                                            <Card
                                                className="card-content-room"
                                            >
                                                <CardHeader
                                                    title={room.name}
                                                />
                                                <img 
                                                    src={`data:image/png;base64,${room.file}`}
                                                    alt={room.file_name}
                                                    style={{
                                                        width: '100%'
                                                    }}
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
                                                        >
                                                            Edit
                                                        </Button>
                                                        <Button
                                                            color="error"
                                                            variant="contained"
                                                            endIcon={<DeleteIcon />}
                                                            size="small"
                                                        >
                                                            Delete
                                                        </Button>
                                                    </Stack>
                                                    <ChangeSwitchStatus 
                                                        id={room.id}
                                                        status={room.status}
                                                    />
                                                </CardActions>
                                            </Card>
                                        </Grid>
                                ))}
                            </Grid> : 
                            <Typography variant="h2" className="advice-center">No content</Typography>}
                    </Box>
                </ConfirmProvider>
            </HandlerError.When>
            <HandlerError.Else>
                <>
                    Soy un error
                </>
            </HandlerError.Else>
        </HandlerError>
    );
}