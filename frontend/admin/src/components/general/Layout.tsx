"use client"

import { useState } from "react";
import { useRouter } from "next/navigation";
import { 
    Avatar,
    Box, 
    CircularProgress, 
    Container, 
    CssBaseline, 
    Divider, 
    Drawer, 
    IconButton, 
    List, 
    ListItem, 
    ListItemButton, 
    ListItemIcon, 
    ListItemText, 
    Stack, 
    styled, 
    Toolbar, 
    Typography, 
    useTheme 
} from "@mui/material";
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';

import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import MenuIcon from '@mui/icons-material/Menu';
import { useSession } from "next-auth/react";
import { useMediaQuery } from "@mui/material";
import { routes } from "@/constants/routes";

import '../globals.css'
import CardSettings from "./CardSettings";

const drawerWidth = 240;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })<{
    open?: boolean;
    isMobile?: boolean
}>(({ theme, open, isMobile }) => ({
    flexGrow: 1,
    padding: theme.spacing(3),
    ...(!isMobile && {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
    })}),
    ...(isMobile ? 
        {marginLeft: 0} :
        {
            marginLeft: `-${drawerWidth}px`,
                ...(open && {
            transition: theme.transitions.create('margin', {
                easing: theme.transitions.easing.easeOut,
                duration: theme.transitions.duration.enteringScreen,
                }),
            marginLeft: 0, 
        })
    }),
}));

interface AppBarProps extends MuiAppBarProps {
    open?: boolean;
}

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})<AppBarProps>(({ theme, open }) => ({
    transition: theme.transitions.create(['margin', 'width'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    marginLeft: `${drawerWidth}px`,
    transition: theme.transitions.create(['margin', 'width'], {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
    minHeight: 64
}));

const useLayout = () => {
    const { data: session, status } = useSession();
    const [open, setOpen] = useState<boolean>(false);
    const [openSettings, setOpenSettings] = useState<boolean>(false);
    const route = useRouter();
    const theme = useTheme();
    const isMobile = useMediaQuery('(max-width:720px)');

    const handleChangeDrawn : () => void = () => {
        setOpen(!open);
    }

    const handleChangeSetting : () => void = () => {
        setOpenSettings(!openSettings)
    }

    const handleChangeRoute = (url : string) => {
        route.push(`/${url}`);
    }

    return {
        isMobile,
        session,
        status,
        open,
        openSettings,
        theme,
        handleChangeDrawn,
        handleChangeSetting,
        handleChangeRoute
    }
}

export default function Layout (
    {children} : Readonly<{children: React.ReactNode}>
) {
    const { isMobile, session, status, open, openSettings, theme, handleChangeDrawn, handleChangeSetting, handleChangeRoute } = useLayout();

    if(status == "unauthenticated") {
        return (
            <Box>
            <CssBaseline />
            <Main open>
                {children}
            </Main>
        </Box>
        );
    }
    else if(status == "authenticated") {
        return (
            <Box sx={{ display: 'flex' }}>
                <CssBaseline />
                <AppBar position="fixed" open={!isMobile && open} >
                    <Toolbar className="header">
                        <Stack direction='row' alignItems='center' spacing={1}>
                            <IconButton
                                color="inherit"
                                aria-label="open drawer"
                                onClick={handleChangeDrawn}
                                edge="start"
                                sx={{ mr: 2, ...((!isMobile && open) && { display: 'none' }) }}
                            >
                                <MenuIcon />
                            </IconButton>
                            <Typography variant="h6" noWrap component="div">
                                La casa de mis recuerdos
                            </Typography>
                        </Stack>
                        <IconButton onClick={handleChangeSetting}>
                            <Avatar>
                                {session?.user.username.charAt(0).toUpperCase()}
                            </Avatar>
                        </IconButton>
                    </Toolbar>
                </AppBar>
                <Drawer
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        '& .MuiDrawer-paper': {
                            width: drawerWidth,
                            boxSizing: 'border-box',
                        },
                    }}
                    variant={isMobile ? "temporary" : "persistent"}
                    anchor="left"
                    open={open}
                >
                    <DrawerHeader>
                        <IconButton onClick={handleChangeDrawn}>
                            {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
                        </IconButton>
                    </DrawerHeader>
                    <Divider />
                    <List>
                        {routes.map(({id, name, Icon}) => (
                            <ListItem
                                key={id}
                                disablePadding
                            >
                                <ListItemButton
                                    onClick={() => handleChangeRoute(id)}
                                >
                                    <ListItemIcon>
                                        <Icon />
                                    </ListItemIcon>
                                    <ListItemText primary={name} />
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Drawer>
                <Main open={open} isMobile={isMobile}>
                    <Container
                        maxWidth='xl'
                        sx={{ padding: "0 24px 0 24px" }}
                    >
                        <DrawerHeader />
                        {children}
                    </Container>
                </Main>
                {openSettings &&
                    <CardSettings 
                        handleChangeOpen={handleChangeSetting}
                        open={openSettings}
                    />
                }
            </Box>
        );
    }

    return (
        <Box className='loading'>
            <CircularProgress />
        </Box>
    );
}