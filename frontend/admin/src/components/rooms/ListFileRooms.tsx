"use client"

import { Fragment, useCallback, useContext } from "react";
import { Avatar, Box, Divider, Icon, IconButton, ListItem, ListItemAvatar, ListItemText, Stack, Tooltip } from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import ChangeCircleIcon from '@mui/icons-material/ChangeCircle';
import UpdateIcon from '@mui/icons-material/Update';
import useModal from "@/hooks/useModal";
import EditFilesModal from "./modals/EditFilesModal";
import ListComponent from "../general/ListComponent";
import { FileId, FilesAdminBytes, Source } from "@/model/types";
import { deleteData } from "@/api/deleteData";
import { Session } from "next-auth";
import { Confirm } from "@/contexts/ConfirmContext";
import { Advice } from "@/contexts/AdviceProvider";
import { setData } from "@/api/setData";

type Props = {
    roomId: string
    data: FilesAdminBytes[];
    handleReload: () => Promise<void>;
    session: Session | null;
}

function useListFileRooms (session : Session | null, handleReload: () => Promise<void>) {
    const { handleMessage, confirm } = useContext(Confirm);
    const { handleAdvice, handleOpen } = useContext(Advice);

    const handleDeleteFile = useCallback(async (file : Source) => {
        try {
            handleMessage({
                title: 'Are you sure you want to delete this file?',
                content: 'Once a file is deleted, it is not possible to recover any data from it'
            })
    
            await confirm()
                .catch(() => { throw ({canceled: true})})
    
            if(session != null) {
                const res = await deleteData<Source>('file/admin', file, session.token);

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
                        message: res.message,
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

    }, []);

    const handleChangeMain = useCallback(async (file : FileId) => {
        try {
            handleMessage({
                title: 'Are you want to change the main image?',
                content: 'If you change the main image, none will be deleted, it will only be replaced and shown to the client'
            })
    
            await confirm()
                .catch(() => { throw ({canceled: true})})

            if(session != null) {
                const res = await setData<FileId>("admin/main/room", session.token, file, "PUT");

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
                        message: res.message,
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
    }, [])

    return {
        handleDeleteFile,
        handleChangeMain
    }
}

export default function ListFileRooms (props : Props) {
    const { roomId, data, session, handleReload } = props;
    const { open, handleOpen, handleClose } = useModal<unknown>();
    const { handleDeleteFile, handleChangeMain } = useListFileRooms(session, handleReload);

    return (
        <Fragment>
            <ListComponent<File>
                title='Room files'
                titleAction="Upload images"
                handleAction={handleOpen}
                hasCollapse
                isAction
            >
                {data.map(file =>  (
                    <Box
                        key={file.id}
                    >
                        <Divider variant="middle" component="li" />
                        <ListItem
                            secondaryAction={
                                !file.main &&
                                    <Stack flexDirection="row" alignContent="center" spacing={2}>
                                        <Tooltip
                                            title="Change main image"
                                        >
                                        <IconButton onClick={() => handleChangeMain({ id: roomId, file_id: file.id })}>
                                            <ChangeCircleIcon />
                                        </IconButton>
                                        </Tooltip>
                                        <Tooltip
                                            title="Delete file"
                                        >
                                            <IconButton onClick={() => handleDeleteFile({id: file.id, source: file.source, main: file.main})}>
                                                <DeleteIcon color="error" />
                                            </IconButton>
                                        </Tooltip>
                                    </Stack>
                            }
                        >   
                            <ListItemAvatar>
                                <Avatar 
                                    src={`data:image/webp;base64,${file.file}`}
                                    sx={{ width: 56, height: 56, marginRight: 2 }}
                                    variant="square"
                                />
                            </ListItemAvatar>
                            <ListItemText 
                                primary={file.name}
                                secondary={file.main ? `This is the main image - ${file.mime}` : file.mime }
                            />
                        </ListItem>
                    </Box>
                ))}
            </ListComponent>
            {open &&
                <EditFilesModal 
                    open={open}
                    handleClose={handleClose}
                    handleReload={handleReload}
                    roomId={roomId}
                    closeConfirm
                />
            }
        </Fragment>
    );
}