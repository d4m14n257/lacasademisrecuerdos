import { Button, Card, CardActions, CardContent, CardHeader, CardMedia, Stack, Typography } from "@mui/material";
import { getData } from "@/api/getData";
import { HandlerError } from "@/components/handler/HandlerError";
import { RoomCard } from "@/model/types";
import PageHeader from "@/components/general/PageHeader";

import '../additional.css'

export default async function RoomsPage () {
    const res = await getData<RoomCard>('hotel');

    const handleCreateRoom = () => {

    }

    return (
        <HandlerError>
            <HandlerError.When hasError={Boolean(res.err)}>
                <>
                    <PageHeader 
                        title='Rooms'
                        buttonCreate="New room"
                    />
                    {res.data !== undefined && res.data.length > 0 ? 
                        <>  
                            {
                                res.data.map((room) => {
                                    <Card
                                        key={room.id}
                                    >
                                        <CardHeader
                                            title={room.name}
                                        />
                                        <CardMedia 
                                                component='img'
                                                image={room.file}
                                            />
                                        <CardContent>
                                            <Typography>
                                                {room.summary}
                                            </Typography>
                                        </CardContent>
                                        <CardActions>
                                            <Button>
                                                Edit
                                            </Button>
                                            <Button>
                                                Delete
                                            </Button>
                                        </CardActions>
                                    </Card>
                                })
                            }
                        </> : 
                        <Typography variant="h2" className="adviceCenter">No content</Typography>}
                </>
            </HandlerError.When>
            <HandlerError.Else>

            </HandlerError.Else>
        </HandlerError>
    );
}