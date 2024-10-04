"use client"

import { ReactNode, useState } from "react";
import { Collapse, IconButton, List, ListItem, ListItemText, Paper, Stack, Tooltip, Typography } from "@mui/material";

import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import EditIcon from '@mui/icons-material/Edit';

import '../globals.css'

type Props<T> = {
    title: string;
    titleAction?: string;
    data?: T[] | T | null;
    handleAction?: () => void;
    hasCollapse?: boolean;
    isAction?: boolean;
    children: ReactNode;
}

const useListComponent = () => {
    const [open, setOpen] = useState<boolean>(true);

    const handleChange = () => {
        setOpen(!open)
    }

    return {
        open,
        handleChange
    }
}

export default function ListComponent<T>(props : Props<T>) {
    const { title, titleAction, handleAction, hasCollapse, isAction, children } = props;
    const { open, handleChange } = useListComponent();

    return (
        <Paper
            className="card-content height-collapse"
        >
            <List>
                <ListItem>
                    <ListItemText 
                        primary={
                            <Typography variant="h5">
                                {title}
                            </Typography>
                        }
                    />
                    <Stack spacing={2} flexDirection="row">
                        {isAction &&
                            <Tooltip
                                title={titleAction}
                                sx={{margin: 0}}
                            >
                                <IconButton size="large" edge="end" onClick={handleAction}>
                                    <EditIcon />
                                </IconButton>
                            </Tooltip>
                        }
                        {hasCollapse &&
                            <Tooltip
                                title={open ? 'Close' : 'Open'}
                            >
                                <IconButton size="large" edge="end" onClick={handleChange} style={{ margin: 0 }}>
                                    {open ? <ExpandLess /> : <ExpandMore />}
                                </IconButton>
                            </Tooltip>
                        }
                    </Stack>
                </ListItem>
                <Collapse
                    in={open}
                    timeout='auto'
                    unmountOnExit
                >
                    {children}
                </Collapse>
            </List>
        </Paper>
    );
}