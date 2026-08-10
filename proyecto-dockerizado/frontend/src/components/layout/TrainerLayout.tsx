import { useContext } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router';
import {
    Box,
    Drawer,
    AppBar,
    Toolbar,
    List,
    Typography,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    IconButton,
    Avatar,
} from '@mui/material';
import {
    FitnessCenter,
    DirectionsRun,
    Schedule,
    People,
    Recommend,
    Message,
    BarChart,
    Logout,
} from '@mui/icons-material';
import AuthContext from '../../context/AuthContext';

const DRAWER_WIDTH = 240;

const navItems = [
    { label: 'Rutinas', path: '/trainer/routines', icon: <FitnessCenter /> },
    { label: 'Ejercicios', path: '/trainer/exercises', icon: <DirectionsRun /> },
    { label: 'Sesiones', path: '/trainer/sesions', icon: <Schedule /> },
    { label: 'Estudiantes', path: '/trainer/students', icon: <People /> },
    { label: 'Recomendaciones', path: '/trainer/recomendations', icon: <Recommend /> },
    { label: 'Mensajes', path: '/trainer/messages', icon: <Message /> },
    { label: 'Estadísticas', path: '/trainer/stats', icon: <BarChart /> },
];

function TrainerLayout() {
    const auth = useContext(AuthContext);
    const nav = useNavigate();
    const location = useLocation();

    const handleLogout = () => {
        auth?.logout();
        nav('/');
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <AppBar
                position="fixed"
                sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
            >
                <Toolbar sx={{ justifyContent: 'space-between' }}>
                    <Typography variant="h6" noWrap sx={{ fontWeight: 700 }}>
                        Panel Trainer
                    </Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Avatar sx={{ width: 32, height: 32, bgcolor: 'secondary.main' }}>
                            {auth?.user?.username?.charAt(0)?.toUpperCase()}
                        </Avatar>
                        <Typography variant="body2">{auth?.user?.username}</Typography>
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
                    },
                }}
            >
                <Toolbar />
                <Box sx={{ overflow: 'auto' }}>
                    <List>
                        {navItems.map((item) => (
                            <ListItem key={item.path} disablePadding>
                                <ListItemButton
                                    selected={location.pathname === item.path}
                                    onClick={() => nav(item.path)}
                                >
                                    <ListItemIcon>{item.icon}</ListItemIcon>
                                    <ListItemText primary={item.label} />
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Box>
            </Drawer>

            <Box component="main" sx={{ flexGrow: 1, p: 3, mt: 8 }}>
                <Outlet />
            </Box>
        </Box>
    );
}

export default TrainerLayout;
