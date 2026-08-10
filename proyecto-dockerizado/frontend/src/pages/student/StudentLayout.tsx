import { useContext, ReactNode } from 'react';
import { useNavigate } from 'react-router';
import {
    AppBar,
    Avatar,
    Box,
    Drawer,
    IconButton,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from '@mui/material';

import {
    Dashboard,
    FitnessCenter,
    DirectionsRun,
    Schedule,
    BarChart,
    Event,
    Message,
    Notifications,
    Recommend,
    Logout,
} from '@mui/icons-material';

import AuthContext from '../../context/AuthContext';

const DRAWER_WIDTH = 240;

const navItems = [
    { label: 'Resumen', sectionId: 'summary', icon: <Dashboard /> },
    { label: 'Rutinas', sectionId: 'routines', icon: <FitnessCenter /> },
    { label: 'Ejercicios', sectionId: 'exercises', icon: <DirectionsRun /> },
    { label: 'Sesiones', sectionId: 'sesions', icon: <Schedule /> },
    { label: 'Estadísticas', sectionId: 'stats', icon: <BarChart /> },
    { label: 'Eventos', sectionId: 'events', icon: <Event /> },
    { label: 'Mensajes', sectionId: 'messages', icon: <Message /> },
    { label: 'Notificaciones', sectionId: 'notifications', icon: <Notifications /> },
    { label: 'Recomendaciones', sectionId: 'recomendations', icon: <Recommend /> },
];

interface StudentLayoutProps {
    children: ReactNode;
}

function StudentLayout({ children }: StudentLayoutProps) {
    const auth = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        auth?.logout();
        navigate('/');
    };

    const goToSection = (sectionId: string) => {
        const element = document.getElementById(sectionId);

        if (element) {
            element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    };

    const username = auth?.user?.username ?? 'Student';

    return (
        <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'grey.100' }}>
            <AppBar
                position="fixed"
                sx={{
                    zIndex: (theme) => theme.zIndex.drawer + 1,
                    bgcolor: '#111827',
                }}
            >
                <Toolbar sx={{ justifyContent: 'space-between' }}>
                    <Typography variant="h6" noWrap sx={{ fontWeight: 700 }}>
                        Panel Student
                    </Typography>

                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Avatar sx={{ width: 32, height: 32, bgcolor: 'success.main' }}>
                            {username.charAt(0).toUpperCase()}
                        </Avatar>

                        <Typography variant="body2">{username}</Typography>

                        <IconButton color="inherit" onClick={handleLogout} title="Cerrar sesión">
                            <Logout />
                        </IconButton>
                    </Box>
                </Toolbar>
            </AppBar>

            <Drawer
                variant="permanent"
                sx={{
                    width: DRAWER_WIDTH,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: DRAWER_WIDTH,
                        boxSizing: 'border-box',
                        bgcolor: '#0f172a',
                        color: '#fff',
                    },
                }}
            >
                <Toolbar />

                <Box sx={{ overflow: 'auto', py: 1 }}>
                    <List>
                        {navItems.map((item) => (
                            <ListItem key={item.sectionId} disablePadding>
                                <ListItemButton
                                    onClick={() => goToSection(item.sectionId)}
                                    sx={{
                                        '&:hover': {
                                            bgcolor: 'rgba(255,255,255,0.08)',
                                        },
                                    }}
                                >
                                    <ListItemIcon sx={{ color: '#bbf7d0' }}>
                                        {item.icon}
                                    </ListItemIcon>

                                    <ListItemText primary={item.label} />
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Box>
            </Drawer>

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    mt: 8,
                    p: 3,
                    overflowX: 'hidden',
                }}
            >
                {children}
            </Box>
        </Box>
    );
}

export default StudentLayout;
